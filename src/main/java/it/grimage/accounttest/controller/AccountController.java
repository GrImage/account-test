package it.grimage.accounttest.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.grimage.accounttest.client.fabrick.AccountBalance;
import it.grimage.accounttest.client.fabrick.AccountTransaction;
import it.grimage.accounttest.controller.api.TransferRequest;
import it.grimage.accounttest.exception.AccountServiceException;
import it.grimage.accounttest.service.account.AccountService;

/**
 * Main controller for account-related operations
 */
@RestController
@RequiredArgsConstructor
@Validated
public class AccountController {
    private final AccountService service;

    @GetMapping(path="account/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountBalance getBalance() throws AccountServiceException {
        return service.getBalance();
    }

    @GetMapping(path="account/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AccountTransaction> getTransactions(
        @RequestParam("fromAccountingDate") @NotNull LocalDate fromDate,
        @RequestParam("toAccountingDate") @NotNull LocalDate toDate) throws AccountServiceException {
            return service.getTransactions(fromDate, toDate);
    }

    @PostMapping(
        path="account/transfer",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public void executeTransfer(@RequestBody @Valid TransferRequest request) throws AccountServiceException {
        service.makeTransfer(request);
    }
}
