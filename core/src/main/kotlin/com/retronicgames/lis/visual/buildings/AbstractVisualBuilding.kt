package com.retronicgames.lis.visual.buildings

import com.retronicgames.lis.manager.Assets
import com.retronicgames.lis.model.buildings.AbstractBuilding
import com.retronicgames.lis.visual.VisualMapObject
import com.retronicgames.utils.IntVector2

abstract class AbstractVisualBuilding<ModelType : AbstractBuilding>(x: Int, y: Int, tileW: Int, tileH: Int, val model: ModelType) : VisualMapObject(Assets.sprite("buildings", model.id)) {
	// This offset is part of the instance, instead of the class. Not optimal, but will do for now...
	open val offset = IntVector2.ZERO

	init {
		sprite.setPosition((x * tileW + offset.x).toFloat(), (y * tileH + offset.y).toFloat())
	}
}

