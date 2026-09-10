package codinpad.springboot.service;

import codinpad.springboot.dto.response.StatusHistoryResponse;

import java.util.List;

public interface LeadStatusHistoryService {

    List<StatusHistoryResponse> getHistory(Long leadId);
}
