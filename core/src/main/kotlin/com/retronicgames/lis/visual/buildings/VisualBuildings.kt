package com.retronicgames.lis.visual.buildings

import com.retronicgames.lis.model.buildings.BuildingLandingZone
import com.retronicgames.lis.model.buildings.BuildingLivingBlock

class VisualBuildingLandingZone(x: Int, y: Int, tileW: Int, tileH: Int, model: BuildingLandingZone) :
		AbstractVisualBuilding<BuildingLandingZone, DataVisualBuildingLandingZone>(x, y, tileW, tileH, model, DataVisualBuildingLandingZone) {

}

class VisualBuildingLivingBlock(x: Int, y: Int, tileW: Int, tileH: Int, model: BuildingLivingBlock) :
		AbstractVisualBuilding<BuildingLivingBlock, DataVisualBuildingLivingBlock>(x, y, tileW, tileH, model, DataVisualBuildingLivingBlock) {

}