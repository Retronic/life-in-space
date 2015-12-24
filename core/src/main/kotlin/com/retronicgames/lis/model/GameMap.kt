package com.retronicgames.lis.model

import com.badlogic.gdx.math.MathUtils
import com.retronicgames.lis.screen.MapCell
import com.retronicgames.lis.screen.ScreenGame

class GameMap(val width: Int, val height: Int) {
	companion object {
		//		const val MAX_HOLES_4x4 = 15
		const val MAX_HOLES_3x3 = 60
		const val MAX_HOLES_2x2 = 240
	}

	private val gameMap = Array(height) { y -> Array(width) { x -> MapCell(x, y) } }
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
			val x = MathUtils.random(ScreenGame.MAP_W - 1)
			if (x > ScreenGame.MAP_W - holeW) continue
			val y = MathUtils.random(ScreenGame.MAP_H - 1)
			if (y > ScreenGame.MAP_H - holeH) continue

			var valid = true

			val newCell = MapCell(x, y, holeW, holeH)
			forEachInRange(newCell) { x, y, row, oldCell ->
				if (oldCell.w != 1 || oldCell.h != 1) valid = false
			}

			if (!valid) continue

			setCell(newCell)
		}
	}

	private fun setCell(newCell: MapCell) {
		forEachInRange(newCell) { x, y, row, cell ->
			row[x] = newCell
		}
	}

	private fun forEachInRange(cell: MapCell, callback: (x: Int, y: Int, row: Array<MapCell>, cell: MapCell) -> Unit) {
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
	fun forEachCell(processRepeated: Boolean, callback: (x: Int, y: Int, cell: MapCell) -> Unit) {
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
}