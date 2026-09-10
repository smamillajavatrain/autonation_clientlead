package codinpad.springboot.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentHistoryResponse {

    private Long id;
    private Long leadId;

    private Long oldAccountManagerId;
    private String oldAccountManagerName;

    private Long newAccountManagerId;
    private String newAccountManagerName;

    private Long assignedById;
    private String assignedByName;

    private LocalDateTime assignedAt;
}
