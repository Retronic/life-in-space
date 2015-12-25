package com.retronicgames.lis.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.retronicgames.lis.model.GameMap
import com.retronicgames.lis.visual.VisualMap
import com.retronicgames.utils.IntVector2
import com.retronicgames.utils.MutableIntVector2
import com.retronicgames.utils.RGCamera

object ScreenGame : ScreenAdapter() {
	const val MAP_W = 30
	const val MAP_H = 30

	private val TILE_W = 32;
	private val TILE_H = 32;

	private val map = GameMap(MAP_W, MAP_H)
	private val visualMap = VisualMap(MAP_W, MAP_H, TILE_W, TILE_H)
	private val cam = RGCamera(MAP_W * TILE_W, MAP_H * TILE_H)

	private val inputProcessor = object : InputAdapter() {
		private var startDrag = MutableIntVector2(-1, -1)
		private var isDragging = false

		override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
			isDragging = false
			startDrag.set(screenX, screenY)

			return true
		}

		override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
			if (startDrag < IntVector2.ZERO) return false

			isDragging = true
			cam.translate((startDrag.x - screenX).toFloat(), (screenY - startDrag.y).toFloat())
			startDrag.set(screenX, screenY)

			return true
		}

		override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
			isDragging = false
			startDrag.set(-1, -1)

			return true
		}
	}

	init {
		visualMap.rebuild(map)

		cam.addListener(visualMap)

		Gdx.input.inputProcessor = inputProcessor
	}

	override fun resize(width: Int, height: Int) {
		cam.resize(width, height)
	}

	override fun render(delta: Float) {
		Gdx.gl.glClearColor(0xcb / 255f, 0xc4 / 255f, 0xac / 255f, 1f)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

		visualMap.render()
	}
}

class MapCell(val x: Int, val y: Int, val w: Int = 1, val h: Int = 1) {
	override fun toString() = "($x, $y) [${w}x$h]"
}
