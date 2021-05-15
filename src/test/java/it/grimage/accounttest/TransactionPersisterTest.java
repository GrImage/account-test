package it.grimage.accounttest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.grimage.accounttest.client.fabrick.AccountTransaction;
import it.grimage.accounttest.configuration.ModelMapperConfiguration;
import it.grimage.accounttest.persistence.model.AccountTransactionEntity;
import it.grimage.accounttest.persistence.repository.AccountTransactionRepository;
import it.grimage.accounttest.service.account.TransactionPersisterImpl;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@Import(ModelMapperConfiguration.class)
public class TransactionPersisterTest {
    @MockBean AccountTransactionRepository repository;
    @Autowired ModelMapper mapper;

    @Captor ArgumentCaptor<Iterable<AccountTransactionEntity>> transactionsCaptor;
    
    private TransactionPersisterImpl persister;

    private AccountTransaction transaction(String id, BigDecimal amount, LocalDate date) {
        AccountTransaction transaction = new AccountTransaction();
        transaction.setTransactionId(id);
        transaction.setAmount(amount);
        transaction.setCurrency("EUR");
        transaction.setAccountingDate(date);
        return transaction;
    }

    @BeforeEach
    public void preparePersister() {
        persister = new TransactionPersisterImpl(mapper, repository);
    }

    @Test
    public void unknownTransactionsShouldBePersisted() {
        when(repository.findTransactionIdByTransactionIdIn(anySet()))
            .thenReturn(ImmutableSet.of("id1"));

        persister.storeUnkwnownTransactions(ImmutableList.of(
            transaction("id1", new BigDecimal("100"), LocalDate.now()),
            transaction("id2", new BigDecimal("200"), LocalDate.now())
        ));

        verify(repository).saveAll(transactionsCaptor.capture());

        Iterable<AccountTransactionEntity> entities = transactionsCaptor.getValue();

        assertThat(entities).extracting("transactionId").contains("id2");
    }

    public void knownTransactionsShouldNotBePersisted() {
        when(repository.findTransactionIdByTransactionIdIn(anySet()))
            .thenReturn(ImmutableSet.of("id1"));

        persister.storeUnkwnownTransactions(ImmutableList.of(
            transaction("id1", new BigDecimal("100"), LocalDate.now()),
            transaction("id2", new BigDecimal("200"), LocalDate.now())
        ));

        verify(repository).saveAll(transactionsCaptor.capture());

        Iterable<AccountTransactionEntity> entities = transactionsCaptor.getValue();

        assertThat(entities).extracting("transactionId").doesNotContain("id1");
    }
}
