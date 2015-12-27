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
import com.retronicgames.utils.IntVector2

class Mission1(private val game: LISGame) : Mission {
	private companion object {
		const val MAP_W = 30
		const val MAP_H = 30

		const val MAX_HOLES_3x3 = 60
		const val MAX_HOLES_2x2 = 240
	}

	override val map: GameMap;
	override val characterMap = GameCharacterMap()
	override val initialCameraCenter: IntVector2

	init {
		var initialCell:BaseMapCell? = null

		map = GameMap(MAP_W, MAP_H) {
			makeHoles(MAX_HOLES_3x3, 3, 3)
			makeHoles(MAX_HOLES_2x2, 2, 2)

			val landingZone = BuildingLandingZone()
			val livingBlock = BuildingLivingBlock()

			val sizeLandingZone = landingZone.data.size
			val sizeLivingBlock = livingBlock.data.size

			var landingZoneCell: BaseMapCell? = null
			var livingBlockCell: BaseMapCell? = null
			var settlerInitialCell: BaseMapCell? = null
			for (j in 0..10) {
				landingZoneCell = randomEmptyCell(sizeLandingZone.x, sizeLandingZone.y) ?: continue
				livingBlockCell = randomEmptyCellSurrounding(landingZoneCell, sizeLivingBlock.x, sizeLivingBlock.y) ?: continue
				settlerInitialCell = randomEmptyCellSurrounding(landingZoneCell, -1, -1) ?: continue

				break
			}
			if (landingZoneCell == null || livingBlockCell == null || settlerInitialCell == null) {
				game.showErrorDialog("Please rerun the game, it's been impossible to create our map")
				throw RuntimeException()
			}

			createBuilding(landingZoneCell.x, landingZoneCell.y, landingZone)
			createBuilding(livingBlockCell.x, livingBlockCell.y, livingBlock)

			characterMap.add(CharacterSettler(this, settlerInitialCell))

			initialCell = landingZoneCell
		}

		initialCameraCenter = IntVector2(initialCell!!.x, initialCell!!.y)
	}
}