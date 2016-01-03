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
package com.retronicgames.lis.mission.tasks

import com.retronicgames.lis.mission.Mission
import com.retronicgames.lis.model.BaseMapCell
import com.retronicgames.lis.model.GameMap
import com.retronicgames.lis.model.ModelMapCell
import com.retronicgames.lis.model.buildings.Building
import com.retronicgames.lis.model.buildings.ConstructionSite
import com.retronicgames.lis.model.buildings.DataBuildings
import com.retronicgames.lis.model.characters.CharacterSettler
import com.retronicgames.lis.model.characters.StateSettler

interface Task {
	/**
	 * Name on the properties file will be "task.$nameI18N.name"
	 */
	val nameI18N: String

	var finished: Boolean

	fun update(delta: Float)
}

class TaskBuild(val mission: Mission, val x: Int, val y: Int, val data: DataBuildings, private val callback: (x: Int, y: Int, building: Building) -> Unit) : Task {
	private enum class TaskState {
		SEARCHING_FOR_SETTLER, WAITING_FOR_SETTLER, PROCESSING, FINISHED
	}

	private val buildCell: ModelMapCell<ConstructionSite>?
	private var state = TaskState.SEARCHING_FOR_SETTLER
	private var settler: CharacterSettler? = null

	override var finished = false

	override val nameI18N = "build"

	private var buildTime = data.buildTime

	init {
		val map = mission.map

		buildCell = createConstructionSite(map)
	}

	private fun createConstructionSite(map: GameMap): ModelMapCell<ConstructionSite>? {
		val buildCell = map.cellAt(x, y)
		if (buildCell != null && buildCell.javaClass == BaseMapCell::class.java) {
			val createdSite = map.createConstructionSite(x, y)
			if (createdSite) {
				return map.cellAt(x, y) as ModelMapCell<ConstructionSite>
			}
		}

		markAsFinished()
		return null
	}

	override fun update(delta: Float) {
		when (state) {
			TaskState.SEARCHING_FOR_SETTLER -> {
				val allIdle = mission.characterMap.filter { it is CharacterSettler && it.state.value == it.stateIdle }
				if (allIdle.isEmpty()) return

				// FIXME: Sort by distance first
				val settler = allIdle.first() as CharacterSettler
				this.settler = settler

				state = TaskState.WAITING_FOR_SETTLER

				settler.moveTo(buildCell)
				settler.onReached {
					buildCell!!.model.started.value = true
					state = TaskState.PROCESSING;
					settler.state.value = StateSettler.WORKING;
					settler.visible.value = false
				}
			}

			TaskState.PROCESSING -> {
				buildTime -= delta

				if (buildTime <= 0) {
					finishedBuilding()
				}
			}
		}
	}

	private fun finishedBuilding() {
		markAsFinished()

		callback(x, y, data.buildingMaker())

		val settler = settler!!
		settler.visible.value = true
		settler.state.value = StateSettler.IDLE
		// FIXME: We need to find the closest empty cell, this can fail when the cell is surrounded!
		val randomCell = mission.map.randomEmptyCellSurrounding(buildCell!!, -1, -1) ?: throw RuntimeException("FIXME! Can't find an empty cell surrounding the recently constructed cell")
		settler.moveTo(randomCell)
	}

	private fun markAsFinished() {
		state = TaskState.FINISHED
		finished = true
	}
}