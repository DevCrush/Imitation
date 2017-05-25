package com.crush.calender.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.crush.calender.adapter.CusCalenderAdapter;
import com.crush.calender.model.ItemDay;
import com.crush.calender.factory.DateFactory;

import java.util.ArrayList;
import java.util.Calendar;
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
        mAdapter = new CusCalenderAdapter(content);
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
        mAdapter.setNewData(DateFactory.generateDaysInMonth(year, month, firstDayOfWeekIsSun));
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
}
