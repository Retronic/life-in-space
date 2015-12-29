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

class TaskManager {
	private val tasks = com.badlogic.gdx.utils.Array<Task>(16)
	private val taskToRemove = com.badlogic.gdx.utils.Array<Task>(false, 16)

	fun add(task: Task) {
		tasks.add(task)
	}

	fun update(delta: Float) {
		tasks.forEach {
			it.update(delta)
			if (it.finished) {
				taskToRemove.add(it)
			}
		}
		if (taskToRemove.size > 0) {
			taskToRemove.forEach { tasks.removeValue(it, true) }
			taskToRemove.clear()
		}
	}
}