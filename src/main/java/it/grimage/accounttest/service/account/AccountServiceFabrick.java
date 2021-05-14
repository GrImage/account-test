package it.grimage.accounttest.service.account;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import it.grimage.accounttest.client.fabrick.AccountBalance;
import it.grimage.accounttest.client.fabrick.AccountTransaction;
import it.grimage.accounttest.client.fabrick.FabrickClient;
import it.grimage.accounttest.client.fabrick.FabrickResponse;
import it.grimage.accounttest.client.fabrick.FabrickStatus;
import it.grimage.accounttest.client.fabrick.FabrickTransferRequest;
import it.grimage.accounttest.client.fabrick.AccountTransaction.AccountTransactionList;
import it.grimage.accounttest.controller.api.TransferRequest;
import it.grimage.accounttest.exception.FabrickException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Default implementation of the {@link AccountService} service, backed
 * by the fabrick services</p>
 * @see https://developers.fabrick.com</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceFabrick implements AccountService {
    private final ObjectMapper mapper;
    private final FabrickClient client;

    @Override
    public AccountBalance getBalance() throws IOException {
        FabrickResponse response = client.getBalance();
        return readPayloadAs(response, AccountBalance.class, "getBalance");
    }

    @Override
    public List<AccountTransaction> getTransactions(@NotNull LocalDate from, @NotNull LocalDate to) throws IOException {
        FabrickResponse response = client.getTransactions(from, to);
        AccountTransactionList list = readPayloadAs(response, AccountTransactionList.class, "getTransactions");
        return list.getList();
    }

    @Override
    public Object makeTransfer(@NotNull @Valid TransferRequest request) throws IOException {
        FabrickResponse response = client.processTransfer(convertRequest(request));
        return readPayloadAs(response, Object.class, "makeTransfer");
    }

    /**
     * Reads the input request from the web service and convert it to the fabrick ws
     * request
     * @param raw raw request coming from our web service
     * @return converted request
     */
    private FabrickTransferRequest convertRequest(TransferRequest raw) {
        FabrickTransferRequest request = new FabrickTransferRequest();
        // since we validated the request, we know this is a valid number
        request.setAmount(new BigDecimal(raw.getAmount()));
        request.setCurrency(raw.getCurrency());

        return request;
    }

    /**
     * This method will examine the fabrick response. If the response status is not OK, an
     * exception is thrown, otherwise it will try to extract the payload and convert it
     * to the required type. If the payload is not found, <code>null</code> is returned,
     * if it cannot be converted, an exception is thrown.
     * @param <T> the required type for the converted payload value
     * @param response the raw response
     * @param targetClass the class of <code>T</code>, for conversion purposes
     * @param callId identifier of the requesting call, for logging purposes
     * @return
     * @throws FabrickException
     */
    @Nullable
    protected <T> T readPayloadAs(@NonNull FabrickResponse response, @NonNull Class<T> targetClass, @NonNull String callId) throws IOException {
        FabrickStatus fabrickStatus = response.getStatus();
        if (fabrickStatus == null) {
            // this shouldn't happen, but let's handle it just in case
            throw new FabrickException(callId, null);
        }

        switch(fabrickStatus) {
            case KO:
                // these should be caught earlier, but in case they get here, throw them
                throw new FabrickException(callId, response);
            case PENDING:
                // we have no indication on how to process a pending call, for now just throw an exception
                throw new FabrickException(callId, response);
            case OK:
                Object payloadContent = response.getPayload();
                if (payloadContent != null) {
                    return mapper.convertValue(payloadContent, targetClass);
                } else {
                    return null;
                }
            case UNKNOWN:
            default: // to satisfy static analysis, we know there is no other value
                // this should never happen unless new enum values are added without handling them here
                log.error("Received unknown status {} while calling {}", fabrickStatus, callId);
                // throw an unchecked exception
                throw new IllegalArgumentException("Unknown status");
        }
    }
}
