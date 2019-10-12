package com.sb.test.rates.repository.server;

import com.sb.test.rates.repository.server.models.RateDTO;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RatesService {
    private RatesAPI api;

    private RatesAPI getRatesApi() {
        if (api == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.level(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .addInterceptor(logging);
                    //.connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS);
            OkHttpClient client = builder.build();


            retrofit2.Retrofit mRetrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(RatesAPI.RATES_API_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            api = mRetrofit.create(RatesAPI.class);
        }
        return api;
    }

    public Single<RateDTO> getRates(String base) {
        return getRatesApi().getRates(base);
    }
}
