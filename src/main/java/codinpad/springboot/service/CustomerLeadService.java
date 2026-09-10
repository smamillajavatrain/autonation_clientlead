package codinpad.springboot.service;

import codinpad.springboot.dto.request.AssignLeadRequest;
import codinpad.springboot.dto.request.CloseLeadRequest;
import codinpad.springboot.dto.request.CustomerLeadRequest;
import codinpad.springboot.dto.response.CustomerLeadResponse;
import codinpad.springboot.dto.response.LeadReportResponse;
import codinpad.springboot.enums.LeadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerLeadService {

    CustomerLeadResponse createLead(
            CustomerLeadRequest request,
            String idempotencyKey);

    CustomerLeadResponse getLead(Long id);

    CustomerLeadResponse assignLead(
            Long leadId,
            AssignLeadRequest request);

    CustomerLeadResponse moveToInitialCall(Long leadId);

    CustomerLeadResponse moveToProposal(Long leadId);

    CustomerLeadResponse moveToPresentProposal(Long leadId);

    CustomerLeadResponse closeLead(
            Long leadId,
            CloseLeadRequest request);

    Page<LeadReportResponse> searchLeads(
            String name,
            String email,
            String phone,
            Long accountManagerId,
            LeadStatus status,
            Pageable pageable);
}
