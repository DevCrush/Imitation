package com.crush.calender.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Crush on 5/26/17.
 */

public class DateUtils {
    public static boolean isSameDay(Date d1, Date d2) {
        if (null == d1 || null == d2)
            return false;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) && (c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isSameMonth(Date d1, Date d2) {
        if (null == d1 || null == d2)
            return false;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH));
    }
}
