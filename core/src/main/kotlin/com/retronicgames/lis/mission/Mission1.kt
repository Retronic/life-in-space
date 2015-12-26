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

import com.retronicgames.lis.LISGame
import com.retronicgames.lis.model.BaseMapCell
import com.retronicgames.lis.model.GameCharacterMap
import com.retronicgames.lis.model.GameMap
import com.retronicgames.lis.model.buildings.BuildingLandingZone
import com.retronicgames.lis.model.buildings.BuildingLivingBlock
import com.retronicgames.lis.model.characters.CharacterSettler
import com.retronicgames.lis.visual.VisualMap
import com.retronicgames.utils.IntVector2

class Mission1(private val game: LISGame) : Mission {
	private companion object {
		const val MAP_W = 30
		const val MAP_H = 30
	}

	override val map = GameMap(MAP_W, MAP_H)
	override val characterMap = GameCharacterMap()
	override val initialCameraCenter: IntVector2

	init {
		val landingZone = BuildingLandingZone()
		val livingBlock = BuildingLivingBlock()

		val sizeLandingZone = landingZone.data.size
		val sizeLivingBlock = livingBlock.data.size

		var landingZoneCell: BaseMapCell? = null
		var livingBlockCell: BaseMapCell? = null
		var settlerInitialCell: BaseMapCell? = null
		for (j in 0..10) {
			landingZoneCell = map.randomEmptyCell(sizeLandingZone.x, sizeLandingZone.y) ?: continue
			livingBlockCell = map.randomEmptyCellSurrounding(landingZoneCell, sizeLivingBlock.x, sizeLivingBlock.y) ?: continue
			settlerInitialCell = map.randomEmptyCellSurrounding(landingZoneCell, -1, -1) ?: continue

			break
		}
		if (landingZoneCell == null || livingBlockCell == null || settlerInitialCell == null) {
			game.showErrorDialog("Please rerun the game, it's been impossible to create our map")
			throw RuntimeException()
		}

		map.createBuilding(landingZoneCell.x, landingZoneCell.y, landingZone)
		map.createBuilding(livingBlockCell.x, livingBlockCell.y, livingBlock)

		initialCameraCenter = IntVector2(landingZoneCell.x, landingZoneCell.y)

		characterMap.add(CharacterSettler(
				settlerInitialCell.x * VisualMap.TILE_W + VisualMap.TILE_W / 2,
				settlerInitialCell.y * VisualMap.TILE_H + VisualMap.TILE_H / 2
		))
	}
}