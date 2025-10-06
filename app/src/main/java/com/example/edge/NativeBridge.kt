package com.example.edge

object NativeBridge {
	@JvmStatic external fun stringFromJNI(): String

	@JvmStatic external fun processFrame(nv21Data: ByteArray, width: Int, height: Int, mode: Int)

	init {
		System.loadLibrary("edge_native")
	}
}
