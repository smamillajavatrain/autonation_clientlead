package codinpad.springboot.exception;

import codinpad.springboot.enums.ErrorCode;
import lombok.Getter;

@Getter
public class CrmBusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public CrmBusinessException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public CrmBusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
