package com.crush.calender.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.crush.calender.model.ItemDay;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Crush on 5/25/17.
 */

public class CusCalenderAdapter extends BaseQuickAdapter<ItemDay, CalenderItemViewHolder> {
    private boolean showLunar = true;

    public CusCalenderAdapter(int layoutResId, List<ItemDay> data) {
        super(layoutResId, data);
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

    private Date checkedDate = null;

    public void choosePosition(Date checkedDate) {
        int size = getData().size();
        List<ItemDay> data = getData();
        for (int i = 0; i < size; i++) {
            ItemDay item = data.get(i);
            boolean needCheck = isSameDay(item.getC().getTime(), checkedDate);
            if (needCheck != item.isChoose()) {
                item.setChoose(needCheck);
                notifyItemChanged(i);
            }
        }
    }

    private boolean isSameDay(Date d1, Date d2) {
        if (null == d1 || null == d2)
            return false;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) && (c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR));
    }
}
