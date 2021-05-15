package it.grimage.accounttest.persistence.model;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Persistent value class for the persistence of service
 * {@link it.grimage.accounttest.client.fabrick.EnumeratedValue}
 * instances
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EnumeratedValueEmbedded {
    private String enumeration;
    private String value;
}
