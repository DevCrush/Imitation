package com.crush.calender.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.crush.calender.R;
import com.crush.calender.adapter.CusCalenderAdapter;
import com.crush.calender.factory.DateFactory;
import com.crush.calender.model.ItemDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Crush on 5/25/17.
 */

public class MonthView extends RecyclerView {
    GridLayoutManager mGridLayoutManager;
    List<ItemDay> content = new ArrayList<>();
    CusCalenderAdapter mAdapter;

    public MonthView(Context context) {
        super(context);
        initView();
    }

    public MonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        mGridLayoutManager = new GridLayoutManager(getContext(), 7, LinearLayoutManager.VERTICAL, false);
        mGridLayoutManager.setAutoMeasureEnabled(true);
        setLayoutManager(mGridLayoutManager);
        mAdapter = new CusCalenderAdapter(R.layout.item_cus_calender_day, content);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                if (null != onDateCheckedListener) {
                    onDateCheckedListener.onDateChange(mAdapter.getData().get(position).getC().getTime());
                }
            }
        });
        setAdapter(mAdapter);
    }

    Calendar mCalendar = Calendar.getInstance();
    int year, month;
    boolean firstDayOfWeekIsSun = true;

    public boolean isFirstDayOfWeekIsSun() {
        return firstDayOfWeekIsSun;
    }

    public void setFirstDayOfWeekIsSun(boolean firstDayOfWeekIsSun) {
        this.firstDayOfWeekIsSun = firstDayOfWeekIsSun;
    }

    public void freshContentData(int year, int month) {
        this.year = year;
        this.month = month;
        mAdapter.setNewData(new ArrayList<ItemDay>());
        mAdapter.addData(DateFactory.generateDaysInMonth(year, month, firstDayOfWeekIsSun));
    }

    int lastChoosePosition;

    public void chooseDate(long date) {
        mAdapter.getItem(lastChoosePosition).setChoose(false);
        mAdapter.notifyItemChanged(lastChoosePosition);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        int offset = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        c.setTimeInMillis(date);
        int position = offset + 7 + c.get(Calendar.DAY_OF_MONTH);
        ItemDay day = mAdapter.getItem(position);
        day.setChoose(true);
        mAdapter.notifyItemChanged(position);
        lastChoosePosition = position;
    }

    public void showLunar(boolean animation) {
        if (mAdapter.isShowLunar())
            return;
        mAdapter.setShowLunar(true);
        if (animation) {
            mAdapter.notifyItemRangeChanged(0, 41);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void goneLunar(boolean animation) {
        if (!mAdapter.isShowLunar())
            return;
        mAdapter.setShowLunar(false);
        if (animation) {
            mAdapter.notifyItemRangeChanged(0, 41);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void chooseDate(Date date) {
        mAdapter.choosePosition(date);
    }

    OnDateCheckedListener onDateCheckedListener;

    public OnDateCheckedListener getOnDateCheckedListener() {
        return onDateCheckedListener;
    }

    public void setOnDateCheckedListener(OnDateCheckedListener onDateCheckedListener) {
        this.onDateCheckedListener = onDateCheckedListener;
    }
}
