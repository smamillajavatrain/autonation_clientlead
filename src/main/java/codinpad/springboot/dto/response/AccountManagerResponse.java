package codinpad.springboot.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountManagerResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private boolean active;
    private LocalDateTime createdAt;
}
