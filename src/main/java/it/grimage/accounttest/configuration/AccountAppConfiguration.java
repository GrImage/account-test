package it.grimage.accounttest.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ConfigurationProperties("account")
@Configuration
@Getter
@Setter
@ToString
public class AccountAppConfiguration {
    private long accountId;
}
