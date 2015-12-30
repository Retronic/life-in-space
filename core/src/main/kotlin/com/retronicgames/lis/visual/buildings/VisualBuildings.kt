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

import com.retronicgames.lis.model.ModelMapCell
import com.retronicgames.lis.model.buildings.BuildingDigSite
import com.retronicgames.lis.model.buildings.BuildingLaboratory
import com.retronicgames.lis.model.buildings.BuildingLandingZone
import com.retronicgames.lis.model.buildings.BuildingLivingBlock
import com.retronicgames.lis.model.buildings.BuildingPowerBlock
import com.retronicgames.lis.model.buildings.BuildingSolarPanels

class VisualBuildingLandingZone(cell: ModelMapCell<BuildingLandingZone>) :
		AbstractVisualBuilding<BuildingLandingZone, DataVisualBuildingLandingZone>(cell, DataVisualBuildingLandingZone) {

}

class VisualBuildingLivingBlock(cell: ModelMapCell<BuildingLivingBlock>) :
		AbstractVisualBuilding<BuildingLivingBlock, DataVisualBuildingLivingBlock>(cell, DataVisualBuildingLivingBlock) {

}

class VisualBuildingDigSite(cell: ModelMapCell<BuildingDigSite>) :
		AbstractVisualBuilding<BuildingDigSite, DataVisualBuildingDigSite>(cell, DataVisualBuildingDigSite) {

}

class VisualBuildingPowerBlock(cell: ModelMapCell<BuildingPowerBlock>) :
		AbstractVisualBuilding<BuildingPowerBlock, DataVisualBuildingPowerBlock>(cell, DataVisualBuildingPowerBlock) {

}

class VisualBuildingSolarPanels(cell: ModelMapCell<BuildingSolarPanels>) :
		AbstractVisualBuilding<BuildingSolarPanels, DataVisualBuildingSolarPanels>(cell, DataVisualBuildingSolarPanels) {

}

class VisualBuildingLaboratory(cell: ModelMapCell<BuildingLaboratory>) :
		AbstractVisualBuilding<BuildingLaboratory, DataVisualBuildingLaboratory>(cell, DataVisualBuildingLaboratory) {

}
