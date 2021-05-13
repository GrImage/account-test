package it.grimage.accounttest.controller.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * This class is used to return informations about processing
 * errors
 */
@Data
@RequiredArgsConstructor(staticName = "of")
public class ErrorResponse {
    private final String code;
    private final String description;
}
