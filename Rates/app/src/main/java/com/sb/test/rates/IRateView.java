package com.sb.test.rates;

import java.util.List;

public interface IRateView {
    void onUpdateRate(List<RateItem> rates);
    void onUpdateRateFailed(int errorMessageRes);
    void onUpdateRateFailed(String errorMessage);
    void onChangeHeaderItem(List<RateItem> rates);
    void setNewBaseValue(double value);
}
