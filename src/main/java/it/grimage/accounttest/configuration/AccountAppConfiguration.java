package it.grimage.accounttest.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ConfigurationProperties("account")
@Component
@Getter
@Setter
@ToString
public class AccountAppConfiguration {
    private long accountId;
}
