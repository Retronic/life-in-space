package com.retronicgames.lis.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.retronicgames.lis.model.GameMap
import com.retronicgames.lis.visual.VisualMap

object ScreenGame : ScreenAdapter() {
	const val MAP_W = 30
	const val MAP_H = 30

	private val TILE_W = 32;
	private val TILE_H = 32;

	private val map = GameMap(MAP_W, MAP_H)
	private val visualMap = VisualMap(MAP_W, MAP_H, TILE_W, TILE_H)
	private val cam = OrthographicCamera()

	init {
		visualMap.rebuild(map)
	}

	override fun resize(width: Int, height: Int) {
		cam.setToOrtho(false, width.toFloat(), height.toFloat())
		visualMap.cameraUpdate(cam);
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
