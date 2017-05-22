package com.crush.example.knife;

import android.widget.TextView;

import com.crush.annotation.BindView;
import com.crush.annotation.OnClick;
import com.crush.example.R;

/**
 * Created by Crush on 5/22/17.
 */

public class ViewHolder {
    @BindView(R.id.tv)
    TextView tv;

    @OnClick(R.id.img)
    void img() {

    }
}
