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
package com.retronicgames.lis.visual.characters

import com.retronicgames.lis.manager.Assets
import com.retronicgames.lis.model.characters.GameCharacter
import com.retronicgames.lis.visual.DataVisual
import com.retronicgames.lis.visual.VisualMapCharacter

abstract class AbstractVisualCharacter<CharacterType : GameCharacter<*, *>, VisualModelType : DataVisual>(character: CharacterType, val visualDataModel: VisualModelType) :
		VisualMapCharacter(Assets.sprite("characters", "${character.data.id}_${character.state.value.name.toLowerCase()}", 0)) {
	init {
		val position = character.position
		val offset = visualDataModel.offset

		val xPos = position.x.toFloat()
		val yPos = position.y.toFloat()
		val xOff = offset.x.toFloat()
		val yOff = offset.y.toFloat()

		sprite.setPosition(xPos - xOff, yPos - yOff)
		sprite.setOrigin(xOff, yOff)

		character.position.onChange { oldX, oldY, newX, newY ->
			sprite.setPosition(newX - xOff, newY - yOff)
		}
	}
}