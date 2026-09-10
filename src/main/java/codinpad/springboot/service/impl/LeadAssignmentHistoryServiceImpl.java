package codinpad.springboot.service.impl;

import codinpad.springboot.dto.response.AssignmentHistoryResponse;
import codinpad.springboot.mapper.LeadAssignmentHistoryMapper;
import codinpad.springboot.repository.LeadAssignmentHistoryRepository;
import codinpad.springboot.service.LeadAssignmentHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LeadAssignmentHistoryServiceImpl
        implements LeadAssignmentHistoryService {

    private final LeadAssignmentHistoryRepository repository;
    private final LeadAssignmentHistoryMapper mapper;

    @Override
    public List<AssignmentHistoryResponse> getHistory(Long leadId) {
        return repository.findByLeadIdOrderByAssignedAtAsc(leadId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
