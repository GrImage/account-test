package it.grimage.accounttest.persistence.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.grimage.accounttest.persistence.model.AccountTransactionEntity;

/**
 * Basic repository for account transactions
 */
@Repository
public interface AccountTransactionRepository extends JpaRepository<AccountTransactionEntity, Long> {
    /**
     * Find the ids in the given id set that are already stored in the storage. Can
     * be used for quickly checking existence without the need to load the full
     * entities
     * @param transactionIds ids of the transaction to search
     * @return ids of the transactions that are already in the storage
     */
    Set<String> findTransactionIdByTransactionIdIn(Set<String> transactionIds);
}
