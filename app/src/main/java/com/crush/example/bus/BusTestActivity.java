package com.crush.example.bus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.crush.annotation.BindView;
import com.crush.annotation.OnClick;
import com.crush.annotationknife.AnnotationKnife;
import com.crush.bus.Bus;
import com.crush.bus.annotation.Subscribe;
import com.crush.example.R;

/**
 * Created by Crush on 5/26/17.
 */

public class BusTestActivity extends AppCompatActivity {
    @BindView(R.id.tv)
    TextView tv;


    Bus bus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_test);
        AnnotationKnife.bind(this);
        bus = Bus.createNewBus();
        bus.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    @OnClick(R.id.btn)
    void send() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bus.postEvent(new Object());
            }
        }).start();
    }

    @Subscribe
    public void aaaaaa(Object o) {
        final long id = Thread.currentThread().getId();
        tv.post(new Runnable() {
            @Override
            public void run() {
                tv.append("收到一个,当前线程：" + id + "\n");
            }
        });
    }
}
