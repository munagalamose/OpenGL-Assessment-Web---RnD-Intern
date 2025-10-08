# ⚡ Quick Start Guide

## 🎯 5-Minute Setup

### 1️⃣ Configure OpenCV (REQUIRED)
Edit `jni/CMakeLists.txt` line 9:
```cmake
set(OpenCV_DIR "C:/OpenCV-android-sdk/sdk/native/jni")
```
Replace with YOUR actual OpenCV path!

### 2️⃣ Open in Android Studio
```
File → Open → Select: flam-assignment folder
```

### 3️⃣ Connect Android Device
- Enable Developer Mode (tap Build Number 7x)
- Enable USB Debugging
- Connect via USB
- Allow USB debugging prompt

### 4️⃣ Run App
Click **Run ▶** button → Select your device

### 5️⃣ Export Frame
- Tap mode button (Gray/Canny/RawY)
- Tap **Export**
- Files saved to: `/Android/data/com.example.edge/files/export/`

### 6️⃣ View on Web
1. Get `frame_gray_base64.txt` from device
2. Copy the base64 string
3. Edit `web/index.html` line 38:
   ```javascript
   window.sampleFrame = "data:image/png;base64,YOUR_BASE64_HERE";
   ```
4. Open in browser or run:
   ```bash
   cd web
   npm run dev
   ```

---

## 📍 Key File Locations

| What | Where |
|------|-------|
| Configure OpenCV | `jni/CMakeLists.txt` (line 9) |
| Update web frame | `web/index.html` (line 38) |
| Exported files | `/Android/data/com.example.edge/files/export/` |
| Main activity | `app/src/main/java/com/example/edge/MainActivity.kt` |
| OpenCV code | `jni/src/opencv_processor.cpp` |

---

## 🚨 Common Issues

| Problem | Solution |
|---------|----------|
| OpenCV not found | Check path in CMakeLists.txt, use `/` not `\` |
| No device found | Enable USB debugging, change to MTP mode |
| Build fails | Install NDK: Tools → SDK Manager → SDK Tools → NDK |
| App crashes | Check Logcat, grant camera permissions |
| Export fails | Grant storage permissions on device |

---

## 📖 Full Documentation
See **SETUP_GUIDE.md** for detailed step-by-step instructions.
