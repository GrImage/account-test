package it.grimage.accounttest.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This class reads and exposes access informations for the fabricks service
 * from the configuration files
 */
// include file with api key, optional so that it can also be provided via start options
@PropertySource(ignoreResourceNotFound = true, value = "classpath:fabrickKey.properties")
@ConfigurationProperties("account.fabrick")
@Configuration
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
