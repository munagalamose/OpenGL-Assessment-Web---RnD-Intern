package com.example.edge.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.view.Surface
import android.view.TextureView
import java.nio.ByteBuffer

class CameraController(
	private val context: Context,
	private val textureView: TextureView,
	private val onFrameNV21: (ByteArray, Int, Int) -> Unit
) {
	private var cameraDevice: CameraDevice? = null
	private var captureSession: CameraCaptureSession? = null
	private var imageReader: ImageReader? = null
	private var backgroundThread: HandlerThread? = null
	private var backgroundHandler: Handler? = null

	@SuppressLint("MissingPermission")
	fun start() {
		startBackgroundThread()
		val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
		val cameraId = manager.cameraIdList.firstOrNull { id ->
			val chars = manager.getCameraCharacteristics(id)
			val facing = chars.get(CameraCharacteristics.LENS_FACING)
			facing == CameraCharacteristics.LENS_FACING_BACK
		} ?: manager.cameraIdList.first()

		manager.openCamera(cameraId, object : CameraDevice.StateCallback() {
			override fun onOpened(device: CameraDevice) {
				cameraDevice = device
				createSession(device)
			}
			override fun onDisconnected(device: CameraDevice) { device.close() }
			override fun onError(device: CameraDevice, error: Int) { device.close() }
		}, backgroundHandler)
	}

	fun stop() {
		captureSession?.close(); captureSession = null
		imageReader?.close(); imageReader = null
		cameraDevice?.close(); cameraDevice = null
		stopBackgroundThread()
	}

	private fun createSession(device: CameraDevice) {
		if (!textureView.isAvailable) {
			textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
				override fun onSurfaceTextureAvailable(st: android.graphics.SurfaceTexture, w: Int, h: Int) { createSession(device) }
				override fun onSurfaceTextureSizeChanged(st: android.graphics.SurfaceTexture, w: Int, h: Int) {}
				override fun onSurfaceTextureDestroyed(st: android.graphics.SurfaceTexture): Boolean = true
				override fun onSurfaceTextureUpdated(st: android.graphics.SurfaceTexture) {}
			}
			return
		}

		val previewSize = Size(textureView.width.coerceAtLeast(640), textureView.height.coerceAtLeast(480))
		textureView.surfaceTexture?.setDefaultBufferSize(previewSize.width, previewSize.height)
		val previewSurface = Surface(textureView.surfaceTexture)

		imageReader = ImageReader.newInstance(previewSize.width, previewSize.height, ImageFormat.YUV_420_888, 2)
		imageReader?.setOnImageAvailableListener({ reader ->
			val image = reader.acquireLatestImage() ?: return@setOnImageAvailableListener
			try {
				val nv21 = yuv420ToNV21(image)
				onFrameNV21(nv21, image.width, image.height)
			} finally {
				image.close()
			}
		}, backgroundHandler)

		device.createCaptureSession(listOf(previewSurface, imageReader!!.surface), object : CameraCaptureSession.StateCallback() {
			override fun onConfigured(session: CameraCaptureSession) {
				captureSession = session
				val request = device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
					addTarget(previewSurface)
					addTarget(imageReader!!.surface)
					set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
				}
				session.setRepeatingRequest(request.build(), null, backgroundHandler)
			}
			override fun onConfigureFailed(session: CameraCaptureSession) {}
		}, backgroundHandler)
	}

	private fun startBackgroundThread() {
		backgroundThread = HandlerThread("CameraBackground").also { it.start() }
		backgroundHandler = Handler(backgroundThread!!.looper)
	}

	private fun stopBackgroundThread() {
		backgroundThread?.quitSafely()
		try { backgroundThread?.join() } catch (_: InterruptedException) {}
		backgroundThread = null
		backgroundHandler = null
	}

	private fun yuv420ToNV21(image: Image): ByteArray {
		val yPlane = image.planes[0]
		val uPlane = image.planes[1]
		val vPlane = image.planes[2]

		val ySize = yPlane.buffer.remaining()
		val uSize = uPlane.buffer.remaining()
		val vSize = vPlane.buffer.remaining()

		// NV21: Y followed by interleaved VU (i.e., V then U)
		val nv21 = ByteArray(ySize + uSize + vSize)
		yPlane.buffer.get(nv21, 0, ySize)

		val chromaRowStride = uPlane.rowStride
		val chromaPixelStride = uPlane.pixelStride
		val width = image.width
		val height = image.height
		var offset = ySize

		val uBuffer = uPlane.buffer
		uBuffer.rewind()
		val vBuffer = vPlane.buffer
		vBuffer.rewind()

		val rowCount = height / 2
		val colCount = width / 2
		val vRow = ByteArray(chromaRowStride)
		val uRow = ByteArray(chromaRowStride)
		for (row in 0 until rowCount) {
			vBuffer.get(vRow, 0, chromaRowStride)
			uBuffer.get(uRow, 0, chromaRowStride)
			var col = 0
			while (col < colCount) {
				val vuIndex = col * chromaPixelStride
				nv21[offset++] = vRow[vuIndex]
				nv21[offset++] = uRow[vuIndex]
				col++
			}
		}
		return nv21
	}
}
