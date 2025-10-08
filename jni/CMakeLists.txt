cmake_minimum_required(VERSION 3.22.1)
project(edge_native)

# C++ standard
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

# Enable exceptions and RTTI
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fexceptions -frtti")

# Shared C++ runtime
set(ANDROID_STL c++_shared)

# OpenCV SDK path
set(OpenCV_DIR "C:/OpenCV-android-sdk/sdk/native/jni")

# Source files
add_library(edge_native SHARED
        src/native_bridge.cpp
        src/opencv_processor.cpp
)

# Find OpenCV
find_package(OpenCV QUIET)
if(OpenCV_FOUND)
    message(STATUS "✅ OpenCV found: ${OpenCV_VERSION}")
    include_directories(${OpenCV_INCLUDE_DIRS})
else()
    message(WARNING "⚠️ OpenCV not found. Configure OpenCV_DIR correctly.")
endif()

# Link libraries (use ONE keyword-style target_link_libraries call)
target_link_libraries(edge_native
        PRIVATE
        ${OpenCV_LIBS}   # OpenCV libraries
        log              # Android log
        c++_shared       # Shared C++ runtime
)
