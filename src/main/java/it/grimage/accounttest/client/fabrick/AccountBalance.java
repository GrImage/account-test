package it.grimage.accounttest.client.fabrick;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Data;

/**
 * This class will map the response of a fabrick account balance
 */
@Data
public class AccountBalance {
    /**
     * Reference date to which the balance refers to
     */
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    /**
     * Account balance at the reference date, without including reserved sums
     */
    private BigDecimal balance;

    /**
     * Accounte balance at the reference date, including reserved sums
     */
    private BigDecimal availableBalance;

    /**
     * Currency the balance is expressed with
     */
    private String currency;
}
