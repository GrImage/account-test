package it.grimage.accounttest.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This class reads and exposes access informations for the fabricks service
 * from the configuration files
 */
@ConfigurationProperties("account.fabrick")
@Component
@Getter
@Setter
@ToString
public class FabrickDataConfiguration {
    /**
     * The api key used to access the fabrick service
     */
    private String apiKey;

    /**
     * The base url of the fabrick service
     */
    private String url;
}
