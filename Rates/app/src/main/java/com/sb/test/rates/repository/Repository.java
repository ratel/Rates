package com.sb.test.rates.repository;

import com.sb.test.rates.IRateResultCallback;
import com.sb.test.rates.RateItem;
import com.sb.test.rates.repository.server.RatesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Repository {
    static final long REQUEST_INTERVAL= 2000L;
    static final String BASE_CURRENCY= "EUR";
    private RatesService ratesService;

    public Repository() {
        ratesService= new RatesService();
    }

    public Disposable updateRates(IRateResultCallback callback) {

        return ratesService.getRates(BASE_CURRENCY)
                .repeatWhen(handler -> handler.delay(REQUEST_INTERVAL, TimeUnit.MILLISECONDS))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(rateDTO -> {
                    List<RateItem> rateItems= new ArrayList<>();
                    for (Map.Entry<String, Double> e : rateDTO.getRates().entrySet()) {
                        rateItems.add(new RateItem("currency name", e.getKey(), e.getValue()));
                    }
                    return rateItems;
                })
                .retryWhen(errors -> {
                    return errors.map(error -> {
                        callback.onRateRecieveFailed(error);
                        return error;
                    })
                            .delay(REQUEST_INTERVAL, TimeUnit.MILLISECONDS);
                })
                .subscribe(callback::onRateReceive, callback::onRateRecieveFailed);
    }
}
