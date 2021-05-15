package it.grimage.accounttest.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountServiceException extends Exception {
    private final String code;
    private final String description;
    private final HttpStatus httpStatus;

    public AccountServiceException(String code, String description, HttpStatus status, Throwable cause) {
        super(cause);

        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }

    /**
     * Returns a generic error for unexpected conditions suitable for
     * unknown situations
     * @return
     */
    public static AccountServiceException generic() {
        return new AccountServiceException("GEN", "Server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Returns a generic error for unexpected conditions suitable for
     * unknown situations
     * @param cause original exception that caused the failure
     * @return
     */
    public static AccountServiceException generic(Throwable cause) {
        return new AccountServiceException("GEN", "Server error", HttpStatus.INTERNAL_SERVER_ERROR, cause);
    }

    /**
     * Get the error code and description as an object, for wrapping in
     * the return payload of a failed response
     * @return
     */
    public AccountError getError() {
        return new AccountError(code, description);
    }
}
