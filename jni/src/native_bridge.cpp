#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_edge_NativeBridge_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Edge Native Ready";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_edge_NativeBridge_processFrame(
        JNIEnv* env,
        jclass /* clazz */,
        jbyteArray nv21Data,
        jint width,
        jint height,
        jint mode) {
    // TODO: Convert NV21 to Mat, apply grayscale/canny in opencv_processor.cpp
    (void)env; (void)nv21Data; (void)width; (void)height; (void)mode;
}
