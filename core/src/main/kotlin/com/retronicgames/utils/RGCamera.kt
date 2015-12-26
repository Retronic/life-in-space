package com.retronicgames.utils

import com.badlogic.gdx.graphics.OrthographicCamera

class RGCamera(worldSizeW: Int, worldSizeH: Int) {
	private val listeners = com.badlogic.gdx.utils.Array<CameraListener>(4)
	private val cam = OrthographicCamera()

	private var minX: Int = 0
	private var maxX: Int = 0
	private var minY: Int = 0
	private var maxY: Int = 0

	init {
		maxX = worldSizeW
		maxY = worldSizeH
	}

	fun addListener(listener: CameraListener) {
		listeners.add(listener)
	}

	fun resize(width: Int, height: Int) {
		cam.viewportWidth = width.toFloat()
		cam.viewportHeight = height.toFloat()
		cam.update()

		fireChange()
	}

	private fun fireChange() {
		listeners.forEach { it.onCameraUpdate(cam) }
	}

	fun translate(x: Float, y: Float): Boolean {
		val position = cam.position
		var transX = x;
		var transY = y;

		val camX = position.x
		val camY = position.y
		if ((transX < 0 && camX < minX) || (transX > 0 && camX > maxX)) {
			transX = 0f
		}
		if ((transY < 0 && camY < minY) || (transY > 0 && camY > maxY)) {
			transY = 0f
		}

		if (transX == 0f && transY == 0f) return false

		cam.translate(transX, transY)
		cam.update()

		fireChange()

		return true
	}
}

interface CameraListener {
	fun onCameraUpdate(cam: OrthographicCamera)
}