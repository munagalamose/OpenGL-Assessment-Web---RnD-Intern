# OpenGL Edge Viewer - Android + Web

![image alt](https://github.com/munagalamose/Blogging-Platform/blob/d05d40721a9fee7af725224d56d3f0c93f1cb3ae/Screenshot%202025-07-02%20211346.png)


Real-time edge detection using OpenCV on Android with web-based frame viewer.

## 🚀 Quick Start

1. **Download OpenCV Android SDK**: https://opencv.org/releases/
2. **Configure path** in `jni/CMakeLists.txt` (line 9)
3. **Open in Android Studio** and sync Gradle
4. **Connect Android device** with USB debugging
5. **Run the app** and export frames
6. **View on web** at `web/index.html`

## 📚 Documentation

- **[SETUP_GUIDE.md](SETUP_GUIDE.md)** - Complete step-by-step setup instructions
- **[QUICK_START.md](QUICK_START.md)** - 5-minute quick reference

## 🎯 Features

- ✅ Real-time camera processing with OpenCV
- ✅ Multiple processing modes (Grayscale, Canny edge detection, Raw Y-plane)
- ✅ OpenGL ES rendering pipeline
- ✅ Frame export to base64 for web viewing
- ✅ Web-based viewer with FPS counter

## 🛠️ Tech Stack

**Android:**
- Kotlin
- OpenCV 4.x
- OpenGL ES
- CameraX
- NDK/JNI

**Web:**
- TypeScript
- Vite
- HTML5 Canvas

## 📁 Project Structure

```
├── app/          # Android application
├── jni/          # Native C++ OpenCV code
├── web/          # Web viewer interface
└── gl/           # OpenGL shaders
```

## ⚡ Requirements

- Android Studio (latest)
- Android device (API 24+)
- OpenCV Android SDK
- Node.js (for web viewer)

## 📸 Usage

1. Launch app on Android device
2. Toggle between processing modes
3. Tap Export to save frame
4. Copy base64 from device
5. Paste into web viewer
6. View processed frame in browser

## 🐛 Troubleshooting

See [SETUP_GUIDE.md](SETUP_GUIDE.md#-troubleshooting) for common issues and solutions.

## 📄 License

Educational project for R&D internship assessment.
