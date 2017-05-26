package com.crush.example.calender;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crush.annotation.BindView;
import com.crush.annotationknife.AnnotationKnife;
import com.crush.calender.OnCalendarStateChangeListener;
import com.crush.calender.view.YearView;
import com.crush.example.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Crush on 5/25/17.
 */

public class YearViewTestActivity extends AppCompatActivity {
    @BindView(R.id.year_view)
    YearView yearView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_view_test);
        AnnotationKnife.bind(this);
        yearView.setCusCalenderListener(new OnCalendarStateChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                Log.v("onMonthChange", year + "/" + month);
            }

            @Override
            public void onDateSelect(Date date) {
                Log.v("onDateSelect", new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(date));
            }
        });
    }


}
