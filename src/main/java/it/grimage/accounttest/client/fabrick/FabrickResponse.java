package it.grimage.accounttest.client.fabrick;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
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
    /*
     * documentation show this field as "errors", but actual testing has shown an
     * "error" field begin returned instead. I'm using an alias so that either would
     * be accepted
     */
    @JsonAlias("error")
    private List<FabrickError> errors;

    /**
     * Response payload. Since we don't know what this will contain, we'll read
     * it as json node (faster) and convert it later to the specific type
     */
    private JsonNode payload;
}
