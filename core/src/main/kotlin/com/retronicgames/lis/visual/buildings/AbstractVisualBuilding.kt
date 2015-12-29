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
package com.retronicgames.lis.visual.buildings

import com.retronicgames.lis.manager.Assets
import com.retronicgames.lis.model.ModelMapCell
import com.retronicgames.lis.model.buildings.AbstractBuilding
import com.retronicgames.lis.visual.DataVisual
import com.retronicgames.lis.visual.VisualMap
import com.retronicgames.lis.visual.VisualMapObject

abstract class AbstractVisualBuilding<ModelType : AbstractBuilding, VisualModelType : DataVisual>(
		cell: ModelMapCell<ModelType>,
		val visualDataModel: VisualModelType) : VisualMapObject<ModelType>(cell, Assets.sprite("buildings", cell.model.data.id)) {
	init {
		val offset = visualDataModel.offset

		val xOff = offset.x.toFloat()
		val yOff = offset.y.toFloat()

		sprite.setPosition((cell.x * VisualMap.TILE_W).toFloat() - xOff, (cell.y * VisualMap.TILE_H).toFloat() - yOff)
		sprite.setOrigin(xOff, yOff)
	}
}

