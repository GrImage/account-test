package it.grimage.accounttest.service.account;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import it.grimage.accounttest.client.fabrick.AccountBalance;
import it.grimage.accounttest.client.fabrick.FabrickClient;
import it.grimage.accounttest.controller.api.TransferRequest;
import lombok.RequiredArgsConstructor;

/**
 * <p>Default implementation of the {@link AccountService} service, backed
 * by the fabrick services</p>
 * @see https://developers.fabrick.com</p>
 */
@RequiredArgsConstructor
public class AccountServiceFabrick implements AccountService {
    private final FabrickClient client;

    @Override
    public AccountBalance getBalance() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<String> getTransactions(@NotNull LocalDate from, @NotNull LocalDate to) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void makeTransfer(@NotNull @Valid TransferRequest request) {
        // TODO Auto-generated method stub
        
    }
    
}
