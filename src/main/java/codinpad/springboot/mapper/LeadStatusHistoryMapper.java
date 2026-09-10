package codinpad.springboot.mapper;

import codinpad.springboot.dto.response.StatusHistoryResponse;
import codinpad.springboot.entity.LeadStatusHistory;
import org.springframework.stereotype.Component;

@Component
public class LeadStatusHistoryMapper {

    public StatusHistoryResponse toResponse(LeadStatusHistory history) {
        return StatusHistoryResponse.builder()
                .id(history.getId())
                .leadId(history.getLead().getId())
                .oldStatus(history.getOldStatus())
                .newStatus(history.getNewStatus())
                .changedById(
                        history.getChangedBy() == null
                                ? null
                                : history.getChangedBy().getId())
                .changedByName(
                        history.getChangedBy() == null
                                ? null
                                : history.getChangedBy().getName())
                .comments(history.getComments())
                .changedAt(history.getChangedAt())
                .build();
    }
}
