package com.crush.example.knife;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.crush.annotation.BindView;
import com.crush.annotationknife.AnnotationKnife;
import com.crush.example.R;

public class SecondActivity extends AppCompatActivity {
    @BindView(R.id.tv)
    TextView mTv;
    @BindView(R.id.img)
    ImageView mImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation_knife);
        AnnotationKnife.bind(this);
        Log.v("", "");
        mTv.setText("222222");
        mImg.setImageResource(R.mipmap.ic_launcher);
        mImg.setBackgroundColor(Color.YELLOW);
    }
}
