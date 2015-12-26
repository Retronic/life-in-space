package com.retronicgames.lis.model

import com.badlogic.gdx.math.MathUtils
import com.retronicgames.utils.IntVector2

class GameMap(val width: Int, val height: Int) {
	companion object {
		//		const val MAX_HOLES_4x4 = 15
		const val MAX_HOLES_3x3 = 60
		const val MAX_HOLES_2x2 = 240
	}

	private val gameMap = Array(height) { y -> Array(width) { x -> BaseMapCell(x, y) } }
	private val randomMap = Array(height) { Array(width) { Math.abs(MathUtils.random.nextInt()) } }

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

	private fun forEachInRange(cell: BaseMapCell, callback: (x: Int, y: Int, row: Array<BaseMapCell>, cell: BaseMapCell) -> Unit) {
		for (y in cell.y..cell.y + cell.h - 1) {
			val row = gameMap[y]
			for (x in cell.x..cell.x + cell.w - 1) {
				callback(x, y, row, row[x])
			}
		}
	}

	/**
	 * @param processRepeated if true, the callback will receive multiple times cells that spawn multiple locations
	 */
	fun forEachCell(processRepeated: Boolean, callback: (x: Int, y: Int, cell: BaseMapCell) -> Unit) {
		for (y in 0..height - 1) {
			val row = gameMap[y]
			for (x in 0..width - 1) {
				val cell = row[x]
				if (!processRepeated && (cell.x != x || cell.y != y)) continue
				callback(x, y, cell)
			}
		}
	}

	fun random(x: Int, y: Int) = randomMap[y][x]
	fun <T> random(x: Int, y: Int, array: com.badlogic.gdx.utils.Array<T>) = array[randomMap[y][x] % array.size]

	fun cellAt(coords: IntVector2) = gameMap.getOrNull(coords.y)?.getOrNull(coords.x)
}

open class BaseMapCell(val x: Int, val y: Int, val w: Int = 1, val h: Int = 1) {
	override fun toString() = "($x, $y) [${w}x$h]"
}

//class MapCell(type: String, x: Int, y: Int, w: Int = 1, h: Int = 1, belowCellsProducer: () -> Array<BaseMapCell>) : BaseMapCell(x, y, w, h) {
//	val belowCells = belowCellsProducer()
//}
