package it.grimage.accounttest.service.account;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.grimage.accounttest.client.fabrick.AccountTransaction;
import it.grimage.accounttest.persistence.model.AccountTransactionEntity;
import it.grimage.accounttest.persistence.repository.AccountTransactionRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransactionPersisterImpl implements TransactionPersister {
    private final ModelMapper mapper;
    private final AccountTransactionRepository repository;

    @Override
    @Transactional
    public void storeUnkwnownTransactions(Collection<AccountTransaction> transactions) {
        /*
         * we're going to assume transactions do not change after being transmitted, so
         * we'll only store new ones in the storage
         */
        // first find the full ids of the transactions
        Set<String> transactionIds = transactions.stream()
            .map(AccountTransaction::getTransactionId)
            .collect(Collectors.toSet());

        // now discard transactions that are already in the storage
        Set<String> foundIds = repository.findTransactionIdByTransactionIdIn(transactionIds);

        // now extract and convert all the missing ones
        Set<AccountTransactionEntity> missingTransactions = transactions.stream()
            .filter(tr -> !foundIds.contains(tr.getTransactionId()))
            .map(tr -> mapper.map(tr, AccountTransactionEntity.class))
            .collect(Collectors.toSet());

        // and store them
        repository.saveAll(missingTransactions);
    }
}
