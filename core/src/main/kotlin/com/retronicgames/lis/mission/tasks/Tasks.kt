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

import com.retronicgames.lis.model.buildings.Building
import com.retronicgames.lis.model.buildings.DataBuildings

interface Task {
	/**
	 * Name on the properties file will be "task.$nameI18N.name"
	 */
	val nameI18N: String

	var finished: Boolean

	fun update(delta: Float)
}

class TaskBuild(val data: DataBuildings, private val callback: (building: Building) -> Unit) : Task {
	override var finished = false

	override val nameI18N = "build"

	private var buildTime = data.buildTime

	override fun update(delta: Float) {
		buildTime -= delta

		if (buildTime <= 0) {
			finishedBuilding()
		}
	}

	private fun finishedBuilding() {
		callback(data.buildingMaker())
		finished = true
	}
}