package codinpad.springboot.mapper;

import codinpad.springboot.dto.request.CustomerLeadRequest;
import codinpad.springboot.dto.response.CustomerLeadResponse;
import codinpad.springboot.entity.CustomerLead;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerLeadMapper {

    public CustomerLead toEntity(CustomerLeadRequest request) {
        return CustomerLead.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .comment(request.getComment())
                .build();
    }

    public CustomerLeadResponse toResponse(CustomerLead lead) {
        return CustomerLeadResponse.builder()
                .id(lead.getId())
                .name(lead.getName())
                .email(lead.getEmail())
                .phone(lead.getPhone())
                .comment(lead.getComment())
                .accountManagerId(
                        lead.getAccountManager() == null
                                ? null
                                : lead.getAccountManager().getId())
                .accountManagerName(
                        lead.getAccountManager() == null
                                ? null
                                : lead.getAccountManager().getName())
                .status(lead.getStatus())
                .closeReason(lead.getCloseReason())
                .createdAt(lead.getCreatedAt())
                .updatedAt(lead.getUpdatedAt())
                .build();
    }
}
