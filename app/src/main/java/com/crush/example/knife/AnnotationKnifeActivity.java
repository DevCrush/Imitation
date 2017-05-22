package com.crush.example.knife;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crush.annotation.BindView;
import com.crush.annotation.OnClick;
import com.crush.annotationknife.AnnotationKnife;
import com.crush.example.R;

/**
 * Created by Crush on 2017/5/21.
 */

public class AnnotationKnifeActivity extends AppCompatActivity {
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
        mTv.setText("11111111111111111111");
        mImg.setImageResource(R.mipmap.ic_launcher);
        mImg.setBackgroundColor(Color.RED);
//        mImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(AnnotationKnifeActivity.this, SecondActivity.class));
//            }
//        });
    }

    @OnClick(R.id.img)
    void click() {
        Toast.makeText(this, "无参数", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AnnotationKnifeActivity.this, SecondActivity.class));
    }

    @OnClick(R.id.tv)
    void click(View v) {
        Toast.makeText(this, v.getClass().getCanonicalName(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AnnotationKnifeActivity.this, SecondActivity.class));
    }
}
