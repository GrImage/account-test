package it.grimage.accounttest.client.fabrick;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

/**
 * Generic container for the fabrick responses
 */
@Data
public class FabrickResponse<T> {
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
     * Response payload. Generic since each response will return a different data
     * structure
     */
    private T payload;
}
