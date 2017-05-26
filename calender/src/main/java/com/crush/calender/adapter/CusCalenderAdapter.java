package com.crush.calender.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.crush.calender.model.ItemDay;
import com.crush.calender.utils.DateUtils;

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
            boolean needCheck = DateUtils.isSameDay(item.getC().getTime(), checkedDate);
            if (needCheck != item.isChoose()) {
                item.setChoose(needCheck);
                notifyItemChanged(i);
            }
        }
    }


}
