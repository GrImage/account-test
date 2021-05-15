package it.grimage.accounttest.service.account;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import it.grimage.accounttest.client.fabrick.AccountTransaction;

/**
 * This class is used to convert and persist transactions received from the web service
 */
public interface TransactionPersister {
    /**
     * Stores in the persistent storage and transaction that has not been encountered yet
     */
    @Transactional
    void storeUnkwnownTransactions(Collection<AccountTransaction> transactions);
}
