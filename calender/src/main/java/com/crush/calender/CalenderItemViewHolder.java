package com.crush.calender;

import android.graphics.Color;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Calendar;

/**
 * Created by Crush on 5/25/17.
 */

public class CalenderItemViewHolder extends BaseViewHolder {
    TextView tvDay;

    public CalenderItemViewHolder(View view) {
        super(view);
    }

    public void initViewWithData(ItemDay data) {
        tvDay = (TextView) itemView.findViewById(R.id.tv_day);
        if (!TextUtils.isEmpty(data.getTitle())) {
            tvDay.setText(data.getTitle());
        } else {
            Calendar c = data.getC();
            if (data.isChoose()) {
                tvDay.setBackgroundColor(Color.GREEN);
                tvDay.setTextColor(Color.WHITE);
            } else if (DateUtils.isToday(c.getTimeInMillis())) {
                tvDay.setBackgroundColor(Color.BLUE);
                tvDay.setTextColor(Color.WHITE);
            } else {
                if (data.getCurrent() < 0) {
                    tvDay.setTextColor(Color.GRAY);
                } else if (data.getCurrent() == 0) {
                    tvDay.setTextColor(Color.BLACK);
                } else {
                    tvDay.setTextColor(Color.GRAY);
                }
                tvDay.setBackgroundColor(Color.TRANSPARENT);
            }
            tvDay.setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
        }

    }
}
