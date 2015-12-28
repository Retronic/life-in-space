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
import com.retronicgames.utils.IntVector2

interface DataBuildingModel : DataModel {
	/**
	 * Specifies if a character / vehicle can pass through this building (for example, the Landing Zone is passable)
	 */
	val passable: Boolean
	val size: IntVector2
}

object DataBuildingLandingZone : DataBuildingModel {
	override val passable = true
	override val id = "landingZone"
	override val size = IntVector2(3, 3)
}

object DataBuildingLivingBlock : DataBuildingModel {
	override val passable = false
	override val id = "livingBlock"
	override val size = IntVector2(2, 2)
}

object DataBuildingDigSite : DataBuildingModel {
	override val passable = false
	override val id = "digSite"
	override val size = IntVector2(3, 3)
}