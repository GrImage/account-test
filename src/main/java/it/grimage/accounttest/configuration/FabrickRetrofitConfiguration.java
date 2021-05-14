package it.grimage.accounttest.configuration;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import it.grimage.accounttest.client.fabrick.FabrickAuthenticatorInterceptor;
import it.grimage.accounttest.client.fabrick.FabrickExternalInterface;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Configures and exposes the retrofit instance used to contact the fabrick service.
 * If multiple retrofit clients were to be used by the application, the factory methods
 * here would need to be annotated further with names or custom annotations to distinguish
 * them from one another
 */
@Configuration
@RequiredArgsConstructor
public class FabrickRetrofitConfiguration {
    private final FabrickDataConfiguration config;

    @Bean
    @Scope(SCOPE_SINGLETON)
    public FabrickExternalInterface getFabrickExternalInterface(Retrofit retrofit) {
        return retrofit.create(FabrickExternalInterface.class);
    }

    @Bean
    @Scope(SCOPE_SINGLETON)
    public Retrofit getFabrickRetrofit() {
        return new Retrofit.Builder()
            .baseUrl(getFabrickServiceBaseUrl())
            .addConverterFactory(JacksonConverterFactory.create(
                getFabrickObjectMapper()))
            .client(getFabrickOkhttpClient())
            .build();
    }

    public ObjectMapper getFabrickObjectMapper() {
        return new ObjectMapper()
            .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
            ;
    }

    @Bean
    @Scope(SCOPE_SINGLETON)
    public OkHttpClient getFabrickOkhttpClient() {
        return new OkHttpClient.Builder()
            .addInterceptor(getFabrickAuthenticatorInterceptor())
            .build();
    }

    @Bean
    @Scope(SCOPE_SINGLETON)
    public FabrickAuthenticatorInterceptor getFabrickAuthenticatorInterceptor() {
        return new FabrickAuthenticatorInterceptor(getFabrickApiKey());
    }

    private String getFabrickApiKey() {
        return config.getApiKey();
    }

    private String getFabrickServiceBaseUrl() {
        return config.getUrl();
    }
}
