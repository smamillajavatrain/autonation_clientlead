package codinpad.springboot.service.impl;

import codinpad.springboot.dto.request.AssignLeadRequest;
import codinpad.springboot.dto.request.CloseLeadRequest;
import codinpad.springboot.dto.request.CustomerLeadRequest;
import codinpad.springboot.dto.response.CustomerLeadResponse;
import codinpad.springboot.dto.response.LeadReportResponse;
import codinpad.springboot.entity.AccountManager;
import codinpad.springboot.entity.CustomerLead;
import codinpad.springboot.entity.LeadAssignmentHistory;
import codinpad.springboot.entity.LeadStatusHistory;
import codinpad.springboot.enums.CloseReason;
import codinpad.springboot.enums.ErrorCode;
import codinpad.springboot.enums.LeadStatus;
import codinpad.springboot.exception.CrmBusinessException;
import codinpad.springboot.mapper.CustomerLeadMapper;
import codinpad.springboot.repository.*;
import codinpad.springboot.service.CustomerLeadService;
import codinpad.springboot.specification.CustomerLeadSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerLeadServiceImpl implements CustomerLeadService {

    private final CustomerLeadRepository leadRepository;
    private final AccountManagerRepository managerRepository;
    private final LeadStatusHistoryRepository statusHistoryRepository;
    private final LeadAssignmentHistoryRepository assignmentHistoryRepository;
    private final CustomerLeadMapper mapper;

    @Override
    public CustomerLeadResponse createLead(
            CustomerLeadRequest request,
            String idempotencyKey) {

        String normalizedEmail = normalizeEmail(request.getEmail());
        String normalizedPhone = normalizePhone(request.getPhone());

        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            Optional<CustomerLead> existing =
                    leadRepository.findByIdempotencyKey(idempotencyKey.trim());

            if (existing.isPresent()) {
                return mapper.toResponse(existing.get());
            }
        }

        // Business duplicate check.
        Optional<CustomerLead> duplicate =
                leadRepository.findFirstByEmailIgnoreCaseAndPhone(
                        normalizedEmail, normalizedPhone);

        if (duplicate.isPresent()) {
            return mapper.toResponse(duplicate.get());
        }

        CustomerLead lead = mapper.toEntity(request);
        lead.setEmail(normalizedEmail);
        lead.setPhone(normalizedPhone);
        lead.setIdempotencyKey(
                idempotencyKey == null || idempotencyKey.isBlank()
                        ? null
                        : idempotencyKey.trim());
        lead.setStatus(LeadStatus.NEW);

        try {
            CustomerLead saved = leadRepository.save(lead);

            saveStatusHistory(
                    saved,
                    null,
                    LeadStatus.NEW,
                    null,
                    "Lead created");

            return mapper.toResponse(saved);

        } catch (DataIntegrityViolationException ex) {
            // Handles concurrent requests that hit the unique constraint.
            if (idempotencyKey != null && !idempotencyKey.isBlank()) {
                return leadRepository.findByIdempotencyKey(
                                idempotencyKey.trim())
                        .map(mapper::toResponse)
                        .orElseThrow(() -> ex);
            }
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerLeadResponse getLead(Long id) {
        return mapper.toResponse(findLead(id));
    }

    @Override
    public CustomerLeadResponse assignLead(
            Long leadId,
            AssignLeadRequest request) {

        CustomerLead lead = findLead(leadId);

        if (lead.getStatus() == LeadStatus.CLOSED) {
            throw new CrmBusinessException(
                    ErrorCode.LEAD_ALREADY_CLOSED,
                    "Lead " + leadId + " is already closed");
        }

        AccountManager newManager =
                findActiveManager(request.getAccountManagerId());

        AccountManager assignedBy = null;
        if (request.getAssignedById() != null) {
            assignedBy = findActiveManager(request.getAssignedById());
        }

        AccountManager oldManager = lead.getAccountManager();
        LeadStatus oldStatus = lead.getStatus();

        lead.setAccountManager(newManager);

        if (oldStatus == LeadStatus.NEW) {
            lead.setStatus(LeadStatus.ASSIGNED);
        }

        CustomerLead saved = leadRepository.save(lead);

        assignmentHistoryRepository.save(
                LeadAssignmentHistory.builder()
                        .lead(saved)
                        .oldAccountManager(oldManager)
                        .newAccountManager(newManager)
                        .assignedBy(assignedBy)
                        .build());

        if (oldStatus != saved.getStatus()) {
            saveStatusHistory(
                    saved,
                    oldStatus,
                    saved.getStatus(),
                    assignedBy,
                    "Lead assigned to account manager");
        }

        return mapper.toResponse(saved);
    }

    @Override
    public CustomerLeadResponse moveToInitialCall(Long leadId) {
        return changeStatus(
                leadId,
                LeadStatus.INITIAL_CALL,
                null,
                "Initial call started");
    }

    @Override
    public CustomerLeadResponse moveToProposal(Long leadId) {
        return changeStatus(
                leadId,
                LeadStatus.PROPOSAL,
                null,
                "Lead moved to proposal");
    }

    @Override
    public CustomerLeadResponse moveToPresentProposal(Long leadId) {
        return changeStatus(
                leadId,
                LeadStatus.PRESENT_PROPOSAL,
                null,
                "Proposal is being presented");
    }

    @Override
    public CustomerLeadResponse closeLead(
            Long leadId,
            CloseLeadRequest request) {

        CustomerLead lead = findLead(leadId);

        if (lead.getStatus() == LeadStatus.CLOSED) {
            throw new CrmBusinessException(
                    ErrorCode.LEAD_ALREADY_CLOSED,
                    "Lead " + leadId + " is already closed");
        }

        validateCloseReason(lead.getStatus(), request.getReason());

        LeadStatus oldStatus = lead.getStatus();

        lead.setStatus(LeadStatus.CLOSED);
        lead.setCloseReason(request.getReason());

        CustomerLead saved = leadRepository.save(lead);

        saveStatusHistory(
                saved,
                oldStatus,
                LeadStatus.CLOSED,
                null,
                request.getComments());

        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadReportResponse> searchLeads(
            String name,
            String email,
            String phone,
            Long accountManagerId,
            LeadStatus status,
            Pageable pageable) {

        Specification<CustomerLead> specification =
                Specification
                        .where(CustomerLeadSpecification.nameContains(name))
                        .and(CustomerLeadSpecification.emailContains(email))
                        .and(CustomerLeadSpecification.phoneContains(phone))
                        .and(CustomerLeadSpecification.managerEquals(accountManagerId))
                        .and(CustomerLeadSpecification.statusEquals(status));

        return leadRepository.findAll(specification, pageable)
                .map(this::toReportResponse);
    }

    private CustomerLeadResponse changeStatus(
            Long leadId,
            LeadStatus newStatus,
            AccountManager changedBy,
            String comments) {

        CustomerLead lead = findLead(leadId);

        LeadStatus oldStatus = lead.getStatus();

        validateTransition(oldStatus, newStatus);

        lead.setStatus(newStatus);

        CustomerLead saved = leadRepository.save(lead);

        saveStatusHistory(
                saved,
                oldStatus,
                newStatus,
                changedBy,
                comments);

        return mapper.toResponse(saved);
    }

    private void validateTransition(
            LeadStatus current,
            LeadStatus next) {

        boolean valid =
                (current == LeadStatus.NEW && next == LeadStatus.ASSIGNED)
                || (current == LeadStatus.ASSIGNED
                    && next == LeadStatus.INITIAL_CALL)
                || (current == LeadStatus.INITIAL_CALL
                    && next == LeadStatus.PROPOSAL)
                || (current == LeadStatus.PROPOSAL
                    && next == LeadStatus.PRESENT_PROPOSAL)
                || (current == LeadStatus.INITIAL_CALL
                    && next == LeadStatus.CLOSED)
                || (current == LeadStatus.PRESENT_PROPOSAL
                    && next == LeadStatus.CLOSED);

        if (!valid) {
            throw new CrmBusinessException(
                    ErrorCode.INVALID_LEAD_TRANSITION,
                    "Cannot change lead status from "
                            + current + " to " + next);
        }
    }

    private void validateCloseReason(
            LeadStatus current,
            CloseReason reason) {

        if (current == LeadStatus.INITIAL_CALL
                && reason != CloseReason.CANT_MEET_NEED) {

            throw new CrmBusinessException(
                    ErrorCode.INVALID_REQUEST,
                    "A lead closed during Initial Call must use "
                            + "CANT_MEET_NEED");
        }

        if (current == LeadStatus.PRESENT_PROPOSAL
                && reason == CloseReason.CANT_MEET_NEED) {

            throw new CrmBusinessException(
                    ErrorCode.INVALID_REQUEST,
                    "CANT_MEET_NEED is applicable during Initial Call");
        }
    }

    private void saveStatusHistory(
            CustomerLead lead,
            LeadStatus oldStatus,
            LeadStatus newStatus,
            AccountManager changedBy,
            String comments) {

        statusHistoryRepository.save(
                LeadStatusHistory.builder()
                        .lead(lead)
                        .oldStatus(oldStatus)
                        .newStatus(newStatus)
                        .changedBy(changedBy)
                        .comments(comments)
                        .build());
    }

    private CustomerLead findLead(Long id) {
        return leadRepository.findById(id)
                .orElseThrow(() -> new CrmBusinessException(
                        ErrorCode.LEAD_NOT_FOUND,
                        "Customer lead not found: " + id));
    }

    private AccountManager findActiveManager(Long id) {
        AccountManager manager = managerRepository.findById(id)
                .orElseThrow(() -> new CrmBusinessException(
                        ErrorCode.ACCOUNT_MANAGER_NOT_FOUND,
                        "Account manager not found: " + id));

        if (!manager.isActive()) {
            throw new CrmBusinessException(
                    ErrorCode.ACCOUNT_MANAGER_INACTIVE,
                    "Account manager " + id + " is inactive");
        }

        return manager;
    }

    private LeadReportResponse toReportResponse(CustomerLead lead) {
        return LeadReportResponse.builder()
                .id(lead.getId())
                .name(lead.getName())
                .phone(lead.getPhone())
                .email(lead.getEmail())
                .accountManagerId(
                        lead.getAccountManager() == null
                                ? null
                                : lead.getAccountManager().getId())
                .accountManager(
                        lead.getAccountManager() == null
                                ? null
                                : lead.getAccountManager().getName())
                .status(lead.getStatus())
                .build();
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private String normalizePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return null;
        }
        return phone.replaceAll("[^0-9+]", "");
    }
}
