package com.sb.test.rates.repository.server.models;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class RateDTO {
    @SerializedName("rates")
    private Map<String, Double> rates;

    public Map<String, Double> getRates() {
        return rates;
    }
}
