package codinpad.springboot.mapper;

import codinpad.springboot.dto.request.AccountManagerRequest;
import codinpad.springboot.dto.response.AccountManagerResponse;
import codinpad.springboot.entity.AccountManager;
import org.springframework.stereotype.Component;

@Component
public class AccountManagerMapper {

    public AccountManager toEntity(AccountManagerRequest request) {
        return AccountManager.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .active(true)
                .build();
    }

    public AccountManagerResponse toResponse(AccountManager manager) {
        return AccountManagerResponse.builder()
                .id(manager.getId())
                .name(manager.getName())
                .email(manager.getEmail())
                .phone(manager.getPhone())
                .active(manager.isActive())
                .createdAt(manager.getCreatedAt())
                .build();
    }
}
