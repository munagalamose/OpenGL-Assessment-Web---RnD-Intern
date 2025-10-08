# 🎨 Visual Setup Guide

Quick visual reference for the complete setup process.

---

## 🗺️ Setup Flow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│  STEP 1: Download OpenCV Android SDK                        │
│  https://opencv.org/releases/                               │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  STEP 2: Extract to C:\OpenCV-android-sdk\                  │
│  Verify: sdk\native\jni\OpenCVConfig.cmake exists           │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  STEP 3: Open Project in Android Studio                     │
│  File → Open → flam-assignment folder                       │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  STEP 4: Configure OpenCV Path                              │
│  Edit: jni/CMakeLists.txt (line 9)                          │
│  set(OpenCV_DIR "C:/OpenCV-android-sdk/sdk/native/jni")     │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  STEP 5: Sync Gradle                                        │
│  Click "Sync Now" → Wait for "OpenCV found" message         │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  STEP 6: Connect Android Device                             │
│  Enable USB Debugging → Connect via USB → Allow prompt      │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  STEP 7: Build & Run                                        │
│  Click Run ▶ → Select device → App installs & launches      │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  STEP 8: Export Frame                                       │
│  Select mode (Gray/Canny/RawY) → Tap Export button          │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  STEP 9: Retrieve Base64 File                               │
│  Device File Explorer → /Android/data/.../export/           │
│  Save frame_gray_base64.txt to computer                     │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  STEP 10: Update Web Viewer                                 │
│  Edit: web/index.html (line 38)                             │
│  Paste base64 string → Save                                 │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  STEP 11: View in Browser                                   │
│  Run: npm run dev → Open http://localhost:5173              │
│  ✅ SUCCESS! Frame displayed in browser                     │
└─────────────────────────────────────────────────────────────┘
```

---

## 📂 File Path Map

```
YOUR COMPUTER
│
├── C:\OpenCV-android-sdk\              ← Download & extract here
│   └── sdk\native\jni\
│       └── OpenCVConfig.cmake          ← Must exist!
│
└── C:\Users\DELL\Desktop\flam-assignment\    ← Project root
    │
    ├── jni\
    │   └── CMakeLists.txt              ← Edit line 9 (OpenCV path)
    │
    ├── app\
    │   ├── build.gradle
    │   └── src\main\java\com\example\edge\
    │       └── MainActivity.kt         ← Main Android code
    │
    └── web\
        ├── index.html                  ← Edit line 38 (base64)
        └── src\main.ts

ANDROID DEVICE
│
└── /sdcard/Android/data/com.example.edge/files/export/
    ├── frame_gray.pgm                  ← Raw image
    └── frame_gray_base64.txt           ← Copy this to computer!
```

---

## 🎯 Key Edits Visual

### Edit 1: Configure OpenCV

**File:** `jni/CMakeLists.txt`

```cmake
 1  cmake_minimum_required(VERSION 3.22.1)
 2  project(edge_native)
 3
 4  set(CMAKE_CXX_STANDARD 17)
 5  set(CMAKE_CXX_STANDARD_REQUIRED ON)
 6
 7  # Set this to your OpenCV Android SDK native jni folder
 8  # Example: set(OpenCV_DIR "C:/OpenCV-android-sdk/sdk/native/jni")
 9  set(OpenCV_DIR "C:/OpenCV-android-sdk/sdk/native/jni")  ← EDIT THIS LINE
10
11  add_library(edge_native SHARED
```

**What to change:**
- Line 9: Uncomment and set YOUR OpenCV path
- Use forward slashes `/` even on Windows
- Path must end with `/jni`

---

### Edit 2: Update Web Viewer

**File:** `web/index.html`

```html
35      <script type="module" src="/src/main.ts"></script>
36      <script>
37        // Standalone fallback: set a tiny placeholder if bundler isn't used
38        window.sampleFrame = "data:image/png;base64,YOUR_BASE64_HERE";  ← EDIT THIS LINE
39      </script>
40    </body>
41  </html>
```

**What to change:**
- Line 38: Replace `YOUR_BASE64_HERE` with content from `frame_gray_base64.txt`
- Keep the prefix: `"data:image/png;base64,`
- Keep the quotes and semicolon at the end

---

## 🔄 Data Flow Diagram

```
┌──────────────────┐
│  Android Camera  │
│   (Live Feed)    │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│  Native C++ Code │
│  (OpenCV Process)│
│  • Grayscale     │
│  • Canny Edge    │
│  • Raw Y-plane   │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│  OpenGL Renderer │
│  (Display Frame) │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│  Export Button   │
│  (User Action)   │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│  Save to Device  │
│  • .pgm file     │
│  • .txt (base64) │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│  Copy to PC      │
│  (Device File    │
│   Explorer/ADB)  │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│  Update HTML     │
│  (Paste base64)  │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│  Web Browser     │
│  (View Result)   │
│  ✅ SUCCESS!     │
└──────────────────┘
```

---

## 🎮 App Interface Layout

```
┌─────────────────────────────────────┐
│  Edge Viewer                    ⚙️  │  ← Title bar
├─────────────────────────────────────┤
│                                     │
│                                     │
│         CAMERA PREVIEW              │
│      (Processed in real-time)       │
│                                     │
│                                     │
│                                     │
├─────────────────────────────────────┤
│  [Gray]  [Canny]  [RawY]            │  ← Mode toggle buttons
├─────────────────────────────────────┤
│           [Export Frame]            │  ← Export button
└─────────────────────────────────────┘
```

**Button Functions:**
- **Gray**: Converts to grayscale
- **Canny**: Edge detection algorithm
- **RawY**: Raw camera Y-plane data
- **Export**: Saves current frame to device storage

---

## 🌐 Web Viewer Layout

```
┌─────────────────────────────────────────────────────┐
│  Edge Viewer (Web)                                  │
│  FPS: 61 | Resolution: 640×360                      │
├─────────────────────────────────────────────────────┤
│                          │                          │
│                          │  Displays a sample       │
│    [Processed Frame]     │  processed frame         │
│    (Your exported        │  (base64) exported       │
│     image appears        │  from the Android        │
│     here)                │  pipeline.               │
│                          │                          │
│                          │  Update via:             │
│                          │  window.sampleFrame      │
└─────────────────────────────────────────────────────┘
```

---

## 🎨 Processing Modes Comparison

```
┌─────────────┬─────────────┬─────────────┬─────────────┐
│   ORIGINAL  │    GRAY     │    CANNY    │    RAWY     │
├─────────────┼─────────────┼─────────────┼─────────────┤
│             │             │             │             │
│   [Color    │   [Black &  │   [White    │   [Raw      │
│    Camera   │    White    │    Edges    │    Luma     │
│    Feed]    │    Image]   │    Only]    │    Data]    │
│             │             │             │             │
│  Full RGB   │  Converted  │  Edge       │  Y-plane    │
│  channels   │  to single  │  detection  │  from       │
│             │  channel    │  algorithm  │  camera     │
└─────────────┴─────────────┴─────────────┴─────────────┘
```

---

## 📊 Build Process Visualization

```
┌──────────────────────────────────────────────────────────┐
│  1. Gradle Sync                                          │
│     ├─ Read build.gradle files                          │
│     ├─ Download dependencies                            │
│     └─ Configure CMake                                  │
└─────────────────┬────────────────────────────────────────┘
                  │
                  ▼
┌──────────────────────────────────────────────────────────┐
│  2. CMake Configuration                                  │
│     ├─ Find OpenCV (using OpenCV_DIR)                   │
│     ├─ Configure native build                           │
│     └─ Generate build files                             │
└─────────────────┬────────────────────────────────────────┘
                  │
                  ▼
┌──────────────────────────────────────────────────────────┐
│  3. Native Code Compilation                              │
│     ├─ Compile C++ sources                              │
│     ├─ Link OpenCV libraries                            │
│     └─ Create libedge_native.so                         │
└─────────────────┬────────────────────────────────────────┘
                  │
                  ▼
┌──────────────────────────────────────────────────────────┐
│  4. Kotlin/Java Compilation                              │
│     ├─ Compile Kotlin sources                           │
│     ├─ Process resources                                │
│     └─ Generate R.java                                  │
└─────────────────┬────────────────────────────────────────┘
                  │
                  ▼
┌──────────────────────────────────────────────────────────┐
│  5. Package APK                                          │
│     ├─ Combine compiled code                            │
│     ├─ Include native libraries                         │
│     ├─ Add resources                                    │
│     └─ Sign APK                                         │
└─────────────────┬────────────────────────────────────────┘
                  │
                  ▼
┌──────────────────────────────────────────────────────────┐
│  6. Install on Device                                    │
│     ├─ Transfer APK via ADB                             │
│     ├─ Install package                                  │
│     └─ Launch app                                       │
└──────────────────────────────────────────────────────────┘
```

---

## 🔍 Verification Points

```
✅ CHECKPOINT 1: OpenCV Found
   Build output shows: "OpenCV found: 4.x.x"
   ❌ If not → Check CMakeLists.txt path

✅ CHECKPOINT 2: Native Library Built
   Build output shows: "Build edge_native"
   ❌ If not → Check CMake errors

✅ CHECKPOINT 3: APK Created
   File exists: app/build/outputs/apk/debug/app-debug.apk
   ❌ If not → Check build errors

✅ CHECKPOINT 4: Device Connected
   Device appears in Android Studio dropdown
   ❌ If not → Check USB debugging

✅ CHECKPOINT 5: App Installed
   App icon appears on device
   ❌ If not → Check installation logs

✅ CHECKPOINT 6: Camera Works
   Live preview visible in app
   ❌ If not → Check camera permission

✅ CHECKPOINT 7: Export Works
   Toast message: "Exported to..."
   ❌ If not → Check storage permission

✅ CHECKPOINT 8: File Retrieved
   frame_gray_base64.txt on computer
   ❌ If not → Check device file explorer

✅ CHECKPOINT 9: Web Updated
   index.html contains base64 string
   ❌ If not → Check paste operation

✅ CHECKPOINT 10: Web Displays
   Browser shows exported image
   ❌ If not → Hard refresh (Ctrl+F5)
```

---

## 🎯 Success Indicators

### Android App
```
✅ Camera preview is smooth
✅ Mode buttons change the display
✅ Export shows toast message
✅ No crashes or errors
```

### Web Viewer
```
✅ FPS counter shows ~60
✅ Resolution matches image
✅ Exported frame is visible
✅ Not showing placeholder SVG
```

---

## 📱 Device Setup Visual

```
Settings App
│
├─ About Phone
│  └─ Build Number (tap 7 times) → Developer Mode Enabled
│
└─ System
   └─ Developer Options
      ├─ [✓] USB Debugging
      ├─ [✓] Install via USB
      └─ [✓] Stay awake
```

---

## 🚀 Quick Command Reference

```bash
# Check Node.js
node --version
npm --version

# Install web dependencies
cd web
npm install

# Run web dev server
npm run dev

# Check connected devices
adb devices

# View device logs
adb logcat | grep edge

# Pull file from device
adb pull /sdcard/Android/data/com.example.edge/files/export/frame_gray_base64.txt

# Restart ADB
adb kill-server
adb start-server
```

---

## 📖 Documentation Quick Links

| Document | Purpose |
|----------|---------|
| [README.md](README.md) | Project overview |
| [SETUP_GUIDE.md](SETUP_GUIDE.md) | Detailed instructions |
| [QUICK_START.md](QUICK_START.md) | 5-minute guide |
| [SETUP_CHECKLIST.md](SETUP_CHECKLIST.md) | Track progress |
| [TROUBLESHOOTING.md](TROUBLESHOOTING.md) | Fix problems |
| [DIRECTORY_STRUCTURE.md](DIRECTORY_STRUCTURE.md) | File locations |
| [VISUAL_GUIDE.md](VISUAL_GUIDE.md) | This file |

---

**Print this guide for quick reference! 📄**
