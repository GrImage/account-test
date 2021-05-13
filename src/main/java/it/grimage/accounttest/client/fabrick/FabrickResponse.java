package it.grimage.accounttest.client.fabrick;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * Generic container for the fabrick responses
 */
@Data
public class FabrickResponse {
    private FabrickStatus status;
    private List<FabrickError> errors;
    private Map<String, Object> payload;
}
