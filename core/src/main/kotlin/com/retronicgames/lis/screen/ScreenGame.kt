package com.retronicgames.lis.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.retronicgames.lis.model.GameMap
import com.retronicgames.lis.ui.LISGUI
import com.retronicgames.lis.visual.VisualMap
import com.retronicgames.utils.IntVector2
import com.retronicgames.utils.MutableIntVector2
import com.retronicgames.utils.RGCamera

object ScreenGame : ScreenAdapter() {
	const val MAP_W = 30
	const val MAP_H = 30

	const val DRAG_THRESHOLD = 10

	private val TILE_W = 32;
	private val TILE_H = 32;

	private val map = GameMap(MAP_W, MAP_H)
	private val visualMap = VisualMap(map, MAP_W, MAP_H, TILE_W, TILE_H)
	private val cam = RGCamera(MAP_W * TILE_W, MAP_H * TILE_H)

	private val gui = LISGUI()

	private val inputProcessor = object : InputAdapter() {
		private var startTouch = MutableIntVector2(-1, -1)
		private var endTouch = MutableIntVector2(-1, -1)
		private var isDragging = false

		override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
			isDragging = false
			startTouch.set(screenX, screenY)

			return true
		}

		override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
			if (startTouch < IntVector2.ZERO) return false

			if (!isDragging) {
				if (Math.abs(startTouch.x - screenX) > DRAG_THRESHOLD || Math.abs(startTouch.y - screenY) > DRAG_THRESHOLD) {
					endTouch.set(screenX, screenY)
					isDragging = true
				}
			}
			if (isDragging) {
				cam.translate((endTouch.x - screenX).toFloat(), (screenY - endTouch.y).toFloat())
				endTouch.set(screenX, screenY)
			}

			return true
		}

		override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
			if (!isDragging) {
				performAction(screenX, screenY)
			}
			isDragging = false
			startTouch.set(-1, -1)

			return true
		}
	}

	init {
		cam.addListener(visualMap)

		Gdx.input.inputProcessor = InputMultiplexer(gui.inputProcessor(), inputProcessor)
	}

	private fun performAction(screenX: Int, screenY: Int) {
		val coords = visualMap.screen2cellCoords(screenX, screenY)
		visualMap.markCell(coords, Color.SALMON)
		gui.showModal("dialog.build.text")
	}

	override fun resize(width: Int, height: Int) {
		cam.resize(width, height)

		gui.resize(width, height)
	}

	override fun render(delta: Float) {
		update(delta)

		render()
	}

	@Suppress("NOTHING_TO_INLINE")
	private inline fun update(delta: Float) {
		visualMap.update(delta)
		gui.update(delta)
	}

	@Suppress("NOTHING_TO_INLINE")
	private inline fun render() {
		Gdx.gl.glClearColor(0xcb / 255f, 0xc4 / 255f, 0xac / 255f, 1f)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

		visualMap.render()
		gui.render()
	}
}

class MapCell(val x: Int, val y: Int, val w: Int = 1, val h: Int = 1) {
	override fun toString() = "($x, $y) [${w}x$h]"
}
