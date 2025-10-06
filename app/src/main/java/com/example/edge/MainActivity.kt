package com.example.edge

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.TextureView
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.opengl.GLSurfaceView
import com.example.edge.camera.CameraController
import com.example.edge.gl.FrameRenderer

class MainActivity : AppCompatActivity() {
	private lateinit var textureView: TextureView
	private lateinit var glSurfaceView: GLSurfaceView
	private lateinit var btnToggle: Button
	private lateinit var txtFps: TextView

	private var mode: Int = 1 // 0 = Gray, 1 = Canny
	private lateinit var cameraController: CameraController
	private lateinit var renderer: FrameRenderer

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

		renderer = FrameRenderer()
		glSurfaceView.setEGLContextClientVersion(2)
		glSurfaceView.setRenderer(renderer)
		glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY

		btnToggle.setOnClickListener { mode = 1 - mode }

		ensurePermissionAndStart()
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

	private fun startCamera() {
		cameraController = CameraController(this, textureView) { nv21, w, h ->
			NativeBridge.processFrame(nv21, w, h, mode)
			val gray = NativeBridge.getLastGray()
			if (gray != null) {
				runOnUiThread { renderer.updateFrame(gray, w, h) }
			}
		}
		cameraController.start()
	}
}
