package it.grimage.accounttest.client.fabrick;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import lombok.Data;

/**
 * This class will map the response of a transfer amount from the fabrick web service
 */
@Data
public class FabrickTransferResponse {
    private String moneyTransferId;
    private String status;
    private String direction;
    private CreditorData creditor;
    private DebtorData debtor;
    private String cro;
    private String uri;
    private String trn;
    private String description;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime accountedDatetime;
    private LocalDate debtorValueDate;
    private LocalDate creditorValueDate;
    private AmountData amount;
    private Boolean isUrgent;
    private Boolean isInstant;
    private String feeType;
    private String feeAccountId;
    private List<FeeData> fees;
    private Boolean hasTaxRelief;

    @Data
    public class AccountData {
        private String accountCode;
        private String bicCode;
    }

    @Data
    public class AddressData {
        private String address;
        private String city;
        private String countryCode;
    }

    @Data
    public class CreditorData {
        private String name;
        private AccountData account;
        private AddressData address;
    }

    @Data
    public class DebtorData {
        private String name;
        private AccountData account;
    }

    @Data
    public class AmountData {
        private BigDecimal debtorAmount;
        private String debtorCurrency;
        private BigDecimal creditorAmount;
        private String creditorCurrency;
        private String creditorCurrencyDate;
        private BigDecimal exchangeRate;
    }

    @Data
    public class FeeData {
        private String feeCode;
        private String description;
        private BigDecimal amount;
        private String currency;
    }
}
