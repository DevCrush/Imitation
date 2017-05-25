package com.crush.calender.model;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.Calendar;

/**
 * Created by Crush on 5/25/17.
 */

public class ItemDay implements MultiItemEntity {
    Calendar c = Calendar.getInstance();
    String title;
    int current;
    boolean choose;

    public ItemDay(long c, int current) {
        this.c.setTimeInMillis(c);
        this.current = current;
    }

    public ItemDay(String title) {
        this.title = title;
    }

    public void setC(Calendar c) {
        this.c = c;
    }

    public Calendar getC() {
        return c;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    @Override
    public int getItemType() {
        return !TextUtils.isEmpty(title) ? 0 : 1;
    }
}
