package codinpad.springboot.exception;

import codinpad.springboot.dto.response.ApiErrorResponse;
import codinpad.springboot.enums.ErrorCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class RestApiGlobalExceptionHandler {

    @ExceptionHandler(CrmBusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(
            CrmBusinessException ex,
            HttpServletRequest request) {

        HttpStatus status = mapStatus(ex.getErrorCode());

        return ResponseEntity.status(status).body(
                ApiErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .errorCode(ex.getErrorCode().getCode())
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> fieldErrors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        fieldErrors.put(
                                error.getField(),
                                error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(
                ApiErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .errorCode(ErrorCode.VALIDATION_ERROR.getCode())
                        .message(ErrorCode.VALIDATION_ERROR.getDescription())
                        .path(request.getRequestURI())
                        .fieldErrors(fieldErrors)
                        .build()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .errorCode(ErrorCode.DUPLICATE_RESOURCE.getCode())
                        .message("A database constraint was violated. The resource may already exist.")
                        .path(request.getRequestURI())
                        .build()
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            NoHandlerFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .errorCode(ErrorCode.RESOURCE_NOT_FOUND.getCode())
                        .message(ErrorCode.RESOURCE_NOT_FOUND.getDescription())
                        .path(request.getRequestURI())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(
            Exception ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .errorCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                        .message(ErrorCode.INTERNAL_SERVER_ERROR.getDescription())
                        .path(request.getRequestURI())
                        .build()
        );
    }

    private HttpStatus mapStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case LEAD_NOT_FOUND, ACCOUNT_MANAGER_NOT_FOUND, RESOURCE_NOT_FOUND ->
                    HttpStatus.NOT_FOUND;
            case INVALID_LEAD_TRANSITION, LEAD_ALREADY_CLOSED, INVALID_REQUEST,
                 ACCOUNT_MANAGER_INACTIVE ->
                    HttpStatus.BAD_REQUEST;
            case DUPLICATE_RESOURCE, IDEMPOTENCY_CONFLICT,
                 ACCOUNT_MANAGER_EMAIL_EXISTS ->
                    HttpStatus.CONFLICT;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
