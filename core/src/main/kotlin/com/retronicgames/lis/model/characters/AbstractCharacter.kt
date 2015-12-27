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
import com.retronicgames.lis.model.DataModel
import com.retronicgames.lis.model.GameMap
import com.retronicgames.lis.visual.VisualMap
import com.retronicgames.utils.IntVector2
import com.retronicgames.utils.MutableIntVector2

abstract class AbstractCharacter<DataType : DataModel, StateType : Enum<StateType>>(
		private val map: GameMap,
		initialCell: BaseMapCell,
		override val data: DataType) : GameCharacter<DataType, StateType> {
	override val position: IntVector2
	override var currentCell = initialCell

	init {
		// FIXME: Not sure if it's a good idea having the pixel position in the model
		position = MutableIntVector2(
				currentCell.x * VisualMap.TILE_W + VisualMap.TILE_W / 2,
				currentCell.y * VisualMap.TILE_H + VisualMap.TILE_H / 2
		)
	}

	override fun moveTo(cell: BaseMapCell?): Boolean {
		if (cell == null) return false

		val path = map.pathFinding.findPath(currentCell.x, currentCell.y, cell.x, cell.y)
		println(path)
		return path.size > 0
	}
}