package com.crush.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.crush.annotation.BindView;
import com.crush.annotationknife.AnnotationKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv)
    TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnnotationKnife.bind(this);
        Log.v("","");
        mTv.setText("11111111111111111111");
    }
}
