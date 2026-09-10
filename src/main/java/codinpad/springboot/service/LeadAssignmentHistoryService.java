package codinpad.springboot.service;

import codinpad.springboot.dto.response.AssignmentHistoryResponse;

import java.util.List;

public interface LeadAssignmentHistoryService {

    List<AssignmentHistoryResponse> getHistory(Long leadId);
}
