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
package com.retronicgames.lis.model.characters

import com.retronicgames.lis.model.BaseMapCell
import com.retronicgames.lis.model.GameMap
import com.retronicgames.lis.visual.VisualMap
import com.retronicgames.utils.IntVector2
import com.retronicgames.utils.MutableIntVector2
import com.retronicgames.utils.RecyclableArray
import com.retronicgames.utils.value.MutableValue

abstract class AbstractCharacter<DataType : DataCharacterModel, StateType : Enum<StateType>>(
		private val map: GameMap,
		initialCell: BaseMapCell,
		override val data: DataType,
		val stateIdle: StateType, val stateWalking: StateType) : GameCharacter<DataType, StateType> {
	override val position: IntVector2
	override val currentCell = MutableValue(initialCell)
	var target:BaseMapCell? = null
		private set

	override val visible = MutableValue(true)

	private var currentPath: RecyclableArray<IntVector2>? = null
	private var currentPathIdx = 0
	private val currentTarget = MutableIntVector2()

	init {
		// FIXME: Not sure if it's a good idea having the pixel position in the model
		val cell = currentCell.value
		position = MutableIntVector2()
		VisualMap.cellToPixelPosition(cell, position as MutableIntVector2)
	}

	override fun moveTo(cell: BaseMapCell?): Boolean {
		if (cell == null) return false
		if (state.value != stateIdle) return false

		(state as MutableValue<StateType>).value = stateWalking
		target = cell

		resetPath()

		val currentCell = currentCell.value
		val path = map.pathFinding.findPath(currentCell.x, currentCell.y, cell.x, cell.y)
		currentPath = path

		val isValidPath = path.size > 0
		val isCurrentCell = cell == currentCell

		if (isCurrentCell) {
			// A bit messy... This is also inside nextPathStep()
			VisualMap.cellCoordsToPixelPosition(IntVector2(cell.x, cell.y), currentTarget)
		} else if (isValidPath) {
			nextPathStep(0f)
		} else {
			setInvalidState()
		}

		return isCurrentCell || isValidPath
	}

	private fun resetPath() {
		currentPath?.dispose()
		currentPath = null
		currentPathIdx = -1
		currentTarget.set(Int.MIN_VALUE, Int.MIN_VALUE)
	}

	override fun update(delta: Float) {
		if (target == null) return

		doMove(delta)
	}

	private fun doMove(delta: Float) {
		val deltaX = (currentTarget.x - position.x).toDouble();
		val deltaY = (currentTarget.y - position.y).toDouble();
		val distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

		if (distance <= 0.0001f) {
			nextPathStep(delta)
			return
		}

		val speedTime = data.speed * delta
		if (speedTime > distance) {
			(position as MutableIntVector2).set(currentTarget.x, currentTarget.y);
		} else {
			var offsetX = (deltaX / distance) * speedTime;
			var offsetY = (deltaY / distance) * speedTime;

			if (offsetX > 0 && offsetX < 1) { offsetX = 1.0 }
			if (offsetY > 0 && offsetY < 1) { offsetY = 1.0 }

			(position as MutableIntVector2).set((position.x + offsetX).toInt(), (position.y + offsetY).toInt());
		}
	}

	private fun nextPathStep(delta: Float) {
		val path = currentPath!!

		if (currentPathIdx >= 0) currentCell.value = map.cellAt(path[currentPathIdx])!!
		if (currentPathIdx >= path.size - 1) {
			resetPath()
			fireTargetReached()
			return
		}

		val coords = path[++currentPathIdx]
		VisualMap.cellCoordsToPixelPosition(coords, currentTarget)

		doMove(delta)
	}

	private fun fireTargetReached() {
		(state as MutableValue<StateType>).value = stateIdle
		target = null

		callbacks.forEach { it() }
		callbacks.clear()
	}

	private fun setInvalidState() {
		(state as MutableValue<StateType>).value = stateIdle
		currentPath = null
		target = null

		callbacks.clear()
	}

	private val callbacks = com.badlogic.gdx.utils.Array<() -> Unit>(false, 4)

	/**
	 * Listener will be removed after we reach our target (or there was no path to it)!
	 */
	fun onReached(callback: () -> Unit) {
		callbacks.add(callback)
	}
}