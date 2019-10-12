package com.sb.test.rates;

public interface IRatePresenter {
    void onAttachView(IRateView view);
    void onDetachView();
    void startUpdateRate();
    void onItemRateClick(int position);
    void editFirstRateValue(double value);
}
