package it.grimage.accounttest.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ConfigurationProperties("account")
@Getter
@Setter
@ToString
public class AccountAppConfiguration {
    private long accountId;
}
