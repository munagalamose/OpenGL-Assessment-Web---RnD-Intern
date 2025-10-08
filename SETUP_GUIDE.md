# 🚀 Complete Setup Guide - OpenGL Edge Viewer

This guide will walk you through setting up the Android OpenCV project and viewing frames on the web interface.

---

## 📋 Prerequisites

Before starting, ensure you have:

- ✅ **Android Studio** (latest version recommended)
- ✅ **Android device** with USB debugging enabled
- ✅ **OpenCV Android SDK** downloaded
- ✅ **Node.js** installed (for web viewer)

---

## 🧩 Part 1: Download OpenCV Android SDK

### Step 1.1: Download OpenCV
1. Go to: https://opencv.org/releases/
2. Download **OpenCV Android SDK** (e.g., `opencv-4.10.0-android-sdk.zip`)
3. Extract it to a location you can remember

**Recommended locations:**
- Windows: `C:\OpenCV-android-sdk`
- Mac/Linux: `/Users/YourName/OpenCV-android-sdk`

### Step 1.2: Verify OpenCV Structure
After extraction, verify this folder exists:
```
OpenCV-android-sdk/
  └── sdk/
      └── native/
          └── jni/
              ├── abi-armeabi-v7a/
              ├── abi-arm64-v8a/
              └── OpenCVConfig.cmake  ← This file must exist!
```

---

## 🎯 Part 2: Open Project in Android Studio

### Step 2.1: Open the Project
1. Launch **Android Studio**
2. Click **"Open"** (or File → Open)
3. Navigate to and select: `c:\Users\DELL\Desktop\flam-assignment`
4. Click **OK**

### Step 2.2: Wait for Gradle Sync
- Android Studio will automatically start syncing Gradle
- Wait for it to complete (check bottom status bar)
- If it doesn't auto-sync, click: **File → Sync Project with Gradle Files**

---

## ⚙️ Part 3: Configure OpenCV Path

### Step 3.1: Edit CMakeLists.txt
1. In Android Studio, navigate to: `jni/CMakeLists.txt`
2. Find line 9 (currently commented out):
   ```cmake
   # set(OpenCV_DIR "")
   ```

3. **Uncomment and set your OpenCV path:**

   **For Windows:**
   ```cmake
   set(OpenCV_DIR "C:/OpenCV-android-sdk/sdk/native/jni")
   ```

   **For Mac:**
   ```cmake
   set(OpenCV_DIR "/Users/YourName/OpenCV-android-sdk/sdk/native/jni")
   ```

   **For Linux:**
   ```cmake
   set(OpenCV_DIR "/home/username/OpenCV-android-sdk/sdk/native/jni")
   ```

   ⚠️ **Important:** Use forward slashes `/` even on Windows!

### Step 3.2: Save and Sync
1. Save the file (Ctrl+S / Cmd+S)
2. Click **"Sync Now"** when the yellow bar appears at the top
3. Wait for sync to complete

### Step 3.3: Verify OpenCV is Found
Check the **Build** tab at the bottom:
- ✅ You should see: `OpenCV found: 4.x.x`
- ❌ If you see: `OpenCV not found`, double-check your path

---

## 📱 Part 4: Prepare Your Android Device

### Step 4.1: Enable Developer Mode
1. Go to **Settings → About phone**
2. Find **Build number**
3. Tap it **7 times** rapidly
4. You'll see: "You are now a developer!"

### Step 4.2: Enable USB Debugging
1. Go to **Settings → System → Developer options**
2. Enable **USB Debugging**
3. Enable **Install via USB** (if available)

### Step 4.3: Connect Device
1. Connect your phone to computer via USB cable
2. On your phone, tap **"Allow USB debugging"** when prompted
3. Check **"Always allow from this computer"**
4. Tap **OK**

### Step 4.4: Verify Connection in Android Studio
1. Look at the top toolbar in Android Studio
2. Click the device dropdown (next to the Run button ▶)
3. Your device should appear in the list
4. If not, try:
   - Unplugging and replugging the USB cable
   - Changing USB mode to "File Transfer (MTP)"
   - Revoking and re-allowing USB debugging

---

## 🏗️ Part 5: Build and Run the App

### Step 5.1: Select Build Variant (Optional)
1. Click **View → Tool Windows → Build Variants**
2. Select **debug** (recommended for testing)

### Step 5.2: Build the Project
1. Click **Build → Make Project** (or Ctrl+F9 / Cmd+F9)
2. Wait for build to complete
3. Check for errors in the **Build** tab

### Step 5.3: Run on Device
1. Click the **Run ▶** button (or Shift+F10)
2. Select your connected device
3. Click **OK**

Android Studio will:
- ✅ Build the native OpenCV library
- ✅ Package the APK
- ✅ Install on your device
- ✅ Launch the app automatically

---

## 📸 Part 6: Using the App

### App Interface
When the app launches, you'll see:
- **Camera preview** showing live feed
- **Toggle buttons** at the bottom:
  - **Gray**: Grayscale conversion
  - **Canny**: Edge detection (Canny algorithm)
  - **RawY**: Raw camera Y-plane
- **Export button**: Saves current frame

### Step 6.1: Test Different Modes
1. Tap **Gray** → See grayscale image
2. Tap **Canny** → See edge detection
3. Tap **RawY** → See raw camera data

### Step 6.2: Export a Frame
1. Select the mode you want (e.g., **Canny**)
2. Tap **Export** button
3. You'll see a toast: "Exported to /Android/data/..."

### Step 6.3: Find Exported Files
Files are saved to:
```
/Android/data/com.example.edge/files/export/
```

Exported files:
- `frame_gray.pgm` - Raw image file
- `frame_gray_base64.txt` - Base64 encoded (for web)

### Step 6.4: Retrieve Files from Device
**Option A: Using Android Studio**
1. Click **View → Tool Windows → Device File Explorer**
2. Navigate to: `/sdcard/Android/data/com.example.edge/files/export/`
3. Right-click `frame_gray_base64.txt`
4. Click **Save As...**
5. Save to your computer

**Option B: Using ADB**
```bash
adb pull /sdcard/Android/data/com.example.edge/files/export/frame_gray_base64.txt
```

---

## 🌐 Part 7: View Frame on Web Interface

### Step 7.1: Copy Base64 Data
1. Open `frame_gray_base64.txt` in a text editor
2. Select all text (Ctrl+A / Cmd+A)
3. Copy (Ctrl+C / Cmd+C)

### Step 7.2: Update Web Page
1. Navigate to: `c:\Users\DELL\Desktop\flam-assignment\web\`
2. Open `index.html` in a text editor
3. Find line 38 (inside the `<script>` tag):
   ```javascript
   window.sampleFrame = window.sampleFrame ||
   ```
4. Replace it with:
   ```javascript
   window.sampleFrame = "data:image/png;base64,PASTE_YOUR_BASE64_HERE";
   ```
   (Replace `PASTE_YOUR_BASE64_HERE` with the copied base64 string)

### Step 7.3: View in Browser
**Option A: Direct File (Simple)**
1. Double-click `index.html`
2. It will open in your default browser

**Option B: Vite Dev Server (Recommended)**
1. Open PowerShell/Terminal
2. Navigate to web folder:
   ```bash
   cd c:\Users\DELL\Desktop\flam-assignment\web
   ```
3. Run dev server:
   ```bash
   npm run dev
   ```
4. Open browser to: http://localhost:5173

### Step 7.4: Verify Display
You should now see:
- ✅ Your exported frame displayed
- ✅ FPS counter running
- ✅ Resolution showing actual image dimensions

---

## 🐛 Troubleshooting

### Problem: "OpenCV not found"
**Solution:**
1. Verify OpenCV path in `jni/CMakeLists.txt`
2. Check that `OpenCVConfig.cmake` exists at that path
3. Use forward slashes `/` in the path
4. Re-sync Gradle

### Problem: "No device found"
**Solution:**
1. Check USB cable (try a different one)
2. Enable "File Transfer (MTP)" mode on phone
3. Revoke USB debugging authorization and re-allow
4. Try: `adb devices` in terminal to verify connection

### Problem: Build fails with NDK errors
**Solution:**
1. Go to **File → Project Structure → SDK Location**
2. Ensure Android NDK is installed
3. If not, go to **Tools → SDK Manager → SDK Tools**
4. Check **NDK (Side by side)** and install

### Problem: App crashes on launch
**Solution:**
1. Check **Logcat** in Android Studio for errors
2. Verify device API level ≥ 24 (Android 7.0+)
3. Try uninstalling and reinstalling the app
4. Check camera permissions are granted

### Problem: Export button doesn't work
**Solution:**
1. Grant storage permissions to the app
2. Check **Logcat** for permission errors
3. On Android 11+, ensure app has file access permissions

### Problem: Web page shows placeholder
**Solution:**
1. Verify you copied the entire base64 string
2. Check that the string starts with `data:image/png;base64,`
3. Ensure no extra spaces or line breaks in the base64 data
4. Try refreshing the browser (Ctrl+F5)

---

## 📁 Project Structure Reference

```
flam-assignment/
├── app/                          # Android app module
│   ├── build.gradle             # App-level Gradle config
│   └── src/
│       └── main/
│           ├── java/com/example/edge/
│           │   ├── MainActivity.kt          # Main activity
│           │   ├── NativeBridge.kt         # JNI bridge
│           │   ├── camera/
│           │   │   └── CameraController.kt # Camera handling
│           │   └── gl/
│           │       └── FrameRenderer.kt    # OpenGL rendering
│           ├── res/                        # Resources
│           └── AndroidManifest.xml
│
├── jni/                          # Native C++ code
│   ├── CMakeLists.txt           # ← CONFIGURE OPENCV HERE
│   └── src/
│       ├── native_bridge.cpp    # JNI implementation
│       └── opencv_processor.cpp # OpenCV processing
│
├── web/                          # Web viewer
│   ├── index.html               # ← UPDATE BASE64 HERE
│   ├── package.json
│   └── src/
│       └── main.ts
│
├── build.gradle                  # Root Gradle config
└── settings.gradle
```

---

## ✅ Success Checklist

Before asking for help, verify:

- [ ] OpenCV Android SDK downloaded and extracted
- [ ] OpenCV path configured in `jni/CMakeLists.txt`
- [ ] Gradle sync completed successfully
- [ ] "OpenCV found" message in build output
- [ ] Android device connected and visible in Android Studio
- [ ] USB debugging enabled and authorized
- [ ] App builds without errors
- [ ] App installs and launches on device
- [ ] Camera preview is visible
- [ ] Toggle buttons change the display
- [ ] Export button creates files
- [ ] Base64 file retrieved from device
- [ ] Web page updated with base64 data
- [ ] Web browser displays the exported frame

---

## 🎉 Next Steps

Once everything is working:

1. **Experiment with different modes** (Gray, Canny, RawY)
2. **Try different lighting conditions** for edge detection
3. **Export multiple frames** and compare results
4. **Modify OpenCV parameters** in `opencv_processor.cpp`
5. **Add new processing modes** by extending the native code

---

## 📞 Need Help?

If you encounter issues:

1. Check the **Troubleshooting** section above
2. Review **Logcat** output in Android Studio
3. Verify all steps in the **Success Checklist**
4. Check that your OpenCV SDK version is compatible (4.x recommended)

---

**Happy coding! 🚀**
