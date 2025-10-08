package com.example.edge

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.view.TextureView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.opengl.GLSurfaceView
import com.example.edge.camera.CameraController
import com.example.edge.gl.FrameRenderer
import java.io.File

class MainActivity : AppCompatActivity() {
	private lateinit var textureView: TextureView
	private lateinit var glSurfaceView: GLSurfaceView
	private lateinit var btnToggle: Button
	private lateinit var txtFps: TextView
	private lateinit var btnExport: Button

	private var mode: Int = 1 // 0 = Gray, 1 = Canny, 2 = RawY
	private lateinit var cameraController: CameraController
	private lateinit var renderer: FrameRenderer

	private var frames = 0
	private var lastTime = 0L

	private val cameraPermission = registerForActivityResult(
		ActivityResultContracts.RequestPermission()
	) { granted ->
		if (granted) startCamera() else finish()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		textureView = findViewById(R.id.textureView)
		glSurfaceView = findViewById(R.id.glSurfaceView)
		btnToggle = findViewById(R.id.btnToggle)
		txtFps = findViewById(R.id.txtFps)
		btnExport = findViewById(R.id.btnExport)

		renderer = FrameRenderer()
		glSurfaceView.setEGLContextClientVersion(2)
		glSurfaceView.setRenderer(renderer)
		glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY

		updateToggleText()
		btnToggle.setOnClickListener {
			mode = (mode + 1) % 3
			updateToggleText()
		}

		btnExport.setOnClickListener {
			requestExport = true
		}

		ensurePermissionAndStart()
	}

	private fun updateToggleText() {
		val label = when (mode) { 2 -> "RawY"; 1 -> "Canny"; else -> "Gray" }
		btnToggle.text = label
	}

	override fun onDestroy() {
		super.onDestroy()
		cameraController.stop()
	}

	private fun ensurePermissionAndStart() {
		when (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
			PackageManager.PERMISSION_GRANTED -> startCamera()
			else -> cameraPermission.launch(Manifest.permission.CAMERA)
		}
	}

	@Volatile private var requestExport = false

	private fun startCamera() {
		lastTime = System.nanoTime()
		cameraController = CameraController(this, textureView) { nv21, w, h ->
			when (mode) {
				2 -> { // Raw Y plane
					val ySize = w * h
					val gray = if (nv21.size >= ySize) nv21.copyOfRange(0, ySize) else nv21
					runOnUiThread {
						renderer.updateFrame(gray, w, h)
						updateFps()
					}
				}
				else -> {
					NativeBridge.processFrame(nv21, w, h, mode)
					val gray = NativeBridge.getLastGray()
					if (gray != null) {
						runOnUiThread {
							renderer.updateFrame(gray, w, h)
							updateFps()
							maybeExportBase64(gray, w, h)
						}
					}
				}
			}
		}
		cameraController.start()
	}

	private fun updateFps() {
		frames++
		val now = System.nanoTime()
		if (now - lastTime >= 1_000_000_000L) {
			val fps = frames
			txtFps.text = "FPS: $fps"
			frames = 0
			lastTime = now
		}
	}

	private fun maybeExportBase64(gray: ByteArray, w: Int, h: Int) {
		if (!requestExport) return
		requestExport = false
		// Export gray to a simple PGM and also base64 PNG placeholder (PGM easier without extra libs)
		try {
			val dir = File(getExternalFilesDir(null), "export")
			dir.mkdirs()
			val pgm = File(dir, "frame_gray.pgm")
			pgm.outputStream().use { os ->
				val header = "P5\n$w $h\n255\n".toByteArray()
				os.write(header)
				os.write(gray)
			}
			// Also write base64 file so web can copy/paste quickly
			val b64 = Base64.encodeToString(gray, Base64.NO_WRAP)
			File(dir, "frame_gray_base64.txt").writeText(b64)
			Toast.makeText(this, "Exported to ${dir.absolutePath}", Toast.LENGTH_LONG).show()
		} catch (e: Throwable) {
			Toast.makeText(this, "Export failed: ${e.message}", Toast.LENGTH_SHORT).show()
		}
	}
}
