package com.crush.calender;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crush.calender.view.YearView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Crush on 5/25/17.
 */

public class CusCalenderView extends RelativeLayout {
    Context mContext;

    TextView tvYear, tvMonth;
    Button btnLastMonth, btnNextMonth;
    YearView yearView;

    public CusCalenderView(Context context) {
        super(context);
        initView();
    }

    public CusCalenderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CusCalenderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    Calendar now = Calendar.getInstance();

    private void initView() {
        mContext = getContext();
        LayoutInflater.from(mContext).inflate(R.layout.cus_calender_view, this, true);
        findView();
        yearView.setMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void currentMonth(Date date) {
                now.setTime(date);
                freshContent();
                if (null != onMonthChangeListener) {
                    onMonthChangeListener.currentMonth(date);
                }
            }
        });
        now.set(Calendar.DAY_OF_MONTH, 1);
        freshContent();
    }

    public void setDate(Date date) {
        yearView.setDate(date);
    }

    DecimalFormat format = new DecimalFormat("00");

    private void freshContent() {
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        tvYear.setText(year + "年");
        tvMonth.setText(format.format(month + 1) + "月");
    }

    private void findView() {
        tvYear = (TextView) findViewById(R.id.tv_year);
        tvMonth = (TextView) findViewById(R.id.tv_month);
        btnLastMonth = (Button) findViewById(R.id.btn_last_month);
        btnNextMonth = (Button) findViewById(R.id.btn_next_month);
        yearView = (YearView) findViewById(R.id.year_view);
        btnLastMonth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                yearView.lastMonth();
            }
        });
        btnNextMonth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                yearView.nextMonth();
            }
        });
    }

    OnMonthChangeListener onMonthChangeListener;

    public OnMonthChangeListener getOnMonthChangeListener() {
        return onMonthChangeListener;
    }

    public void setOnMonthChangeListener(OnMonthChangeListener onMonthChangeListener) {
        this.onMonthChangeListener = onMonthChangeListener;
    }

    public void showLunar() {
        yearView.showLunar();
    }

    public void goneLunar() {
        yearView.goneLunar();
    }
}
