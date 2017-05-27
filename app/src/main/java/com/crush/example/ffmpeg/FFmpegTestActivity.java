package com.crush.example.ffmpeg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.crush.annotation.BindView;
import com.crush.example.R;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

/**
 * Created by Crush on 5/27/17.
 */

public class FFmpegTestActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView tv;

    String TAG = FFmpegTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg_test);
//        tv.setText(FFmpeg.getConfig());
        try {
            String cmd = "-version";
            String[] command = cmd.split(" ");
            FFmpeg.getInstance(this).execute(command, new FFmpegExecuteResponseHandler() {
                @Override
                public void onSuccess(String s) {
                    Log.v(TAG, "onSuccess" + s);
                }

                @Override
                public void onProgress(String s) {
                    Log.i(TAG, "onProgress" + s);
                }

                @Override
                public void onFailure(String s) {
                    Log.e(TAG, "onFailure" + s);
                }

                @Override
                public void onStart() {
                    Log.i(TAG, "onStart");
                }

                @Override
                public void onFinish() {
                    Log.i(TAG, "onFinish");
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }
}
