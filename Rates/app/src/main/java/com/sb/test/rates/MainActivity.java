package com.sb.test.rates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IRateView, OnRateAdapterCallback {
    @BindView(R.id.cl_root)
    ConstraintLayout clRoot;
    @BindView(R.id.rates_list)
    RecyclerView rateListView;
    private RateAdapter adapter;
    IRatePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        adapter= new RateAdapter(this);
        rateListView.setLayoutManager(new LinearLayoutManager(this));
        rateListView.setAdapter(adapter);
        presenter= new RatePresenter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onAttachView(this);
        presenter.startUpdateRate();
    }
    @Override
    protected void onStop() {
        presenter.onDetachView();
        super.onStop();
    }

    @Override
    public void onUpdateRate(List<RateItem> rates) {
        adapter.setRates(rates);
    }

    @Override
    public void onUpdateRateFailed(int errorMessageRes) {
        showErrorSnack(getString(errorMessageRes));
    }

    @Override
    public void onUpdateRateFailed(String errorMessage) {
        showErrorSnack(errorMessage);
    }

    @Override
    public void onChangeHeaderItem(List<RateItem> rates) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rateListView.getLayoutManager();
        adapter.changeHeadItem(rates);
        if (layoutManager != null)
            layoutManager.scrollToPositionWithOffset(0, 0);
    }

    @Override
    public void setNewBaseValue(double value) {
        adapter.changeBaseValue(value);
    }

    @Override
    public void onItemClick(int position) {
        presenter.onItemRateClick(position);
    }

    @Override
    public void editFirstValue(double value) {
        presenter.editFirstRateValue(value);
    }

    public void showErrorSnack(final String error) {
        Snackbar snackbar = Snackbar
                .make(clRoot, error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.error_dismiss,view ->{});

        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
        snackbar.setActionTextColor(getResources().getColor(android.R.color.black));

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(android.R.color.white));
        textView.setMaxLines(10);
        snackbar.show();
    }
}
