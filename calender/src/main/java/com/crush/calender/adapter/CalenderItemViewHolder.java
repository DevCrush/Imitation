package com.crush.calender.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.crush.calender.R;
import com.crush.calender.lunar.LunarCalendar;
import com.crush.calender.model.ItemDay;

import java.util.Calendar;

/**
 * Created by Crush on 5/25/17.
 */

public class CalenderItemViewHolder extends BaseViewHolder {
    TextView tvDay;
    TextView tvLunar;

    public CalenderItemViewHolder(View view) {
        super(view);
    }

    public void initViewWithData(ItemDay data, boolean showLunar) {
        tvDay = (TextView) itemView.findViewById(R.id.tv_day);
        tvLunar = (TextView) itemView.findViewById(R.id.tv_lunar);
        if (!TextUtils.isEmpty(data.getTitle())) {
            tvDay.setText(data.getTitle());
        } else {
            Calendar c = data.getC();
            if (showLunar) {
                String lunar = "";
                if (1900 <= c.get(Calendar.YEAR) && c.get(Calendar.YEAR) <= 2100) {
                    LunarCalendar lunarCalendar = LunarCalendar.obtainCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
//                lunar = lunarCalendar.getLunarDay()
//                        + "\n" + lunarCalendar.getSubTitle()
//                        + "\n" + lunarCalendar.getSolarTerm()
//                        + "\n" + lunarCalendar.getFestivals().toString();
                    lunar = lunarCalendar.getSubTitle();
                }
                tvLunar.setText(lunar);
                tvLunar.setVisibility(View.VISIBLE);
            } else {
                tvLunar.setVisibility(View.GONE);
            }
            if (data.isChoose()) {
                itemView.setBackgroundColor(Color.GREEN);
                changeTextColor(Color.WHITE);
            } else if (DateUtils.isToday(c.getTimeInMillis())) {
                itemView.setBackgroundColor(Color.BLUE);
                changeTextColor(Color.WHITE);
            } else {
                if (data.getCurrent() < 0) {
                    changeTextColor(Color.GRAY);
                } else if (data.getCurrent() == 0) {
                    changeTextColor(Color.BLACK);
                } else {
                    changeTextColor(Color.GRAY);
                }
                itemView.setBackgroundColor(Color.TRANSPARENT);
            }
            tvDay.setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
        }
    }

    private void changeTextColor(int black) {
        tvDay.setTextColor(black);
        tvLunar.setTextColor(black);
    }
}
