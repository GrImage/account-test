package it.grimage.accounttest.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Error class returned to the user when an unexpected conditions occurs
 */
@Getter
@RequiredArgsConstructor
@ToString
public class AccountError {
    private final String code;
    private final String description;

    public AccountError(String code) {
        this(code, null);
    }
}
