package com.example.edge.gl

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class FrameRenderer : GLSurfaceView.Renderer {
	private var program = 0
	private var textureId = 0
	private var frameWidth = 0
	private var frameHeight = 0
	@Volatile private var latestFrame: ByteArray? = null

	private lateinit var vertexBuffer: FloatBuffer
	private lateinit var texBuffer: FloatBuffer

	private val vertexShader = """
		attribute vec4 aPos;
		attribute vec2 aTex;
		varying vec2 vTex;
		void main(){
			gl_Position = aPos;
			vTex = aTex;
		}
	""".trimIndent()

	private val fragmentShader = """
		precision mediump float;
		varying vec2 vTex;
		uniform sampler2D uTex;
		void main(){
			float g = texture2D(uTex, vTex).r;
			gl_FragColor = vec4(vec3(g), 1.0);
		}
	""".trimIndent()

	fun updateFrame(bytes: ByteArray, width: Int, height: Int) {
		latestFrame = bytes
		frameWidth = width
		frameHeight = height
	}

	override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
		GLES20.glClearColor(0.07f, 0.08f, 0.10f, 1f)
		program = buildProgram(vertexShader, fragmentShader)
		textureId = createTexture()
		val vertices = floatArrayOf(
			-1f, -1f,
			 1f, -1f,
			-1f,  1f,
			 1f,  1f
		)
		val tex = floatArrayOf(
			0f, 1f,
			1f, 1f,
			0f, 0f,
			1f, 0f
		)
		vertexBuffer = FloatBuffer.allocate(vertices.size).put(vertices)
		vertexBuffer.position(0)
		texBuffer = FloatBuffer.allocate(tex.size).put(tex)
		texBuffer.position(0)
	}

	override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
		GLES20.glViewport(0, 0, width, height)
	}

	override fun onDrawFrame(gl: GL10?) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
		val frame = latestFrame
		if (frame != null && frameWidth > 0 && frameHeight > 0) {
			GLES20.glUseProgram(program)
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
			// Upload grayscale as RED channel (ES 3 would use GL_R8; for ES2, many devices accept GL_LUMINANCE)
			GLES20.glTexImage2D(
				GLES20.GL_TEXTURE_2D, 0,
				GLES20.GL_LUMINANCE,
				frameWidth, frameHeight, 0,
				GLES20.GL_LUMINANCE,
				GLES20.GL_UNSIGNED_BYTE,
				ByteBuffer.allocateDirect(frame.size).order(ByteOrder.nativeOrder()).put(frame).apply { position(0) }
			)
			val aPos = GLES20.glGetAttribLocation(program, "aPos")
			val aTex = GLES20.glGetAttribLocation(program, "aTex")
			GLES20.glEnableVertexAttribArray(aPos)
			GLES20.glEnableVertexAttribArray(aTex)
			GLES20.glVertexAttribPointer(aPos, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer)
			GLES20.glVertexAttribPointer(aTex, 2, GLES20.GL_FLOAT, false, 0, texBuffer)
			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
			GLES20.glDisableVertexAttribArray(aPos)
			GLES20.glDisableVertexAttribArray(aTex)
		}
	}

	private fun buildProgram(vs: String, fs: String): Int {
		fun compile(type: Int, src: String): Int {
			val shader = GLES20.glCreateShader(type)
			GLES20.glShaderSource(shader, src)
			GLES20.glCompileShader(shader)
			return shader
		}
		val v = compile(GLES20.GL_VERTEX_SHADER, vs)
		val f = compile(GLES20.GL_FRAGMENT_SHADER, fs)
		val p = GLES20.glCreateProgram()
		GLES20.glAttachShader(p, v)
		GLES20.glAttachShader(p, f)
		GLES20.glLinkProgram(p)
		return p
	}

	private fun createTexture(): Int {
		val ids = IntArray(1)
		GLES20.glGenTextures(1, ids, 0)
		val id = ids[0]
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id)
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
		return id
	}
}
