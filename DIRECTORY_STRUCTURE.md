# 📁 Complete Directory Structure Guide

## 🎯 Where to Place OpenCV

### Recommended Setup

```
C:\                                    # or /Users/YourName/ on Mac
└── OpenCV-android-sdk\               # ← Download and extract here
    └── sdk\
        └── native\
            └── jni\                   # ← This path goes in CMakeLists.txt
                ├── abi-armeabi-v7a\
                ├── abi-arm64-v8a\
                ├── abi-x86\
                ├── abi-x86_64\
                └── OpenCVConfig.cmake # ← Must exist!
```

**Then set in `jni/CMakeLists.txt`:**
```cmake
set(OpenCV_DIR "C:/OpenCV-android-sdk/sdk/native/jni")
```

---

## 📂 Project Structure (Complete)

```
flam-assignment\                       # ← Root project folder
│
├── .git\                              # Git repository
├── .gitignore                         # Git ignore rules
│
├── README.md                          # ← Main project overview
├── SETUP_GUIDE.md                     # ← Detailed setup instructions
├── QUICK_START.md                     # ← Quick reference
├── DIRECTORY_STRUCTURE.md             # ← This file
│
├── build.gradle                       # Root Gradle config
├── settings.gradle                    # Gradle settings
│
├── app\                               # ═══ ANDROID APP ═══
│   ├── .placeholder
│   ├── build.gradle                   # App-level Gradle config
│   ├── proguard-rules.pro
│   └── src\
│       ├── main\
│       │   ├── AndroidManifest.xml    # App manifest
│       │   │
│       │   ├── java\com\example\edge\
│       │   │   ├── MainActivity.kt           # Main activity (UI)
│       │   │   ├── NativeBridge.kt          # JNI bridge to C++
│       │   │   │
│       │   │   ├── camera\
│       │   │   │   └── CameraController.kt  # Camera handling
│       │   │   │
│       │   │   └── gl\
│       │   │       └── FrameRenderer.kt     # OpenGL rendering
│       │   │
│       │   └── res\                          # Android resources
│       │       ├── layout\
│       │       │   └── activity_main.xml    # Main layout
│       │       ├── values\
│       │       │   ├── colors.xml
│       │       │   ├── strings.xml
│       │       │   └── themes.xml
│       │       └── xml\
│       │           └── file_paths.xml       # File provider config
│       │
│       ├── debug\                            # Debug build variant
│       └── release\                          # Release build variant
│
├── jni\                               # ═══ NATIVE C++ CODE ═══
│   ├── CMakeLists.txt                 # ← CONFIGURE OPENCV PATH HERE (line 9)
│   └── src\
│       ├── native_bridge.cpp          # JNI implementation
│       ├── opencv_processor.cpp       # OpenCV processing logic
│       └── opencv_processor.h         # Header file
│
├── gl\                                # ═══ OPENGL SHADERS ═══
│   └── shaders\
│       ├── vertex.glsl                # Vertex shader
│       └── fragment.glsl              # Fragment shader
│
└── web\                               # ═══ WEB VIEWER ═══
    ├── package.json                   # Node.js dependencies
    ├── package-lock.json              # Locked versions
    ├── tsconfig.json                  # TypeScript config
    ├── index.html                     # ← UPDATE BASE64 HERE (line 38)
    │
    ├── node_modules\                  # Installed packages (auto-generated)
    │
    └── src\
        └── main.ts                    # TypeScript entry point
```

---

## 🎯 Key Files to Edit

### 1. Configure OpenCV Path
**File:** `jni/CMakeLists.txt`  
**Line:** 9  
**What to change:**
```cmake
# Before:
# set(OpenCV_DIR "")

# After (Windows):
set(OpenCV_DIR "C:/OpenCV-android-sdk/sdk/native/jni")

# After (Mac):
set(OpenCV_DIR "/Users/YourName/OpenCV-android-sdk/sdk/native/jni")

# After (Linux):
set(OpenCV_DIR "/home/username/OpenCV-android-sdk/sdk/native/jni")
```

### 2. Update Web Viewer with Exported Frame
**File:** `web/index.html`  
**Line:** 38  
**What to change:**
```javascript
// Before:
window.sampleFrame = window.sampleFrame || 'data:image/svg+xml;base64,...';

// After:
window.sampleFrame = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA..."; // Your base64 here
```

---

## 📱 Android Device File Structure

When you run the app and export frames, files are saved here:

```
/sdcard/                               # Device internal storage
└── Android\
    └── data\
        └── com.example.edge\          # Your app's package
            └── files\
                └── export\            # ← Exported frames here
                    ├── frame_gray.pgm           # Raw image file
                    └── frame_gray_base64.txt    # Base64 for web
```

**How to access:**
1. **Android Studio:** View → Tool Windows → Device File Explorer
2. **ADB command:** `adb pull /sdcard/Android/data/com.example.edge/files/export/`
3. **File manager app** on device (if it has permission)

---

## 🗂️ Build Output (Auto-generated)

These folders are created automatically during build:

```
flam-assignment\
├── .gradle\                           # Gradle cache
├── .idea\                             # Android Studio settings
├── build\                             # Build output (root)
│
├── app\
│   ├── build\                         # App build output
│   │   ├── intermediates\
│   │   ├── outputs\
│   │   │   └── apk\
│   │   │       ├── debug\
│   │   │       │   └── app-debug.apk  # ← Your APK file
│   │   │       └── release\
│   │   └── tmp\
│   └── .cxx\                          # Native build cache
│
└── web\
    ├── node_modules\                  # Node packages (after npm install)
    └── dist\                          # Built web app (after npm run build)
```

---

## 🔍 Finding Important Files

### During Development

| What you need | Where to find it |
|---------------|------------------|
| Configure OpenCV | `jni/CMakeLists.txt` |
| Main Android code | `app/src/main/java/com/example/edge/MainActivity.kt` |
| OpenCV processing | `jni/src/opencv_processor.cpp` |
| Web viewer | `web/index.html` |
| Exported frames | Device: `/sdcard/Android/data/com.example.edge/files/export/` |
| Built APK | `app/build/outputs/apk/debug/app-debug.apk` |

### After Building

| File | Purpose |
|------|---------|
| `app-debug.apk` | Installable Android app |
| `frame_gray.pgm` | Raw grayscale image |
| `frame_gray_base64.txt` | Base64 for web display |
| `build/` folders | Compiled code (can be deleted to clean) |

---

## 🧹 Clean Build

If you need to start fresh:

**In Android Studio:**
```
Build → Clean Project
Build → Rebuild Project
```

**Or delete these folders:**
```
flam-assignment\
├── .gradle\          ← Delete
├── build\            ← Delete
├── app\build\        ← Delete
├── app\.cxx\         ← Delete
└── web\node_modules\ ← Delete (then run npm install again)
```

---

## 📦 What to Backup

If you want to save your work:

**Essential files (small):**
```
✅ app/src/          # Your code
✅ jni/src/          # Native code
✅ web/src/          # Web code
✅ web/index.html    # Web page
✅ jni/CMakeLists.txt
✅ app/build.gradle
✅ build.gradle
```

**Don't need to backup (auto-generated):**
```
❌ .gradle/
❌ .idea/
❌ build/
❌ app/build/
❌ app/.cxx/
❌ web/node_modules/
❌ web/dist/
```

---

## 🎯 Path Examples by OS

### Windows
```
OpenCV:  C:/OpenCV-android-sdk/sdk/native/jni
Project: C:/Users/DELL/Desktop/flam-assignment
Web:     C:/Users/DELL/Desktop/flam-assignment/web
```

### macOS
```
OpenCV:  /Users/YourName/OpenCV-android-sdk/sdk/native/jni
Project: /Users/YourName/Projects/flam-assignment
Web:     /Users/YourName/Projects/flam-assignment/web
```

### Linux
```
OpenCV:  /home/username/OpenCV-android-sdk/sdk/native/jni
Project: /home/username/projects/flam-assignment
Web:     /home/username/projects/flam-assignment/web
```

---

## ✅ Verification Checklist

Before building, verify these paths exist:

- [ ] `C:/OpenCV-android-sdk/sdk/native/jni/OpenCVConfig.cmake`
- [ ] `flam-assignment/jni/CMakeLists.txt` (OpenCV_DIR set)
- [ ] `flam-assignment/app/build.gradle`
- [ ] `flam-assignment/web/package.json`
- [ ] `flam-assignment/web/node_modules/` (after npm install)

---

**Need help?** See [SETUP_GUIDE.md](SETUP_GUIDE.md) for detailed instructions!
