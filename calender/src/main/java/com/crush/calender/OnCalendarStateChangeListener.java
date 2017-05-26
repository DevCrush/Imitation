package com.crush.calender;

import java.util.Date;

/**
 * Created by Crush on 5/26/17.
 */

public interface OnCalendarStateChangeListener {
    void onMonthChange(int year, int month);

    void onDateSelect(Date date);
}
