package it.grimage.accounttest.client.fabrick;

import retrofit2.Call;

/**
 * This class is just a retrofit interface mapping the calls toward the fabrick
 * service
 */
public interface FabrickExternalInterface {
    Call<FabrickResponse> getBalance();

    Call<FabrickResponse> getTransactions();

    Call<FabrickResponse> executeTransfer();
}
