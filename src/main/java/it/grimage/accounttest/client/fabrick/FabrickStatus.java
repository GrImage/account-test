package it.grimage.accounttest.client.fabrick;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * This enumerated class describes the possible outcomes of a fabrick call
 */
public enum FabrickStatus {
    /**
     * Successful response, payload will contain the data, no errors should be
     * present
     */
    OK,

    /**
     * Unsuccessful response, payload should be empty and the errors list will
     * contain details about the exact problems that occured
     */
    KO,

    /**
     * The request requires further interaction (documentation does not state if
     * payload or error will be populated, but it is reasonable to assume they
     * won't)
     */
    PENDING,

    /**
     * This value is not actually returned by the web service, but is used as a
     * catch-all fallback in case of new values so that the internal components
     * can still handle it gracefully (and send a meaningful error)
     */
    @JsonEnumDefaultValue
    UNKNOWN,
    ;
}
