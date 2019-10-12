package com.sb.test.rates;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class RateDiffUtilCallback extends DiffUtil.Callback {
    private List<RateItem> oldList;
    private List<RateItem> newList;

    public RateDiffUtilCallback(List<RateItem> oldList, List<RateItem> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        RateItem oldItem= oldList.get(oldItemPosition);
        RateItem newItem= newList.get(newItemPosition);

        // в принимаемых объектах нет id, поэтому полагаемся на короткое наименование
        return oldItem.getShortName().equals(newItem.getShortName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        RateItem oldItem= oldList.get(oldItemPosition);
        RateItem newItem= newList.get(newItemPosition);

        return oldItem.equals(newItem) && oldItemPosition != 0 && newItemPosition != 0;
    }
}
