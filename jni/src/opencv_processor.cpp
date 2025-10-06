#include <vector>
// #include <opencv2/opencv.hpp> // Enable after OpenCV SDK configured

enum class ProcessMode : int {
    Grayscale = 0,
    Canny = 1
};

struct FrameBufferNV21 {
    const unsigned char* data;
    int width;
    int height;
};

void processFrameNV21(const FrameBufferNV21& /*frame*/, ProcessMode /*mode*/) {
    // Stub: implement NV21 -> BGR/GRAY conversion and processing with OpenCV
    // Example (after enabling OpenCV):
    // cv::Mat yuv(frame.height + frame.height/2, frame.width, CV_8UC1, (void*)frame.data);
    // cv::Mat bgr; cv::cvtColor(yuv, bgr, cv::COLOR_YUV2BGR_NV21);
    // if (mode == ProcessMode::Grayscale) { cv::cvtColor(bgr, bgr, cv::COLOR_BGR2GRAY); }
    // else { cv::Mat edges; cv::Canny(bgr, edges, 100, 200); }
}
