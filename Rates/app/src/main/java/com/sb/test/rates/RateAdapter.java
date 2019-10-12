package com.sb.test.rates;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sb.test.rates.utils.TextFormatStandart;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

public class RateAdapter extends RecyclerView.Adapter<RatesViewHolder> {
    private List<RateItem> rates;
    private OnRateAdapterCallback onRateAdapterCallback;
    private double baseValue;
    private PublishSubject<String> valueData;
    private Disposable valueDataDisposable;
    private InputValueWatcher valueWatcher= new InputValueWatcher();
    private DecimalFormat valueFormat= TextFormatStandart.getMonetaryDecimalFormat();


    public RateAdapter(OnRateAdapterCallback onRateAdapterCallback) {
        this.onRateAdapterCallback = onRateAdapterCallback;

    }
    @Override
    public void onAttachedToRecyclerView(@NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        initValueData();
    }
    @Override
    public void onDetachedFromRecyclerView(@NotNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        valueDataDisposable.dispose();
        valueDataDisposable= null;
    }

    public void changeBaseValue(double baseValue) {
        this.baseValue= baseValue;
        if (getItemCount() > 0)
            notifyItemRangeChanged(1, getItemCount() - 1);
    }
    public void changeHeadItem(List<RateItem> rates) {
        RateDiffUtilCallback productDiffUtilCallback = new RateDiffUtilCallback(this.rates, rates);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);

        if (this.rates != null && this.rates.size() > 0 && rates != null && rates.size() > 0)
            baseValue= baseValue / this.rates.get(0).getCoefficient() * rates.get(0).getCoefficient();
        else
            baseValue=0;

        this.rates = rates;
        productDiffResult.dispatchUpdatesTo(this);
    }

    public void setRates(List<RateItem> rates) {
        if (rates != null && rates.size() > 0 && this.rates != null && this.rates.size() > 0
                && rates.get(0).getShortName().equals(this.rates.get(0).getShortName())) {
            this.rates = rates;
            notifyItemRangeChanged(1, getItemCount() - 1);
        }
        else
            changeHeadItem(rates);
    }

    @NonNull
    @Override
    public RatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RatesViewHolder.create(parent, onRateAdapterCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull RatesViewHolder holder, int position) {
        RateItem item= rates.get(position);
        holder.tvShortName.setText(item.getShortName());
        holder.tvName.setText(item.getName());
        boolean enableValue= position == 0;
        if (enableValue != holder.etValue.isEnabled()) {
            if (enableValue) {
                holder.etValue.setText(valueFormat.format(baseValue));
                holder.etValue.addTextChangedListener(valueWatcher);
            }
            else
                holder.etValue.removeTextChangedListener(valueWatcher);
            holder.etValue.setEnabled(enableValue);
        }
        if (position == 0) {
            holder.etValue.requestFocus();
        }
        else {
            holder.etValue.setText(valueFormat.format(item.translate(baseValue / this.rates.get(0).getCoefficient())));
        }
    }

    @Override
    public int getItemCount() {
        return rates != null ? rates.size() : 0;
    }

    private void initValueData() {
        if (valueDataDisposable != null) {
            valueDataDisposable.dispose();
            valueDataDisposable= null;
        }
        valueData= PublishSubject.create();
        valueDataDisposable= valueData.debounce(500, TimeUnit.MILLISECONDS)
                .map(s-> {
                    try {
                        return TextUtils.isEmpty(s) ? 0 : Double.parseDouble(s);
                    } catch (Exception e) {}
                    return -1.0;
                    })
                .filter(aDouble -> aDouble >= 0 && baseValue != aDouble)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(inputValue -> onRateAdapterCallback.editFirstValue(inputValue)
                        , throwable -> Log.e("E", "onError ", throwable));
    }

    class InputValueWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            valueData.onNext(editable.toString());
        }
    }
}
