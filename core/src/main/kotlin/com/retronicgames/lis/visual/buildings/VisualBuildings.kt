package com.retronicgames.lis.visual.buildings

import com.retronicgames.lis.model.MapCell
import com.retronicgames.lis.model.buildings.BuildingLandingZone
import com.retronicgames.lis.model.buildings.BuildingLivingBlock

class VisualBuildingLandingZone(cell: MapCell<BuildingLandingZone>, tileW: Int, tileH: Int) :
		AbstractVisualBuilding<BuildingLandingZone, DataVisualBuildingLandingZone>(cell, tileW, tileH, DataVisualBuildingLandingZone) {

}

class VisualBuildingLivingBlock(cell: MapCell<BuildingLivingBlock>, tileW: Int, tileH: Int) :
		AbstractVisualBuilding<BuildingLivingBlock, DataVisualBuildingLivingBlock>(cell, tileW, tileH, DataVisualBuildingLivingBlock) {

}