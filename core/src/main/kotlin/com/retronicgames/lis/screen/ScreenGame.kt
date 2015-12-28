/**
 * Copyright (C) 2015 Oleg Dolya
 * Copyright (C) 2015 Eduardo Garcia
 *
 * This file is part of Life in Space, by Retronic Games
 *
 * Life in Space is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Life in Space is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Life in Space.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.retronicgames.lis.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.graphics.GL20
import com.retronicgames.lis.mission.Mission
import com.retronicgames.lis.model.buildings.DataBuildings
import com.retronicgames.lis.ui.LISGUI
import com.retronicgames.lis.visual.VisualMap
import com.retronicgames.utils.IntVector2
import com.retronicgames.utils.MutableIntVector2
import com.retronicgames.utils.RGCamera

class ScreenGame(val mission: Mission) : ScreenAdapter() {
	private companion object {
		const val DRAG_THRESHOLD = 10
	}

	private val visualMap = VisualMap(mission.map, mission.characterMap)
	private val cam = RGCamera(mission.map.width * VisualMap.TILE_W, mission.map.height * VisualMap.TILE_H)

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
		cam.translate((mission.initialCameraCenter.x * VisualMap.TILE_W).toFloat(), (mission.initialCameraCenter.y * VisualMap.TILE_H).toFloat())

		Gdx.input.inputProcessor = InputMultiplexer(gui.inputProcessor(), inputProcessor)
	}

	private fun performAction(screenX: Int, screenY: Int) {
		val coords = visualMap.screen2cellCoords(screenX, screenY)
		visualMap.markCell(coords)
		gui.showList("dialog.build.title", *DataBuildings.values()) { success ->
			visualMap.unmarkCells()
		}
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
		GdxAI.getTimepiece().update(delta);

		mission.update(delta)
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
