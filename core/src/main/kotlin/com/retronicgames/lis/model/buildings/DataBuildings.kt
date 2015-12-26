package com.retronicgames.lis.model.buildings

import com.retronicgames.lis.model.DataModel
import com.retronicgames.utils.IntVector2

object DataBuildingLandingZone: DataModel {
	override val id = "landingZone"
	override val size = IntVector2(3, 3)
}

object DataBuildingLivingBlock : DataModel {
	override val id = "livingBlock"
	override val size = IntVector2(2, 2)
}