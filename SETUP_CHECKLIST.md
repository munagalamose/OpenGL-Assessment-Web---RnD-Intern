# ✅ Setup Checklist

Use this checklist to track your progress through the setup process.

---

## 📥 Phase 1: Prerequisites

- [ ] Android Studio installed
- [ ] Android device available
- [ ] USB cable ready
- [ ] Node.js installed (verify: `node --version`)
- [ ] OpenCV Android SDK downloaded from https://opencv.org/releases/

---

## 📂 Phase 2: OpenCV Setup

- [ ] OpenCV Android SDK extracted
- [ ] Verified path exists: `OpenCV-android-sdk/sdk/native/jni/`
- [ ] Found `OpenCVConfig.cmake` in the jni folder
- [ ] Noted the full path to the jni folder

**My OpenCV Path:**
```
_____________________________________________
```

---

## 🎯 Phase 3: Project Configuration

- [ ] Opened `flam-assignment` folder in Android Studio
- [ ] Gradle sync started automatically
- [ ] Edited `jni/CMakeLists.txt` line 9
- [ ] Set OpenCV_DIR to my path (using forward slashes `/`)
- [ ] Saved the file
- [ ] Clicked "Sync Now"
- [ ] Build output shows: "OpenCV found: X.X.X"

**If sync failed:**
- [ ] Double-checked path uses `/` not `\`
- [ ] Verified OpenCVConfig.cmake exists at that path
- [ ] Tried File → Invalidate Caches → Invalidate and Restart

---

## 📱 Phase 4: Device Setup

- [ ] Enabled Developer Mode (tapped Build Number 7 times)
- [ ] Found Developer Options in Settings
- [ ] Enabled "USB Debugging"
- [ ] Enabled "Install via USB" (if available)
- [ ] Connected device via USB
- [ ] Tapped "Allow" on USB debugging prompt
- [ ] Checked "Always allow from this computer"
- [ ] Device appears in Android Studio device dropdown

**If device not found:**
- [ ] Changed USB mode to "File Transfer (MTP)"
- [ ] Tried different USB cable
- [ ] Ran `adb devices` in terminal
- [ ] Revoked and re-allowed USB debugging

---

## 🏗️ Phase 5: Build & Run

- [ ] Selected device from dropdown
- [ ] Clicked Build → Make Project
- [ ] Build completed successfully (no errors)
- [ ] Clicked Run ▶ button
- [ ] App installed on device
- [ ] App launched automatically
- [ ] Camera preview is visible
- [ ] Camera permission granted

**If build failed:**
- [ ] Checked Build tab for error messages
- [ ] Verified NDK is installed (SDK Manager → SDK Tools → NDK)
- [ ] Tried Build → Clean Project, then rebuild
- [ ] Checked Logcat for detailed errors

---

## 📸 Phase 6: Test the App

- [ ] Camera preview showing live feed
- [ ] Tapped "Gray" button → image turned grayscale
- [ ] Tapped "Canny" button → edge detection visible
- [ ] Tapped "RawY" button → raw camera data shown
- [ ] Selected preferred mode (e.g., Canny)
- [ ] Tapped "Export" button
- [ ] Saw toast message: "Exported to..."

**If app crashed:**
- [ ] Checked Logcat for error messages
- [ ] Verified camera permission granted
- [ ] Tried uninstalling and reinstalling app
- [ ] Checked device API level ≥ 24

---

## 📤 Phase 7: Retrieve Exported Files

**Method A: Device File Explorer**
- [ ] Opened View → Tool Windows → Device File Explorer
- [ ] Navigated to `/sdcard/Android/data/com.example.edge/files/export/`
- [ ] Found `frame_gray_base64.txt`
- [ ] Right-clicked → Save As
- [ ] Saved to computer

**Method B: ADB Command**
- [ ] Opened terminal/PowerShell
- [ ] Ran: `adb pull /sdcard/Android/data/com.example.edge/files/export/frame_gray_base64.txt`
- [ ] File downloaded to current directory

**Verification:**
- [ ] Opened `frame_gray_base64.txt` in text editor
- [ ] File contains long base64 string
- [ ] String starts with valid base64 characters

---

## 🌐 Phase 8: Web Viewer Setup

- [ ] Navigated to `web` folder in terminal
- [ ] Ran `npm install` (if not done already)
- [ ] Packages installed successfully
- [ ] Opened `web/index.html` in text editor
- [ ] Found line 38 (window.sampleFrame)
- [ ] Copied entire base64 string from `frame_gray_base64.txt`
- [ ] Pasted into index.html replacing placeholder
- [ ] Added proper prefix: `"data:image/png;base64,YOUR_BASE64"`
- [ ] Saved the file

---

## 🚀 Phase 9: Run Web Viewer

**Option A: Direct File**
- [ ] Double-clicked `index.html`
- [ ] Opened in browser

**Option B: Dev Server (Recommended)**
- [ ] Opened terminal in `web` folder
- [ ] Ran: `npm run dev`
- [ ] Server started successfully
- [ ] Opened browser to http://localhost:5173

**Verification:**
- [ ] Web page loaded
- [ ] Title shows "Edge Viewer (Web)"
- [ ] FPS counter is running (showing ~60)
- [ ] Resolution is displayed (e.g., 640×360)
- [ ] **Exported frame is visible** (not placeholder)
- [ ] Image shows processed frame from Android

---

## 🎉 Phase 10: Success Verification

- [ ] Android app runs smoothly
- [ ] Can switch between processing modes
- [ ] Export creates files successfully
- [ ] Web viewer displays exported frames
- [ ] FPS counter works
- [ ] Resolution matches exported image

---

## 🔄 Optional: Test Full Workflow

- [ ] Exported frame in "Gray" mode
- [ ] Updated web viewer with Gray base64
- [ ] Verified grayscale image in browser
- [ ] Exported frame in "Canny" mode
- [ ] Updated web viewer with Canny base64
- [ ] Verified edge detection in browser
- [ ] Exported frame in "RawY" mode
- [ ] Updated web viewer with RawY base64
- [ ] Verified raw Y-plane in browser

---

## 📊 Final Status

**Date Completed:** _______________

**Android App:** ✅ Working / ❌ Issues  
**Web Viewer:** ✅ Working / ❌ Issues  
**Frame Export:** ✅ Working / ❌ Issues  
**Full Pipeline:** ✅ Working / ❌ Issues

---

## 🐛 Issues Encountered

Document any problems you faced and how you solved them:

**Issue 1:**
```
Problem: _______________________________________
Solution: ______________________________________
```

**Issue 2:**
```
Problem: _______________________________________
Solution: ______________________________________
```

**Issue 3:**
```
Problem: _______________________________________
Solution: ______________________________________
```

---

## 📝 Notes

Additional observations or customizations:

```
_______________________________________________
_______________________________________________
_______________________________________________
_______________________________________________
```

---

## 🎯 Next Steps

After completing setup:

- [ ] Experiment with different lighting conditions
- [ ] Try adjusting Canny edge detection thresholds
- [ ] Test with different camera angles
- [ ] Export multiple frames and compare
- [ ] Explore the native C++ code in `jni/src/`
- [ ] Modify OpenCV parameters
- [ ] Add custom processing modes

---

**Congratulations! 🎉**

If all items are checked, your OpenGL Edge Viewer is fully operational!

For troubleshooting, see: [SETUP_GUIDE.md](SETUP_GUIDE.md#-troubleshooting)
