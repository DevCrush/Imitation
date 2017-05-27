package com.crush.ffmpeg;

/**
 * Created by Crush on 5/27/17.
 */

public class FFmpeg {
    static {
        System.loadLibrary("ffmpegutil");
    }
    public native static String getConfig();
}
