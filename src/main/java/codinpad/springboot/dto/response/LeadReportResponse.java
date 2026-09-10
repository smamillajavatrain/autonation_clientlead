package codinpad.springboot.dto.response;

import codinpad.springboot.enums.LeadStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadReportResponse {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private Long accountManagerId;
    private String accountManager;
    private LeadStatus status;
}
