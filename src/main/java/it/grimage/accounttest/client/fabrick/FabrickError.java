package it.grimage.accounttest.client.fabrick;

import lombok.Data;

/**
 * This class maps the structure of an error received from the fabricks web
 * service
 */
@Data
public class FabrickError {
    private String code;
    private String description;
    private String params;
}
