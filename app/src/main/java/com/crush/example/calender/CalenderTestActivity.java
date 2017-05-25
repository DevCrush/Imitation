package com.crush.example.calender;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.crush.calender.CusCalenderView;
import com.crush.example.R;

import java.util.Calendar;

/**
 * Created by Crush on 5/25/17.
 */

public class CalenderTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_test);
        CusCalenderView cusCalenderView = (CusCalenderView) findViewById(R.id.cus_cv);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2100);
        c.set(Calendar.MONTH, 12);
        c.set(Calendar.DAY_OF_MONTH, 1);
        cusCalenderView.setDate(c.getTime());
    }
}
