package codinpad.springboot.dto.response;

import codinpad.springboot.enums.CloseReason;
import codinpad.springboot.enums.LeadStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerLeadResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String comment;

    private Long accountManagerId;
    private String accountManagerName;

    private LeadStatus status;
    private CloseReason closeReason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
