cmake_minimum_required(VERSION 3.22.1)
project(edge_native)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Set this to your OpenCV Android SDK native jni folder, or pass -DOpenCV_DIR
# Example: set(OpenCV_DIR "C:/OpenCV-android-sdk/sdk/native/jni")
# set(OpenCV_DIR "")

add_library(edge_native SHARED
    src/native_bridge.cpp
    src/opencv_processor.cpp
)

find_package(OpenCV QUIET)
if(OpenCV_FOUND)
    message(STATUS "OpenCV found: ${OpenCV_VERSION}")
    include_directories(${OpenCV_INCLUDE_DIRS})
    target_link_libraries(edge_native ${OpenCV_LIBS})
    target_compile_definitions(edge_native PRIVATE HAVE_OPENCV=1)
else()
    message(WARNING "OpenCV not found. Configure OpenCV_DIR for build.")
endif()

if(ANDROID)
    find_library(log-lib log)
    target_link_libraries(edge_native ${log-lib})
endif()
