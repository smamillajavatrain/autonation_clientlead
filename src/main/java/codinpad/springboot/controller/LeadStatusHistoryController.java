package codinpad.springboot.controller;

import codinpad.springboot.dto.response.StatusHistoryResponse;
import codinpad.springboot.service.LeadStatusHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadStatusHistoryController {

    private final LeadStatusHistoryService service;

    @GetMapping("/{leadId}/status-history")
    public ResponseEntity<List<StatusHistoryResponse>> getHistory(
            @PathVariable Long leadId) {

        return ResponseEntity.ok(service.getHistory(leadId));
    }
}
