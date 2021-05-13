package it.grimage.accounttest.client.fabrick;

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
    ;
}
