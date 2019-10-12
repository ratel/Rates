package com.sb.test.rates.repository.server;

import com.sb.test.rates.repository.server.models.RateDTO;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RatesAPI {
    static public String RATES_API_URL="https://sbertest.glitch.me";
    String API_GETRATES= "/latest";

    @GET(API_GETRATES)
    Single<RateDTO> getRates(@Query("base") String base);
}
