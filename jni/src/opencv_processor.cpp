#include <vector>
#include <cstddef>

#ifdef HAVE_OPENCV
#include <opencv2/imgproc.hpp>
#include <opencv2/core.hpp>
#endif

enum class ProcessMode : int {
    Grayscale = 0,
    Canny = 1
};

static inline ProcessMode toMode(int mode) {
    return mode == 1 ? ProcessMode::Canny : ProcessMode::Grayscale;
}

void processFrameNV21Bytes(const unsigned char* data, int width, int height, int mode,
                           std::vector<unsigned char>& outGray) {
#ifdef HAVE_OPENCV
    cv::Mat yuv(height + height/2, width, CV_8UC1, const_cast<unsigned char*>(data));
    cv::Mat bgr; cv::cvtColor(yuv, bgr, cv::COLOR_YUV2BGR_NV21);
    if (toMode(mode) == ProcessMode::Grayscale) {
        cv::Mat gray; cv::cvtColor(bgr, gray, cv::COLOR_BGR2GRAY);
        outGray.assign(gray.data, gray.data + static_cast<size_t>(gray.total()));
    } else {
        cv::Mat gray, edges; cv::cvtColor(bgr, gray, cv::COLOR_BGR2GRAY);
        cv::Canny(gray, edges, 100, 200);
        outGray.assign(edges.data, edges.data + static_cast<size_t>(edges.total()));
    }
#else
    const size_t ySize = static_cast<size_t>(width) * static_cast<size_t>(height);
    outGray.assign(data, data + ySize);
#endif
}
