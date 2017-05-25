package com.crush.calender.factory;

import com.crush.calender.model.ItemDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Crush on 5/25/17.
 */

public class DateFactory {
    public static List<ItemDay> generateDaysInMonth(int year, int month, boolean firstDayOfWeekIsSun) {
        Calendar mCalendar = Calendar.getInstance();
        List<ItemDay> content = new ArrayList<>();
        if (firstDayOfWeekIsSun) {
            content.add(new ItemDay("日"));
        }
        content.add(new ItemDay("一"));
        content.add(new ItemDay("二"));
        content.add(new ItemDay("三"));
        content.add(new ItemDay("四"));
        content.add(new ItemDay("五"));
        content.add(new ItemDay("六"));
        if (!firstDayOfWeekIsSun) {
            content.add(new ItemDay("日"));
        }
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.setFirstDayOfWeek(firstDayOfWeekIsSun ? Calendar.SUNDAY : Calendar.MONDAY);
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mCalendar.set(Calendar.MONTH, month);
        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek > 1) {
            Calendar lastMonth = Calendar.getInstance();
            lastMonth.set(Calendar.DAY_OF_MONTH, 1);
            lastMonth.add(Calendar.DAY_OF_YEAR, -dayOfWeek + 1);
            for (int i = 1; i < dayOfWeek; i++) {
                content.add(new ItemDay(lastMonth.getTimeInMillis(), -1));
                lastMonth.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
        int daysInMonth = mCalendar.getActualMaximum(Calendar.DATE);
        for (int i = 0; i < daysInMonth; i++) {
            content.add(new ItemDay(mCalendar.getTimeInMillis(), 0));
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        for (int i = content.size(); i < 49; i++) {
            content.add(new ItemDay(mCalendar.getTimeInMillis(), 1));
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return content;
    }

}
