package com.sb.test.rates.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class TextFormatStandart {
    static public DecimalFormat getMonetaryDecimalFormat() {
        DecimalFormat moneyFormat= new DecimalFormat();
        moneyFormat.setGroupingUsed(false);
        //moneyFormat.setGroupingSize(3);
        moneyFormat.setMaximumFractionDigits(2);
        moneyFormat.setMinimumFractionDigits(2);
        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.getDefault());
        decimalFormatSymbols.setDecimalSeparator('.');
        //decimalFormatSymbols.setGroupingSeparator(' ');
        moneyFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        return moneyFormat;
    }
}
