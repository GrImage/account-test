package it.grimage.accounttest.service.account;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import it.grimage.accounttest.client.fabrick.AccountBalance;
import it.grimage.accounttest.client.fabrick.AccountTransaction;
import it.grimage.accounttest.client.fabrick.FabrickClient;
import it.grimage.accounttest.client.fabrick.FabrickResponse;
import it.grimage.accounttest.client.fabrick.FabrickStatus;
import it.grimage.accounttest.client.fabrick.FabrickTransferRequest;
import it.grimage.accounttest.client.fabrick.FabrickTransferResponse;
import it.grimage.accounttest.client.fabrick.AccountTransaction.AccountTransactionList;
import it.grimage.accounttest.client.fabrick.FabrickTransferRequest.AccountData;
import it.grimage.accounttest.client.fabrick.FabrickTransferRequest.CreditorData;
import it.grimage.accounttest.controller.api.TransferRequest;
import it.grimage.accounttest.exception.AccountServiceException;
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
    private final FabrickClient client;
    private final TransactionPersister persister;

    @Override
    public AccountBalance getBalance() throws AccountServiceException {
        try {
            FabrickResponse<AccountBalance> response = client.getBalance();
            return getPayload(response, "getBalance");
        } catch (FabrickException e) {
            log.error("Received fabrick error {} on {}", e.getResponse(), e.getCallId());
            throw AccountServiceException.generic(e);
        } catch (IOException e) {
            throw AccountServiceException.generic(e);
        }
    }

    @Transactional
    @Override
    public List<AccountTransaction> getTransactions(@NotNull LocalDate from, @NotNull LocalDate to) throws AccountServiceException {
        try {
            FabrickResponse<AccountTransactionList> response = client.getTransactions(from, to);
            AccountTransactionList list = getPayload(response, "getTransactions");
            List<AccountTransaction> transactions = list.getList();
            persister.storeUnkwnownTransactions(transactions);
            return transactions;
        } catch (FabrickException e) {
            log.error("Received fabrick error {} on {}", e.getResponse(), e.getCallId());
            throw AccountServiceException.generic(e);
        } catch (IOException e) {
            throw AccountServiceException.generic(e);
        }
    }

    @Override
    public FabrickTransferResponse makeTransfer(@NotNull @Valid TransferRequest request) throws AccountServiceException {
        try {
            FabrickResponse<FabrickTransferResponse> response = client.processTransfer(convertRequest(request));
            return getPayload(response, "makeTransfer");
        } catch (FabrickException e) {
            // return the desired error code
            log.error("Received fabrick error {} on {}", e.getResponse(), e.getCallId());
            throw new AccountServiceException("API000", "Errore tecnico  La condizione BP049 non e' prevista per il conto id 14537780", HttpStatus.BAD_REQUEST, e);
        } catch (IOException e) {
            throw AccountServiceException.generic(e);
        }
    }

    /**
     * Reads the input request from the web service and convert it to the fabrick ws
     * request
     * @param raw raw request coming from our web service
     * @return converted request
     */
    private FabrickTransferRequest convertRequest(TransferRequest raw) {
        FabrickTransferRequest request = FabrickTransferRequest.builder()
            // since we validated the request, we know this is a valid number
            .amount(new BigDecimal(raw.getAmount()))
            .currency(raw.getCurrency())
            .executionDate(raw.getExecutionDate())
            .description(raw.getDescription())
            .creditor(CreditorData.builder()
                .name(raw.getReceiverName())
                .account(AccountData.builder()
                    .accountCode(raw.getReceiverAccountCode())
                    .build())
                .build())
            .build();
        log.debug("Converted {} to {}", raw, request);
        return request;
    }

    /**
     * This method will examine the fabrick response. If the response status is not OK, an
     * exception is thrown, otherwise it will try to extract the payload and convert it
     * to the required type. If the payload is not found, <code>null</code> is returned,
     * if it cannot be converted, an exception is thrown.
     * @param <T> the required type for the converted payload value
     * @param response the raw response
     * @param callId identifier of the requesting call, for logging purposes
     * @return
     * @throws FabrickException
     */
    @Nullable
    protected <T> T getPayload(@NonNull FabrickResponse<T> response, @NonNull String callId) throws AccountServiceException, FabrickException {
        FabrickStatus fabrickStatus = response.getStatus();
        if (fabrickStatus == null) {
            // this shouldn't happen, but let's handle it just in case
            log.error("Received empty status calling {}", callId);
            throw new FabrickException(callId, response);
        }

        switch(fabrickStatus) {
            case KO:
                // these should be caught earlier, but in case they get here, throw them
                throw new FabrickException(callId, response);
            case PENDING:
                // we have no indication on how to process a pending call, for now just throw an exception
                throw new FabrickException(callId, response);
            case OK:
                return response.getPayload();
            case UNKNOWN:
            default: // to satisfy static analysis, we know there is no other value
                // this should never happen unless new enum values are added without handling them here
                log.error("Received unknown status {} while calling {}", fabrickStatus, callId);
                // throw an unchecked exception
                throw new IllegalArgumentException("Unknown status");
        }
    }
}
