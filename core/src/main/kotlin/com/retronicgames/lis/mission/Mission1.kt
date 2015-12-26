package com.retronicgames.lis.mission

import com.retronicgames.lis.LISGame
import com.retronicgames.lis.model.BaseMapCell
import com.retronicgames.lis.model.GameMap
import com.retronicgames.lis.model.buildings.BuildingLandingZone
import com.retronicgames.lis.model.buildings.BuildingLivingBlock

class Mission1(private val game: LISGame) : Mission {
	private companion object {
		const val MAP_W = 30
		const val MAP_H = 30
	}

	override val map = GameMap(MAP_W, MAP_H)

	init {
		val landingZone = BuildingLandingZone()
		val livingBlock = BuildingLivingBlock()

		val sizeLandingZone = landingZone.data.size
		val sizeLivingBlock = livingBlock.data.size

		var landingZoneCell: BaseMapCell? = null
		var livingBlockCell: BaseMapCell? = null
		for (j in 0..10) {
			landingZoneCell = map.randomEmptyCell(sizeLandingZone.x, sizeLandingZone.y)
			if (landingZoneCell != null) {
				livingBlockCell = map.randomEmptyCellSurrounding(landingZoneCell, sizeLivingBlock.x, sizeLivingBlock.y)
				if (livingBlockCell != null) {
					break
				}
			}
		}
		if (landingZoneCell == null || livingBlockCell == null) {
			game.showErrorDialog("Please rerun the game, it's been impossible to create our map")
			throw RuntimeException()
		}

		map.createBuilding(landingZoneCell.x, landingZoneCell.y, landingZone)
		map.createBuilding(livingBlockCell.x, livingBlockCell.y, livingBlock)
	}
}