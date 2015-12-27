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
package com.retronicgames.lis.mission

import com.retronicgames.lis.model.GameCharacterMap
import com.retronicgames.lis.model.GameMap
import com.retronicgames.utils.IntVector2

abstract class Mission {
	abstract val map: GameMap
	abstract val initialCameraCenter: IntVector2

	val characterMap = GameCharacterMap()

	fun update(delta: Float) {
		map.update(delta)
		characterMap.update(delta)
	}
}