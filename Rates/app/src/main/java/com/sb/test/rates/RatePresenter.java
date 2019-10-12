package com.sb.test.rates;

import android.util.Log;

import com.sb.test.rates.repository.Repository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class RatePresenter implements IRatePresenter, IRateResultCallback {
    private Repository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private IRateView view;
    private List<RateItem> rates;
    private double baseValue;

    public RatePresenter() {
        this.repository = new Repository();
    }

    @Override
    public void onAttachView(IRateView view) {
        this.view= view;
    }

    @Override
    public void onDetachView() {
        disposables.clear();
        view= null;
    }

    @Override
    public void startUpdateRate() {
        disposables.add(repository.updateRates(this));
    }

    @Override
    public void onItemRateClick(int position) {
        if (rates != null && rates.size() > position && position > 0) {
            rates.add(0, rates.remove(position));
            if (view != null) {
                view.onChangeHeaderItem(copyRateList(rates));
            }
        }
    }

    @Override
    public void editFirstRateValue(double value) {
        if (rates != null) {
            baseValue = value;
            if (view != null)
                view.setNewBaseValue(baseValue);
        }
    }

    @Override
    public void onRateReceive(List<RateItem> rateList) {
        rates= applyWithSaveOrder(rateList);
        if (view != null)
            view.onUpdateRate(copyRateList(rates));
    }

    @Override
    public void onRateRecieveFailed(Throwable t) {
        if (view != null)
            view.onUpdateRateFailed(R.string.error_rate_request_failed);
    }

    private List<RateItem> copyRateList(List<RateItem> rateList) {
        List<RateItem> result= null;
        if (rateList != null) {
            result= new ArrayList<>(rateList.size());
            for (RateItem r : rateList)
                result.add(new RateItem(r));
        }
        return result;
    }
    private List<RateItem> applyWithSaveOrder(List<RateItem> rateList) {
        if (rateList == null)
            return null;

        List<RateItem> result;
        result= new ArrayList<>(rateList.size());
        if (rates != null) {
            for (RateItem r : rates) {
                RateItem newItem= popRate(rateList, r.getShortName());
                if (newItem != null)
                    result.add(newItem);
            }
        }
        result.addAll(rateList);

        return result;
    }
    private RateItem popRate(List<RateItem> rateList, String shortName) {
        if (rateList != null)
            for (int i = 0; i < rateList.size(); i++) {
                if (shortName.equals(rateList.get(i).getShortName()))
                    return rateList.remove(i);
            }
        return null;
    }
}
