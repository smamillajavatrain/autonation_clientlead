package codinpad.springboot.controller;

import codinpad.springboot.dto.request.AssignLeadRequest;
import codinpad.springboot.dto.request.CloseLeadRequest;
import codinpad.springboot.dto.request.CustomerLeadRequest;
import codinpad.springboot.dto.response.CustomerLeadResponse;
import codinpad.springboot.dto.response.LeadReportResponse;
import codinpad.springboot.enums.LeadStatus;
import codinpad.springboot.service.CustomerLeadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class CustomerLeadController {

    private final CustomerLeadService service;

    @PostMapping
    public ResponseEntity<CustomerLeadResponse> createLead(
            @RequestHeader(value = "Idempotency-Key", required = false)
            String idempotencyKey,
            @Valid @RequestBody CustomerLeadRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createLead(request, idempotencyKey));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerLeadResponse> getLead(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getLead(id));
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<CustomerLeadResponse> assignLead(
            @PathVariable Long id,
            @Valid @RequestBody AssignLeadRequest request) {

        return ResponseEntity.ok(service.assignLead(id, request));
    }

    @PutMapping("/{id}/initial-call")
    public ResponseEntity<CustomerLeadResponse> initialCall(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.moveToInitialCall(id));
    }

    @PutMapping("/{id}/proposal")
    public ResponseEntity<CustomerLeadResponse> proposal(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.moveToProposal(id));
    }

    @PutMapping("/{id}/present-proposal")
    public ResponseEntity<CustomerLeadResponse> presentProposal(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.moveToPresentProposal(id));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<CustomerLeadResponse> closeLead(
            @PathVariable Long id,
            @Valid @RequestBody CloseLeadRequest request) {

        return ResponseEntity.ok(service.closeLead(id, request));
    }

    @GetMapping("/report")
    public ResponseEntity<Page<LeadReportResponse>> report(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Long accountManagerId,
            @RequestParam(required = false) LeadStatus status,
            @PageableDefault(size = 20, sort = "createdAt",
                    direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(
                service.searchLeads(
                        name,
                        email,
                        phone,
                        accountManagerId,
                        status,
                        pageable));
    }
}
