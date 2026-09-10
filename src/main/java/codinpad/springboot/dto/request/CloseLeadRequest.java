package codinpad.springboot.dto.request;

import codinpad.springboot.enums.CloseReason;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CloseLeadRequest {

    @NotNull(message = "Close reason is required")
    private CloseReason reason;

    @Size(max = 500, message = "Comments must not exceed 500 characters")
    private String comments;
}
