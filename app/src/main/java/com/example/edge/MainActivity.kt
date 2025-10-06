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

class MainActivity : AppCompatActivity() {
	private lateinit var textureView: TextureView
	private lateinit var glSurfaceView: GLSurfaceView
	private lateinit var btnToggle: Button
	private lateinit var txtFps: TextView

	private var mode: Int = 1 // 0 = Gray, 1 = Canny

	private val cameraPermission = registerForActivityResult(
		ActivityResultContracts.RequestPermission()
	) { granted ->
		if (granted) startCamera()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		textureView = findViewById(R.id.textureView)
		glSurfaceView = findViewById(R.id.glSurfaceView)
		btnToggle = findViewById(R.id.btnToggle)
		txtFps = findViewById(R.id.txtFps)

		glSurfaceView.setEGLContextClientVersion(2)
		glSurfaceView.setRenderer(com.example.edge.gl.Renderer())
		glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY

		btnToggle.setOnClickListener {
			mode = 1 - mode
		}

		ensurePermissionAndStart()
	}

	private fun ensurePermissionAndStart() {
		when (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
			PackageManager.PERMISSION_GRANTED -> startCamera()
			else -> cameraPermission.launch(Manifest.permission.CAMERA)
		}
	}

	private fun startCamera() {
		// TODO: Implement Camera2 capture to feed NV21 frames into NativeBridge.processFrame
	}
}
