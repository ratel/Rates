package com.sb.test.rates;

import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatesViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.ll_root)
    ViewGroup llRoot;
    @BindView(R.id.tv_short_name)
    TextView tvShortName;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.et_value)
    AppCompatEditText etValue;

    public static RatesViewHolder create(@NonNull ViewGroup parent, OnRateAdapterCallback onRateAdapterCallback) {
        RatesViewHolder holder= new RatesViewHolder(
                LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rate, parent, false));
        holder.llRoot.setOnClickListener(v -> onRateAdapterCallback.onItemClick(holder.getAdapterPosition()));
        return holder;
    }

    public RatesViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
