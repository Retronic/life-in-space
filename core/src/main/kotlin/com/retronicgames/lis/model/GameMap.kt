package com.retronicgames.lis.model

import com.badlogic.gdx.math.MathUtils
import com.retronicgames.lis.model.buildings.Building
import com.retronicgames.utils.IntVector2

class GameMap(val width: Int, val height: Int) {
	companion object {
		//		const val MAX_HOLES_4x4 = 15
		const val MAX_HOLES_3x3 = 60
		const val MAX_HOLES_2x2 = 240
	}

	private val gameMap = Array(height) { y -> Array(width) { x -> BaseMapCell(x, y) } }
	private val randomMap = Array(height) { Array(width) { Math.abs(MathUtils.random.nextInt()) } }

	@Transient
	private val tempCellArray = com.badlogic.gdx.utils.Array<BaseMapCell>(16)

	@Transient
	private val cellChangeListeners = com.badlogic.gdx.utils.Array<(x: Int, y: Int, cell: BaseMapCell) -> Unit>(2)

	init {
		//		makeHoles(MAX_HOLES_4x4, 4, 4)
		makeHoles(MAX_HOLES_3x3, 3, 3)
		makeHoles(MAX_HOLES_2x2, 2, 2)
	}

	/**
	 * Makes between 0 and [maxCount] (so it can be less than expected)
	 */
	private fun makeHoles(maxCount: Int, holeW: Int, holeH: Int) {
		for (j in 0..maxCount - 1) {
			val x = MathUtils.random(width - 1)
			if (x > width - holeW) continue
			val y = MathUtils.random(height - 1)
			if (y > height - holeH) continue

			var valid = true

			val newCell = BaseMapCell(x, y, holeW, holeH)
			forEachInRange(newCell) { x, y, row, oldCell ->
				if (oldCell.w != 1 || oldCell.h != 1) valid = false
			}

			if (!valid) continue

			setCell(newCell)
		}
	}

	private fun setCell(newCell: BaseMapCell) {
		forEachInRange(newCell) { x, y, row, cell ->
			row[x] = newCell
		}
	}

	private fun forEachInRange(x: Int, y: Int, w: Int, h: Int, callback: (x: Int, y: Int, row: Array<BaseMapCell>, cell: BaseMapCell) -> Unit) {
		for (newY in y..y + h - 1) {
			val row = gameMap[newY]
			for (newX in x..x + w - 1) {
				callback(newX, newY, row, row[newX])
			}
		}
	}

	private fun forEachInRange(cell: BaseMapCell, callback: (x: Int, y: Int, row: Array<BaseMapCell>, cell: BaseMapCell) -> Unit) {
		return forEachInRange(cell.x, cell.y, cell.w, cell.h, callback)
	}

	/**
	 * @param processRepeated if true, the callback will receive multiple times cells that spawn multiple locations
	 */
	fun forEachCell(processRepeated: Boolean, callback: (x: Int, y: Int, row: Array<BaseMapCell>, cell: BaseMapCell) -> Unit) {
		for (y in 0..height - 1) {
			val row = gameMap[y]
			for (x in 0..width - 1) {
				val cell = row[x]
				if (!processRepeated && (cell.x != x || cell.y != y)) continue
				callback(x, y, row, cell)
			}
		}
	}

	fun createBuilding(x: Int, y: Int, model: Building<out DataModel>) = addTopCell(x, y, model)

	private fun <ModelType : Model<out DataModel>> addTopCell(x: Int, y: Int, model: ModelType): Boolean {
		val cell = cellAt(x, y) ?: return false

		val size = model.data.size
		if (cell.w != size.x || cell.h != size.y) return false

		val newCell = MapCell(model, x, y, size.x, size.y, cell)

		forEachInRange(newCell) { x, y, row, cell ->
			row[x] = newCell
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

	fun cellAt(x:Int, y:Int) = gameMap.getOrNull(y)?.getOrNull(x)
	fun cellAt(coords: IntVector2) = cellAt(coords.x, coords.y)

	fun randomEmptyCell(w: Int, h: Int): BaseMapCell {
		tempCellArray.clear()
		forEachCell(false) { x, y, row, cell ->
			if (cell.w == w && cell.h == h) {
				tempCellArray.add(cell)
			}
		}

		return tempCellArray.random()
	}

	fun addOnCellListener(listener: (x: Int, y: Int, cell: BaseMapCell) -> Unit) {
		cellChangeListeners.add(listener)
	}
}

open class BaseMapCell(val x: Int, val y: Int, val w: Int = 1, val h: Int = 1) {
	override fun toString() = "($x, $y) [${w}x$h]"
}

class MapCell<ModelType : Model<out DataModel>>(val model: ModelType, x: Int, y: Int, w: Int = 1, h: Int = 1, val belowCell: BaseMapCell) : BaseMapCell(x, y, w, h) {
}
