package it.grimage.accounttest.client.fabrick;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

/**
 * This class maps a fabrick transaction from the accounts api
 */
@Data
public class AccountTransaction {
    private String transactionId;
    private String operationId;
    private LocalDate accountingDate;
    private LocalDate valueDate;
    private EnumeratedValue type;
    private BigDecimal amount;
    private String currency;
    private String description;

    /**
     * This is simply a wrapper class to read the transactions payload,
     * and provide generics type info
     */
    @Data
    public static class AccountTransactionList {
        private List<AccountTransaction> list;
    }
}
