package com.retronicgames.lis.visual.buildings

import com.retronicgames.lis.manager.Assets
import com.retronicgames.lis.model.buildings.AbstractBuilding
import com.retronicgames.lis.visual.DataVisual
import com.retronicgames.lis.visual.VisualMapObject

abstract class AbstractVisualBuilding<ModelType : AbstractBuilding<*>, VisualModelType : DataVisual>(
		x: Int,
		y: Int,
		tileW: Int,
		tileH: Int,
		val model: ModelType,
		val visualDataModel: VisualModelType) : VisualMapObject(Assets.sprite("buildings", model.data.id)) {
	init {
		sprite.setPosition((x * tileW + visualDataModel.offset.x).toFloat(), (y * tileH + visualDataModel.offset.y).toFloat())
	}
}

