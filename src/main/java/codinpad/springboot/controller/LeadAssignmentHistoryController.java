package codinpad.springboot.controller;

import codinpad.springboot.dto.response.AssignmentHistoryResponse;
import codinpad.springboot.service.LeadAssignmentHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadAssignmentHistoryController {

    private final LeadAssignmentHistoryService service;

    @GetMapping("/{leadId}/assignment-history")
    public ResponseEntity<List<AssignmentHistoryResponse>> getHistory(
            @PathVariable Long leadId) {

        return ResponseEntity.ok(service.getHistory(leadId));
    }
}
