package codinpad.springboot.service.impl;

import codinpad.springboot.dto.response.StatusHistoryResponse;
import codinpad.springboot.mapper.LeadStatusHistoryMapper;
import codinpad.springboot.repository.LeadStatusHistoryRepository;
import codinpad.springboot.service.LeadStatusHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LeadStatusHistoryServiceImpl
        implements LeadStatusHistoryService {

    private final LeadStatusHistoryRepository repository;
    private final LeadStatusHistoryMapper mapper;

    @Override
    public List<StatusHistoryResponse> getHistory(Long leadId) {
        return repository.findByLeadIdOrderByChangedAtAsc(leadId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
