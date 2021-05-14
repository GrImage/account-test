package it.grimage.accounttest.client.fabrick;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

/**
 * Generic container for the fabrick responses
 */
@Data
public class FabrickResponse {
    /**
     * Status. The presence or absence of other fields will depend on this
     */
    private FabrickStatus status;

    /**
     * Received errors. Only populated when status is <code>KO</code>
     */
    private List<FabrickError> errors;

    /**
     * Response payload. Since we don't know what this will contain, we'll read
     * it as json node (faster) and convert it later to the specific type
     */
    private JsonNode payload;
}
