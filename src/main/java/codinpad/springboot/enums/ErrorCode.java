package codinpad.springboot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    VALIDATION_ERROR("COMMON-001", "Request validation failed"),
    INVALID_REQUEST("COMMON-002", "Invalid request"),
    RESOURCE_NOT_FOUND("COMMON-003", "Requested resource was not found"),
    DUPLICATE_RESOURCE("COMMON-004", "Duplicate resource"),
    INTERNAL_SERVER_ERROR("COMMON-005", "An unexpected error occurred"),

    LEAD_NOT_FOUND("LEAD-001", "Customer lead was not found"),
    INVALID_LEAD_TRANSITION("LEAD-002", "Invalid customer lead status transition"),
    LEAD_ALREADY_CLOSED("LEAD-003", "Customer lead is already closed"),
    IDEMPOTENCY_CONFLICT("LEAD-004", "The idempotency key is already associated with another request"),

    ACCOUNT_MANAGER_NOT_FOUND("MANAGER-001", "Account manager was not found"),
    ACCOUNT_MANAGER_EMAIL_EXISTS("MANAGER-002", "Account manager email already exists"),
    ACCOUNT_MANAGER_INACTIVE("MANAGER-003", "Account manager is inactive");

    private final String code;
    private final String description;
}
