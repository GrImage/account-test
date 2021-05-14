package it.grimage.accounttest.client.fabrick;

import java.io.IOException;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;

/**
 * This class is a middle tier component standing between the service and the
 * actual external client. Its purpose is to interpret the calls in order to
 * handle failed responses, and to provide a mockable interface for tests.
 */
@Component
@Slf4j
public class FabrickClient {
    private final FabrickExternalInterface ext;

    add javadoc
    private <T> T getPayloadOrThrow(
        Response<FabrickResponse> response,
        Class<T> targetClass,
        String callId) throws IOException {
        log.debug("Examining response for {}", callId);
        if (!response.isSuccessful()) {
            throw something;
        }

        // Nope - this part belong to the service layer
        FabrickResponse fabrickResponse = response.body();
        FabrickStatus fabrickStatus = fabrickResponse.getStatus();
        if (fabrickStatus == null) {
            // this shouldn't happen, but let's handle it just in case
            throw something else
        }

        switch(fabrickStatus) {
            case KO:
                read error and throw
                break;
            case PENDING:
                just throw
                break;
            case OK:
                break;
            default:
                // this should never happen unless new enum values are added without handling them here
                log.error("Received unknown status {} while calling {}", fabrickStatus, callId);
                throw new IllegalArgumentException("Unknown status");
                break;
        }
    }

    public AccountBalance getBalance() throws IOException {
        Response<FabrickResponse> response = ext.getBalance().execute();
    }
}
