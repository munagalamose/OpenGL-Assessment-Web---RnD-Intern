# 🔧 Troubleshooting Guide

Quick solutions to common problems.

---

## 🚨 OpenCV Issues

### ❌ "OpenCV not found" during Gradle sync

**Symptoms:**
- Build output shows: `OpenCV not found. Configure OpenCV_DIR for build.`
- CMake can't find OpenCVConfig.cmake

**Solutions:**

1. **Check the path in CMakeLists.txt**
   ```cmake
   # File: jni/CMakeLists.txt (line 9)
   set(OpenCV_DIR "C:/OpenCV-android-sdk/sdk/native/jni")
   ```
   - ✅ Use forward slashes `/` even on Windows
   - ❌ Don't use backslashes `\`
   - ✅ Path should end with `/jni`

2. **Verify OpenCVConfig.cmake exists**
   ```
   OpenCV-android-sdk/
     └── sdk/
         └── native/
             └── jni/
                 └── OpenCVConfig.cmake  ← Must exist!
   ```

3. **Try absolute path**
   ```cmake
   set(OpenCV_DIR "C:/Users/DELL/OpenCV-android-sdk/sdk/native/jni")
   ```

4. **Re-sync Gradle**
   - File → Sync Project with Gradle Files
   - Or click "Sync Now" banner

5. **Clean and rebuild**
   - Build → Clean Project
   - Build → Rebuild Project

---

## 📱 Device Connection Issues

### ❌ "No device found" in Android Studio

**Symptoms:**
- Device dropdown shows "No devices"
- Can't run app

**Solutions:**

1. **Check USB debugging**
   - Settings → Developer options → USB debugging (ON)
   - Unplug and replug USB cable
   - Accept "Allow USB debugging" prompt on device

2. **Change USB mode**
   - Swipe down notification panel
   - Tap USB notification
   - Select "File Transfer (MTP)" or "PTP"

3. **Verify with ADB**
   ```bash
   adb devices
   ```
   - Should show your device
   - If "unauthorized", revoke and re-allow USB debugging
   - If not listed, try different USB cable/port

4. **Restart ADB**
   ```bash
   adb kill-server
   adb start-server
   adb devices
   ```

5. **Check drivers (Windows)**
   - Device Manager → Check for Android device
   - Update driver if needed
   - Install Google USB Driver from SDK Manager

6. **Revoke USB debugging authorization**
   - Settings → Developer options → Revoke USB debugging authorizations
   - Unplug device
   - Plug back in
   - Accept prompt again

---

## 🏗️ Build Errors

### ❌ NDK not found

**Symptoms:**
- Error: "NDK not configured"
- CMake errors about missing toolchain

**Solutions:**

1. **Install NDK**
   - Tools → SDK Manager
   - SDK Tools tab
   - Check "NDK (Side by side)"
   - Click Apply

2. **Specify NDK version**
   ```gradle
   // In app/build.gradle
   android {
       ndkVersion "25.2.9519653"  // or your installed version
   }
   ```

3. **Check NDK path**
   - File → Project Structure → SDK Location
   - Verify Android NDK location is set

---

### ❌ Gradle sync failed

**Symptoms:**
- Red errors in build.gradle files
- "Could not resolve dependencies"

**Solutions:**

1. **Check internet connection**
   - Gradle needs to download dependencies

2. **Invalidate caches**
   - File → Invalidate Caches → Invalidate and Restart

3. **Update Gradle wrapper**
   ```bash
   ./gradlew wrapper --gradle-version=8.0
   ```

4. **Check Gradle version compatibility**
   - Gradle 8.0+ requires JDK 17+
   - File → Project Structure → SDK Location → JDK location

5. **Clean Gradle cache**
   - Delete `.gradle` folder in project root
   - Sync again

---

### ❌ "Duplicate class" errors

**Symptoms:**
- Build fails with duplicate class errors
- Multiple versions of same library

**Solutions:**

1. **Clean build**
   ```
   Build → Clean Project
   Build → Rebuild Project
   ```

2. **Delete build folders**
   - Delete `app/build/`
   - Delete `app/.cxx/`
   - Sync Gradle

---

## 📱 Runtime Errors

### ❌ App crashes on launch

**Symptoms:**
- App installs but crashes immediately
- "Unfortunately, app has stopped"

**Solutions:**

1. **Check Logcat**
   - View → Tool Windows → Logcat
   - Filter by "Error" or "AndroidRuntime"
   - Look for exception stack trace

2. **Common causes:**

   **Camera permission not granted:**
   ```
   Settings → Apps → Edge Viewer → Permissions → Camera (Allow)
   ```

   **Native library not loaded:**
   - Check Build output for native build errors
   - Verify OpenCV was found during build

   **API level too low:**
   - App requires Android 7.0+ (API 24)
   - Check device Android version

3. **Reinstall app**
   - Uninstall from device
   - Build → Clean Project
   - Run again

---

### ❌ Camera preview is black

**Symptoms:**
- App launches but camera shows black screen
- No error messages

**Solutions:**

1. **Grant camera permission**
   - App should request on first launch
   - If not: Settings → Apps → Permissions → Camera

2. **Check camera in use**
   - Close other camera apps
   - Restart device

3. **Try different camera**
   - Some devices have multiple cameras
   - Check if app uses correct camera ID

---

### ❌ Export button doesn't work

**Symptoms:**
- Tap Export, nothing happens
- No toast message

**Solutions:**

1. **Check storage permission**
   - Android 11+: Settings → Apps → Edge Viewer → Permissions → Files and media

2. **Check Logcat for errors**
   - Look for permission denied errors
   - Look for file write errors

3. **Verify export path**
   - Path should be: `/sdcard/Android/data/com.example.edge/files/export/`
   - This folder is created automatically

4. **Check available storage**
   - Ensure device has free space

---

## 🌐 Web Viewer Issues

### ❌ Web page shows placeholder

**Symptoms:**
- Browser shows "Sample processed frame" SVG
- Not showing exported image

**Solutions:**

1. **Verify base64 string**
   - Open `frame_gray_base64.txt`
   - Should contain long base64 string
   - Copy entire string

2. **Check index.html**
   ```javascript
   // Line 38 should look like:
   window.sampleFrame = "data:image/png;base64,iVBORw0KGgo...";
   ```
   - Must start with `"data:image/png;base64,`
   - Must end with `";`
   - No line breaks in the base64 string

3. **Hard refresh browser**
   - Ctrl+F5 (Windows)
   - Cmd+Shift+R (Mac)
   - Clears cache

4. **Check browser console**
   - F12 → Console tab
   - Look for JavaScript errors

---

### ❌ npm command not found

**Symptoms:**
- `npm: command not found` or similar error

**Solutions:**

1. **Restart terminal/IDE**
   - Close and reopen to refresh PATH

2. **Refresh environment variables (PowerShell)**
   ```powershell
   $env:Path = [System.Environment]::GetEnvironmentVariable("Path","Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path","User")
   ```

3. **Verify Node.js installation**
   ```bash
   node --version
   npm --version
   ```

4. **Reinstall Node.js**
   - Download from https://nodejs.org/
   - Install with default options

---

### ❌ Vite dev server won't start

**Symptoms:**
- `npm run dev` fails
- Port already in use

**Solutions:**

1. **Kill existing process**
   ```bash
   # Windows
   netstat -ano | findstr :5173
   taskkill /PID <PID> /F
   
   # Mac/Linux
   lsof -ti:5173 | xargs kill -9
   ```

2. **Use different port**
   ```bash
   npm run dev -- --port 3000
   ```

3. **Reinstall dependencies**
   ```bash
   rm -rf node_modules
   npm install
   ```

---

## 🔍 Debugging Tips

### Check Build Output
```
View → Tool Windows → Build
```
- Shows CMake output
- Shows OpenCV detection
- Shows native library compilation

### Check Logcat
```
View → Tool Windows → Logcat
```
- Filter by package: `com.example.edge`
- Filter by level: Error, Warn
- Look for stack traces

### Check Device File Explorer
```
View → Tool Windows → Device File Explorer
```
- Navigate to `/sdcard/Android/data/com.example.edge/`
- Verify export folder exists
- Check file sizes

### Test ADB Connection
```bash
adb devices              # List connected devices
adb logcat              # View device logs
adb shell               # Open device shell
adb pull <path>         # Copy file from device
```

---

## 📞 Still Stuck?

### Gather Information

Before asking for help, collect:

1. **Error messages**
   - Full text from Build output
   - Logcat stack traces
   - Browser console errors

2. **Environment info**
   - Android Studio version
   - Gradle version
   - Device model and Android version
   - OpenCV SDK version

3. **What you tried**
   - List of solutions attempted
   - Results of each attempt

### Check Documentation

- [SETUP_GUIDE.md](SETUP_GUIDE.md) - Detailed setup
- [QUICK_START.md](QUICK_START.md) - Quick reference
- [DIRECTORY_STRUCTURE.md](DIRECTORY_STRUCTURE.md) - File locations

---

## ✅ Prevention Checklist

To avoid common issues:

- [ ] Use forward slashes `/` in paths
- [ ] Verify OpenCVConfig.cmake exists before building
- [ ] Always sync Gradle after changing CMakeLists.txt
- [ ] Grant all permissions when app requests
- [ ] Keep Android Studio and SDK tools updated
- [ ] Use stable versions of dependencies
- [ ] Test on device with API 24+ (Android 7.0+)
- [ ] Ensure stable USB connection
- [ ] Keep device screen unlocked during debugging

---

**Good luck! 🚀**
