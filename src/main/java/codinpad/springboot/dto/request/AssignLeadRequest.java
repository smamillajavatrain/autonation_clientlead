package codinpad.springboot.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignLeadRequest {

    @NotNull(message = "Account manager id is required")
    private Long accountManagerId;

    private Long assignedById;
}
