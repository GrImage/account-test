package it.grimage.accounttest.client.fabrick;

import java.io.IOException;
import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import it.grimage.accounttest.configuration.AccountAppConfiguration;
import it.grimage.accounttest.exception.FabrickException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Response;

/**
 * This class is a middle tier component standing between the service and the
 * actual external client. Its purpose is to interpret the calls in order to
 * handle failed responses, and to provide a mockable interface for tests.
 */
@Component
@Validated
@RequiredArgsConstructor
@Slf4j
public class FabrickClient {
    /**
     * This header holds time zone information, both when sending and receiving
     * data. Standard java ZoneId format
     */
    static final String HEADER_TIME_ZONE = "X-Time-Zone";

    private final FabrickExternalInterface ext;
    private final AccountAppConfiguration appConfiguration;
    private final ObjectMapper mapper;

    /**
     * This method wraps common checks on received response at network level
     * (i.e. failed calls) by loggin failure data and wrapping them in more
     * specific exceptions
     * @param response a retrofit response
     * @param callId a descriptor of the call point, used in logs to identify the caller
     * @return the extracted response, if no errors are found
     * @throws IOException 
     */
    @NonNull
    private FabrickResponse getResponseOrThrow(@NonNull Response<FabrickResponse> response, @NonNull String callId) throws IOException {
        log.debug("Examining response for {}", callId);
        if (!response.isSuccessful()) {
            // parse the body anyway and wrap it in an exception
            FabrickResponse errorBody = mapper.readValue(
                response.errorBody().charStream(),
                FabrickResponse.class);
            throw new FabrickException(callId, errorBody);
        }
        return response.body();
    }

    /**
     * Fetch the account id used for web service calls from configuration
     * @return the account id to use
     */
    private long getAccountId() {
        return appConfiguration.getAccountId();
    }

    public FabrickResponse getBalance() throws IOException {
        Response<FabrickResponse> response = ext.getBalance(getAccountId()).execute();
        return getResponseOrThrow(response, "getBalance");
    }

    public FabrickResponse getTransactions(@NotNull LocalDate from, @NotNull LocalDate to) throws IOException {
        Response<FabrickResponse> response = ext.getTransactions(getAccountId(), from, to).execute();
        return getResponseOrThrow(response, "getTransactions");
    }

    public FabrickResponse processTransfer(@NotNull @Valid FabrickTransferRequest request) throws IOException {
        Response<FabrickResponse> response = ext.executeTransfer(getAccountId(), request).execute();
        return getResponseOrThrow(response, "processTransfer");
    }
}
