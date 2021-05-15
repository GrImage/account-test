package it.grimage.accounttest.client.fabrick;

import java.time.LocalDate;

import it.grimage.accounttest.client.fabrick.AccountTransaction.AccountTransactionList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * This class is just a retrofit interface mapping the calls toward the fabrick
 * service. Authentication data will be injected at call time via interceptors
 */
public interface FabrickExternalInterface {
    /**
     * The common base parts for all mapped api
     */
    String BASE_PATH = "api/gbs/banking/v4.0/accounts";

    @GET(BASE_PATH + "/{accountId}/balance")
    Call<FabrickResponse<AccountBalance>> getBalance(
        @Path("accountId") long accountId);

    @GET(BASE_PATH + "/{accountId}/transactions")
    Call<FabrickResponse<AccountTransactionList>> getTransactions(
        @Path("accountId") long accountId,
        @Query("fromAccountingDate") LocalDate from,
        @Query("toAccountingDate") LocalDate to);

    @POST(BASE_PATH + "/{accountId}/payments/money-transfers")
    // from the docs it appear this is the only accepted time zone, so it makes little sense parameterizing it
    @Headers(FabrickClient.HEADER_TIME_ZONE + ": Europe/Rome")
    Call<FabrickResponse<FabrickTransferResponse>> executeTransfer(
        @Path("accountId") long accountId,
        @Body FabrickTransferRequest request);
}
