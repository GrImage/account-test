package it.grimage.accounttest.persistence.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This entity allows account transactions to be stored into a persistent storage.
 * While we could store directly the data received from the service, having a
 * different class allow greater flexibility in case of future develoments
 */
@Getter
@Setter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class AccountTransactionEntity {
    // Since ids are coming from the web service, we're not using any automatic generation strategy
    @Id
    private String transactionId;

    private String operationId;
    private LocalDate accountingDate;
    private LocalDate valueDate;
    private BigDecimal amount;
    private String currency;
    private String description;

    @Embedded
    private EnumeratedValueEmbedded type;

    /**
     * The time at which the transaction was stored. Since we do not update transactions
     * after storing, an update time is not needed
     */
    @CreatedDate
    private Instant storedOn;
}
