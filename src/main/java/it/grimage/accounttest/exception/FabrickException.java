package it.grimage.accounttest.exception;

import java.io.IOException;

import it.grimage.accounttest.client.fabrick.FabrickResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This exception is used to signal an error received from fabrick
 * web services, wrapping the received response
 */
@RequiredArgsConstructor
@Getter
public class FabrickException extends IOException {
    /**
     * An identifier of the call that was being performed, for logging purposes
     */
    private final String callId;

    /**
     * The full response of the fabrick ws, if available
     */
    private final FabrickResponse<?> response;
}
