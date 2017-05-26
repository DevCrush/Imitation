package com.crush.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.crush.annotation.BindView;
import com.crush.annotationknife.AnnotationKnife;
import com.crush.example.bus.BusTestActivity;
import com.crush.example.calender.CalenderTestActivity;
import com.crush.example.calender.YearViewTestActivity;
import com.crush.example.knife.AnnotationKnifeActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.lv)
    ListView listView;
    ArrayAdapter<String> adapter;
    static final String ANNOTATION_KNIFE = "AnnotationKnife";
    static final String CALENDER_VIEW = "CalenderView";
    static final String YEAR_VIEW = "YearView";
    static final String BUS = "Bus";
    static final String[] activities = new String[]{
            ANNOTATION_KNIFE,
            CALENDER_VIEW,
            YEAR_VIEW,
            BUS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnnotationKnife.bind(this);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, activities);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = null;
        switch (adapter.getItem(i)) {
            case ANNOTATION_KNIFE:
                intent = new Intent(this, AnnotationKnifeActivity.class);
                break;
            case YEAR_VIEW:
                intent = new Intent(this, YearViewTestActivity.class);
                break;
            case CALENDER_VIEW:
                intent = new Intent(this, CalenderTestActivity.class);
                break;
            case BUS:
                intent = new Intent(this, BusTestActivity.class);
                break;
            default:
                break;
        }
        if (null != intent) {
            startActivity(intent);
        }
    }
}
