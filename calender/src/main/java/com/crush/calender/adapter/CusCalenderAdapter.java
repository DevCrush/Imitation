package com.crush.calender.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.crush.calender.R;
import com.crush.calender.model.ItemDay;

import java.util.List;

/**
 * Created by Crush on 5/25/17.
 */

public class CusCalenderAdapter extends BaseMultiItemQuickAdapter<ItemDay, CalenderItemViewHolder> {
    private boolean showLunar = true;

    public CusCalenderAdapter(List<ItemDay> data) {
        super(data);
        addItemType(0, R.layout.item_cus_calender_title);
        addItemType(1, R.layout.item_cus_calender_day);
    }

    public void setShowLunar(boolean showLunar) {
        this.showLunar = showLunar;
    }

    public boolean isShowLunar() {
        return showLunar;
    }

    @Override
    protected void convert(CalenderItemViewHolder helper, ItemDay item) {
        helper.initViewWithData(item, showLunar);
    }
}
