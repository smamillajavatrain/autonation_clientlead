package codinpad.springboot.dto.response;

import codinpad.springboot.enums.LeadStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusHistoryResponse {

    private Long id;
    private Long leadId;
    private LeadStatus oldStatus;
    private LeadStatus newStatus;
    private Long changedById;
    private String changedByName;
    private String comments;
    private LocalDateTime changedAt;
}
