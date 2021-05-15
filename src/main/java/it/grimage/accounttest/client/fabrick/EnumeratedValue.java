package it.grimage.accounttest.client.fabrick;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Maps an enumerated value from fabrick web service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnumeratedValue {
    private String enumeration;
    private String value;
}
