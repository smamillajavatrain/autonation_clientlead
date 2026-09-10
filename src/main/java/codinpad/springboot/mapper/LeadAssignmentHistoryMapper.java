package codinpad.springboot.mapper;

import codinpad.springboot.dto.response.AssignmentHistoryResponse;
import codinpad.springboot.entity.LeadAssignmentHistory;
import org.springframework.stereotype.Component;

@Component
public class LeadAssignmentHistoryMapper {

    public AssignmentHistoryResponse toResponse(
            LeadAssignmentHistory history) {

        return AssignmentHistoryResponse.builder()
                .id(history.getId())
                .leadId(history.getLead().getId())
                .oldAccountManagerId(
                        history.getOldAccountManager() == null
                                ? null
                                : history.getOldAccountManager().getId())
                .oldAccountManagerName(
                        history.getOldAccountManager() == null
                                ? null
                                : history.getOldAccountManager().getName())
                .newAccountManagerId(
                        history.getNewAccountManager().getId())
                .newAccountManagerName(
                        history.getNewAccountManager().getName())
                .assignedById(
                        history.getAssignedBy() == null
                                ? null
                                : history.getAssignedBy().getId())
                .assignedByName(
                        history.getAssignedBy() == null
                                ? null
                                : history.getAssignedBy().getName())
                .assignedAt(history.getAssignedAt())
                .build();
    }
}
