package codinpad.springboot.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiErrorResponse {

    private LocalDateTime timestamp;
    private String errorCode;
    private String message;
    private String path;
    private Map<String, String> fieldErrors;
}
