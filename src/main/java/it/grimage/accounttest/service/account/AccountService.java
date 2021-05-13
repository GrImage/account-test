package it.grimage.accounttest.service.account;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import it.grimage.accounttest.client.fabrick.AccountBalance;
import it.grimage.accounttest.controller.api.TransferRequest;

/**
 * The main account service, will take care of handling requests to the actual
 * backing (web) services
 */
@Validated
public interface AccountService {
    /**
     * Returns the balance of the application account
     */
    AccountBalance getBalance() throws IOException;

    /**
     * Returns the list of transactions between the given terms
     * @param from starting date, not <code>null</code>
     * @param to ending date, not <code>null</code>
     */
    List<String> getTransactions(@NotNull LocalDate from, @NotNull LocalDate to) throws IOException;

    /**
     * Performs a bank transfer using the data contained in the 
     * given request
     */
    void makeTransfer(@NotNull @Valid TransferRequest request) throws IOException;
}
