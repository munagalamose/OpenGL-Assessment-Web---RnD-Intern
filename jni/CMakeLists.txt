cmake_minimum_required(VERSION 3.22.1)
project(edge_native)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Hint: set -DOpenCV_DIR or use local variable
# set(OpenCV_DIR "${CMAKE_SOURCE_DIR}/../third_party/opencv/sdk/native/jni")

add_library(edge_native SHARED
    src/native_bridge.cpp
    src/opencv_processor.cpp
)

# Find OpenCV if available
find_package(OpenCV QUIET)
if(OpenCV_FOUND)
    message(STATUS "OpenCV found: ${OpenCV_VERSION}")
    include_directories(${OpenCV_INCLUDE_DIRS})
    target_link_libraries(edge_native ${OpenCV_LIBS})
else()
    message(WARNING "OpenCV not found. Configure OpenCV_DIR for build.")
endif()

# Android NDK log if needed
if(ANDROID)
    find_library(log-lib log)
    target_link_libraries(edge_native ${log-lib})
endif()
