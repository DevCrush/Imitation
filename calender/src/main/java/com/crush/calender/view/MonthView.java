package com.crush.calender.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.animation.BaseAnimation;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.crush.calender.R;
import com.crush.calender.adapter.CusCalenderAdapter;
import com.crush.calender.factory.DateFactory;
import com.crush.calender.model.ItemDay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Crush on 5/25/17.
 */

public class MonthView extends RecyclerView {
    GridLayoutManager mGridLayoutManager;
    List<ItemDay> content = new ArrayList<>();
    CusCalenderAdapter mAdapter;
    YearView yearView;

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
        mAdapter.openLoadAnimation(new BaseAnimation() {
            @Override
            public Animator[] getAnimators(View view) {
                return new Animator[]{
                        ObjectAnimator.ofFloat(view, "scaleY", 1f)
                };
            }
        });
        addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                ItemDay itemDay = mAdapter.getData().get(position);
                if (null != yearView) {
                    if (itemDay.getCurrent() > 0) {
                        yearView.nextMonth();
                    } else if (itemDay.getCurrent() < 0) {
                        yearView.lastMonth();
                    }
                }
                if (null != onDateCheckedListener) {
                    onDateCheckedListener.onDateChange(itemDay.getC().getTime());
                }

            }
        });
        setAdapter(mAdapter);
    }

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

    public void showLunar(boolean animation) {
        if (mAdapter.isShowLunar())
            return;
        mAdapter.setShowLunar(true);
        if (animation) {
            mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount() - 1);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void goneLunar(boolean animation) {
        if (!mAdapter.isShowLunar())
            return;
        mAdapter.setShowLunar(false);
        if (animation) {
            mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount() - 1);
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

    public YearView getYearView() {
        return yearView;
    }

    public void setYearView(YearView yearView) {
        this.yearView = yearView;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
