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
package com.retronicgames.lis.visual

import com.retronicgames.lis.manager.Assets
import com.retronicgames.lis.model.BaseMapCell
import com.retronicgames.utils.RGCompositeSprite
import com.retronicgames.utils.RGSpriteWrapper

class CellSelection : VisualMapCharacter(RGCompositeSprite(Assets.ninePatchSprite("ui", "selection"), RGSpriteWrapper(Assets.sprite("ui", "selectionCross")))) {
	fun selectCell(cell: BaseMapCell) {
		val compositeSprite = sprite as RGCompositeSprite

		val spriteBorder = compositeSprite.sprite1
		val spriteCross = compositeSprite.sprite2

		spriteBorder.setPosition(
				(cell.x * VisualMap.TILE_W).toFloat(),
				(cell.y * VisualMap.TILE_H).toFloat()
		)
		spriteBorder.setSize(
				(cell.w * VisualMap.TILE_W).toFloat(),
				(cell.h * VisualMap.TILE_H).toFloat()
		)

		spriteCross.setPosition(
				spriteBorder.x + spriteBorder.w * 0.5f - spriteCross.w * 0.5f,
				spriteBorder.y + spriteBorder.h * 0.5f - spriteCross.h * 0.5f
		)
	}
}
