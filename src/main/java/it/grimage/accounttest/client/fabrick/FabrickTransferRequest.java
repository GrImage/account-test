package it.grimage.accounttest.client.fabrick;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * This class maps a transfer request to fabrick web services
 */
@Data
public class FabrickTransferRequest {
    /*
     * With more time, all enumerated values here could either become enums
     * or have custom validators to check their value is within the allowed
     * bounds. For the purpose of the test, we know we won't be using them,
     * so it hasn't been done
     */

    @NotNull @Valid private CreditorData creditor;
    private LocalDate executionDate;
    private String uri;
    @NotBlank @Size(max = 140) private String description;
    @NotNull private BigDecimal amount;
    @NotBlank private String currency;
    private Boolean isUrgent;
    private Boolean isInstant;
    private String feeType;
    private String feeAccountId;
    private TaxReliefData taxRelief;

    @Data
    public static class CreditorData {
        @NotBlank private String name;
        @NotNull @Valid private AccountData account;
        private AddressData address;
    }

    @Data
    public static class AccountData {
        @NotBlank private String accountCode;
        private String bicCode;
    }

    @Data
    public static class AddressData {
        private String address;
        private String city;
        private String countryCode;
    }

    @Data
    public static class TaxReliefData {
        private String taxReliefId;
        @NotNull private Boolean isCondoUpgrade;
        @NotBlank private String creditorFiscalCode;
        @NotBlank private String beneficiaryType;
        private NaturalPersonBeneficiaryData naturalPersonBeneficiary;
    }

    @Data
    public static class NaturalPersonBeneficiaryData {
        @NotBlank private String fiscalCode1;
        private String fiscalCode2;
        private String fiscalCode3;
        private String fiscalCode4;
        private String fiscalCode5;
    }

    @Data
    public static class LegalPersonBeneficiaryData {
        @NotBlank private String fiscalCode;
        private String legalRepresentativeFiscalCode;
    }
}
