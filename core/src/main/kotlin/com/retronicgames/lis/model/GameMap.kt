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
package com.retronicgames.lis.model

import com.badlogic.gdx.math.RandomXS128
import com.retronicgames.gdx.from
import com.retronicgames.lis.model.buildings.Building
import com.retronicgames.lis.model.buildings.ConstructionSite
import com.retronicgames.lis.model.map.PathFinding
import com.retronicgames.utils.IntVector2

class GameMap(val seed: Long, val width: Int, val height: Int, initializer: GameMap.() -> Unit) {
	private val random = RandomXS128(seed)

	private val gameMap = Array(height) { y -> Array(width) { x -> BaseMapCell(x, y) } }
	private val randomMap = Array(height) { Array(width) { Math.abs(random.nextInt()) } }

	@Transient
	private val tempCellArray = com.badlogic.gdx.utils.Array<BaseMapCell>(16)

	@Transient
	private val cellChangeListeners = com.badlogic.gdx.utils.Array<(x: Int, y: Int, cell: BaseMapCell) -> Unit>(2)

	@Transient
	val pathFinding: PathFinding;

	init {
		initializer()

		pathFinding = PathFinding(this)
	}

	fun update(delta: Float) {
		// Do nothing... for now
	}

	/**
	 * Makes between 0 and [maxCount] (so it can be less than expected)
	 */
	fun makeHoles(maxCount: Int, holeW: Int, holeH: Int) {
		for (j in 0..maxCount - 1) {
			val x = random.nextInt(width - holeW)
			val y = random.nextInt(height - holeH)

			var valid = true

			val newCell = BaseMapCell(x, y, holeW, holeH)
			forEachInRange(true, newCell) { x, y, oldCell ->
				if (oldCell.w != 1 || oldCell.h != 1) {
                    valid = false
                    return@forEachInRange
                }
			}

			if (!valid) continue

			setCell(newCell)
		}
	}

	private fun setCell(newCell: BaseMapCell) {
		forEachInRange(true, newCell) { x, y, cell ->
			gameMap[y][x] = newCell
		}
	}

	/**
	 * @param processRepeated if true, the callback will receive multiple times cells that spawn multiple locations.
	 * It also means that if the starting coordinate points to "half" a cell (i.e. is not the bottom left coordinate for that cell), said cell won't be returned!!
	 */
	private fun forEachInRange(processRepeated: Boolean, x: Int, y: Int, w: Int, h: Int, callback: (x: Int, y: Int, cell: BaseMapCell) -> Unit) {
		val x1 = Math.max(0, x)
		val y1 = Math.max(0, y)
		val x2 = Math.min(width - 1, x + w - 1)
		val y2 = Math.min(height - 1, y + h - 1)

		for (newY in y1..y2) {
			val row = gameMap[newY]
			for (newX in x1..x2) {
				val cell = row[newX]
				if (!processRepeated && (cell.x != newX || cell.y != newY)) continue
				callback(newX, newY, cell)
			}
		}
	}

	private fun forEachInRange(processRepeated: Boolean, cell: BaseMapCell, callback: (x: Int, y: Int, cell: BaseMapCell) -> Unit) {
		return forEachInRange(processRepeated, cell.x, cell.y, cell.w, cell.h, callback)
	}

	/**
	 * @param processRepeated if true, the callback will receive multiple times cells that spawn multiple locations.
	 */
	fun forEachCell(processRepeated: Boolean, callback: (x: Int, y: Int, cell: BaseMapCell) -> Unit) =
			forEachInRange(processRepeated, 0, 0, width, height, callback)

	fun createConstructionSite(x: Int, y: Int): Boolean {
		val cell = cellAt(x, y) ?: return false
		val size = IntVector2(cell.w, cell.h)
		return addTopCell(x, y, size, { size, baseCell ->
			ModelMapCell(ConstructionSite(size), baseCell.x, baseCell.y, size.x, size.y, baseCell)
		})
	}

	fun createBuilding(x: Int, y: Int, model: Building) = addTopCell(x, y, model.data.size, { size, baseCell ->
		ModelMapCell(model, baseCell.x, baseCell.y, size.x, size.y, baseCell)
	})

	private fun addTopCell(x: Int, y: Int, size: IntVector2, cellCreator: (size: IntVector2, baseCell: BaseMapCell) -> BaseMapCell): Boolean {
		val cell = cellAt(x, y) ?: return false

		if (cell.w != size.x || cell.h != size.y) return false

		val newCell = cellCreator(size, cell)

		forEachInRange(true, newCell) { x, y, cell ->
			gameMap[y][x] = newCell
		}
		fireCellChange(x, y, newCell)

		return true
	}

	private fun fireCellChange(x: Int, y: Int, cell: BaseMapCell) {
		cellChangeListeners.forEach {
			it(x, y, cell)
		}
	}

	fun random(x: Int, y: Int) = randomMap[y][x]
	fun <T> random(x: Int, y: Int, array: com.badlogic.gdx.utils.Array<T>) = array[randomMap[y][x] % array.size]

	fun cellAt(x: Int, y: Int) = gameMap.getOrNull(y)?.getOrNull(x)
	fun cellAt(coords: IntVector2) = cellAt(coords.x, coords.y)

	fun randomEmptyCell(w: Int, h: Int): BaseMapCell? {
		tempCellArray.clear()
		forEachCell(false) { x, y, cell ->
			if (cell.w == w && cell.h == h && cell.javaClass == BaseMapCell::class.java) {
				tempCellArray.add(cell)
			}
		}

		return random.from(tempCellArray)
	}

	fun randomEmptyCellSurrounding(surroundedCell: BaseMapCell, targetW: Int, targetH: Int): BaseMapCell? {
		tempCellArray.clear()

		val x = surroundedCell.x
		val y = surroundedCell.y
		val w = surroundedCell.w
		val h = surroundedCell.h
		val callback = { x: Int, y: Int, cell: BaseMapCell ->
			if (cell.javaClass == BaseMapCell::class.java &&
					(targetW <= 0 || targetH <= 0 || (cell.w == targetW && cell.h == targetH) && !tempCellArray.contains(cell, true))
			) {
				tempCellArray.add(cell)
			}
		}
		forEachInRange(true, x - 1, y - 1, w + 2, 1, callback)
		forEachInRange(true, x - 1, y + h, w + 2, 1, callback)

		forEachInRange(true, x - 1, y - 1, 1, h + 2, callback)
		forEachInRange(true, x + w, y - 1, 1, h + 2, callback)

		return random.from(tempCellArray)
	}

	fun addOnCellListener(listener: (x: Int, y: Int, cell: BaseMapCell) -> Unit) {
		cellChangeListeners.add(listener)
	}
}

open class BaseMapCell(val x: Int, val y: Int, val w: Int = 1, val h: Int = 1) {
	override fun toString() = "($x, $y) [${w}x$h]"
}

class ModelMapCell<ModelType : Model<out DataModel>>(val model: ModelType, x: Int, y: Int, w: Int = 1, h: Int = 1, val belowCell: BaseMapCell) : BaseMapCell(x, y, w, h) {
}
