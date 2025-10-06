#include <jni.h>
#include <string>
#include <vector>
#include <mutex>

// Forward decl from processor
void processFrameNV21Bytes(const unsigned char* data, int width, int height, int mode,
                           std::vector<unsigned char>& outGray);

static std::mutex g_mutex;
static std::vector<unsigned char> g_lastGray;
static int g_lastW = 0, g_lastH = 0;

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
    const int len = env->GetArrayLength(nv21Data);
    std::vector<unsigned char> buf(len);
    env->GetByteArrayRegion(nv21Data, 0, len, reinterpret_cast<jbyte*>(buf.data()));

    std::vector<unsigned char> outGray;
    processFrameNV21Bytes(buf.data(), width, height, mode, outGray);

    {
        std::lock_guard<std::mutex> lock(g_mutex);
        g_lastGray.swap(outGray);
        g_lastW = width; g_lastH = height;
    }
}

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_example_edge_NativeBridge_getLastGray(
        JNIEnv* env,
        jclass /* clazz */) {
    std::lock_guard<std::mutex> lock(g_mutex);
    if (g_lastGray.empty()) return nullptr;
    jbyteArray arr = env->NewByteArray(static_cast<jsize>(g_lastGray.size()));
    env->SetByteArrayRegion(arr, 0, static_cast<jsize>(g_lastGray.size()), reinterpret_cast<const jbyte*>(g_lastGray.data()));
    return arr;
}
