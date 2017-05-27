#include <jni.h>
#include <string.h>
#include "./libavcodec/avcodec.h"
JNIEXPORT jstring JNICALL
Java_com_crush_ffmpeg_FFmpeg_getAvcodecConfig(JNIEnv *env, jclass type) {
    char info[10000] = { 0 };
    sprintf(info, "%s\n", avcodec_configuration());
    return (*env)->NewStringUTF(env, info);
}