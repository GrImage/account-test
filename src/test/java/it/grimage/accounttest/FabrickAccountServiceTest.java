package it.grimage.accounttest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.google.common.collect.ImmutableList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.grimage.accounttest.client.fabrick.AccountBalance;
import it.grimage.accounttest.client.fabrick.AccountTransaction;
import it.grimage.accounttest.client.fabrick.FabrickClient;
import it.grimage.accounttest.client.fabrick.FabrickError;
import it.grimage.accounttest.client.fabrick.FabrickResponse;
import it.grimage.accounttest.client.fabrick.FabrickStatus;
import it.grimage.accounttest.client.fabrick.FabrickTransferResponse;
import it.grimage.accounttest.client.fabrick.AccountTransaction.AccountTransactionList;
import it.grimage.accounttest.controller.api.TransferRequest;
import it.grimage.accounttest.exception.AccountServiceException;
import it.grimage.accounttest.exception.FabrickException;
import it.grimage.accounttest.service.account.AccountServiceFabrick;

/**
 * Test of the fabrick implementation 
 */
@ExtendWith(SpringExtension.class)
public class FabrickAccountServiceTest {
    @MockBean private FabrickClient client;
    private AccountServiceFabrick service;

    @BeforeEach
    public void prepareService() {
        service = new AccountServiceFabrick(client);
    }

    private AccountTransaction transaction(String id, BigDecimal amount, LocalDate date) {
        AccountTransaction transaction = new AccountTransaction();
        transaction.setTransactionId(id);
        transaction.setAmount(amount);
        transaction.setCurrency("EUR");
        transaction.setAccountingDate(date);
        return transaction;
    }

    @Test
    public void getBalanceShouldForwardDataOnSuccess() throws AccountServiceException, IOException {
        AccountBalance givenBalance = new AccountBalance();
        givenBalance.setDate(LocalDate.now());
        givenBalance.setCurrency("EUR");
        givenBalance.setBalance(new BigDecimal("120.56"));
        givenBalance.setAvailableBalance(new BigDecimal("110.56"));

        FabrickResponse<AccountBalance> response = new FabrickResponse<>();
        response.setStatus(FabrickStatus.OK);
        response.setPayload(givenBalance);
        Mockito.when(client.getBalance()).thenReturn(response);

        AccountBalance gotBalance = service.getBalance();

        assertThat(gotBalance.getDate()).isEqualTo(givenBalance.getDate());
        assertThat(gotBalance.getCurrency()).isEqualTo(givenBalance.getCurrency());
        assertThat(gotBalance.getBalance()).isEqualTo(givenBalance.getBalance());
        assertThat(gotBalance.getAvailableBalance()).isEqualTo(givenBalance.getAvailableBalance());
    }

    @Test
    public void getTransactionsShouldForwardDataOnSuccess() throws AccountServiceException, IOException {
        AccountTransactionList givenList = new AccountTransactionList();
        givenList.setList(ImmutableList.of(
            transaction("id1", new BigDecimal("100.43"), LocalDate.of(2021, 5, 15)),
            transaction("id2", new BigDecimal("250.27"), LocalDate.of(2021, 5, 14))
        ));

        FabrickResponse<AccountTransactionList> response = new FabrickResponse<>();
        response.setStatus(FabrickStatus.OK);
        response.setPayload(givenList);
        Mockito.when(client.getTransactions(any(), any())).thenReturn(response);

        LocalDate start = LocalDate.now().minusDays(20);
        LocalDate end = LocalDate.now();
        List<AccountTransaction> transactions = service.getTransactions(start, end);

        assertThat(transactions).extracting("transactionId", "amount", "accountingDate")
            .containsExactly(
                tuple("id1", new BigDecimal("100.43"), LocalDate.of(2021, 5, 15)),
                tuple("id2", new BigDecimal("250.27"), LocalDate.of(2021, 5, 14)));
    }

    @Test
    public void transferRequestShouldReturnErrorOnFailure() throws IOException {
        FabrickResponse<FabrickTransferResponse> response = new FabrickResponse<>();
        response.setStatus(FabrickStatus.KO);
        response.setErrors(ImmutableList.of(new FabrickError()));
        Mockito.when(client.processTransfer(any())).thenThrow(new FabrickException("transfer", response));

        TransferRequest request = new TransferRequest();
        request.setReceiverName("Me");
        request.setReceiverAccountCode("IT60X0542811101000000123456");
        request.setAmount("145.6");
        request.setCurrency("EUR");
        request.setDescription("Pagamento");
        request.setExecutionDate(LocalDate.now());

        AccountServiceException ex = catchThrowableOfType(() -> service.makeTransfer(request), AccountServiceException.class);
        assertThat(ex).isNotNull().as("transfer request should have failed");
        assertThat(ex.getCode()).isEqualTo("API000");
   }
}
