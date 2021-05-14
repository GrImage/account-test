package it.grimage.accounttest.client.fabrick;

import lombok.Data;

/**
 * Maps an enumerated value from fabrick web service
 */
@Data
public class EnumeratedValue {
    private String enumeration;
    private String value;
}
