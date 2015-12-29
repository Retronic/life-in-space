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
package com.retronicgames.lis.model.buildings

import com.retronicgames.lis.model.DataModel
import com.retronicgames.lis.ui.LISGUI
import com.retronicgames.utils.IntVector2

interface DataBuildingModel : DataModel {
	/**
	 * Specifies if a character / vehicle can pass through this building (for example, the Landing Zone is passable)
	 */
	val passable: Boolean
	val size: IntVector2

	/**
	 * In seconds
	 */
	val buildTime: Float
}

enum class DataBuildings(
		override val id: String,
		override val passable: Boolean,
		override val size: IntVector2, override val buildTime: Float,
		val buildingMaker: () -> Building
) : DataBuildingModel {
	LANDING_ZONE("landingZone", true, IntVector2(3, 3), 3f, ::BuildingLandingZone),
	LIVING_BLOCK("livingBlock", false, IntVector2(2, 2), 3f, ::BuildingLivingBlock),
	DIG_SITE("digSite", false, IntVector2(3, 3), 3f, ::BuildingDigSite),
	POWER_BLOCK("powerBlock", false, IntVector2(1, 1), 3f, ::BuildingPowerBlock),
	SOLAR_PANELS("solarPanels", false, IntVector2(3, 3), 3f, ::BuildingSolarPanels);

	override fun toString() = LISGUI.i18n.get("name.$id")
}
