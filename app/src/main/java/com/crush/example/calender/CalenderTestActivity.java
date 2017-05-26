package com.crush.example.calender;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.crush.annotation.BindView;
import com.crush.annotation.OnClick;
import com.crush.annotationknife.AnnotationKnife;
import com.crush.calender.CusCalenderView;
import com.crush.example.R;

import java.util.Calendar;

/**
 * Created by Crush on 5/25/17.
 */

public class CalenderTestActivity extends AppCompatActivity {
    @BindView(R.id.cus_cv)
    CusCalenderView cusCalenderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_test);
        AnnotationKnife.bind(this);
    }

    @OnClick(R.id.btn_today)
    void btn_today() {
        Calendar c = Calendar.getInstance();
        cusCalenderView.setDate(c.getTime());
    }

    @OnClick(R.id.btn_invisible_lunar)
    void btn_invisible_lunar() {
        cusCalenderView.goneLunar();
    }

    @OnClick(R.id.btn_visible_lunar)
    void btn_visible_lunar() {
        cusCalenderView.showLunar();
    }
}
