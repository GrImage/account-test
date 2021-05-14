package it.grimage.accounttest.client.fabrick;

import java.io.IOException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Okhttp Interceptor implementation used to inject authentication
 * data into fabrick requests. Only supports S2S authentication via
 * api key in its current state
 */
@RequiredArgsConstructor
public class FabrickAuthenticatorInterceptor implements Interceptor{
    @NonNull private final String apiKey;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request authenticatedRequest = originalRequest.newBuilder()
            .addHeader("Auth-Schema", "S2S")
            .addHeader("Api-Key", apiKey)
            .build();
        return chain.proceed(authenticatedRequest);
    }
}
