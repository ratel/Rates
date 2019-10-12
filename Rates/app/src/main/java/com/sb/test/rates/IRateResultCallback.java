package com.sb.test.rates;

import java.util.List;

public interface IRateResultCallback {
    void onRateReceive(List<RateItem> rateList);
    void onRateRecieveFailed(Throwable t);
}
