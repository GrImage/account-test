package it.grimage.accounttest.controller.api;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Data;

/**
 * This class will map a transaction request received from
 * the web interface
 */
@Data
public class TransferRequest {
    @NotBlank
    private String receiverName;

    @NotBlank
    private String description;

    @NotBlank
    private String currency;

    @Pattern(regexp = "\\d+")
    private String amount;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate executionDate; //YYYY-MM-DD
}
