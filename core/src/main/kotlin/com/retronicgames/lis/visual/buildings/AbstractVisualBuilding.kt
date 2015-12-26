package com.retronicgames.lis.visual.buildings

import com.retronicgames.lis.manager.Assets
import com.retronicgames.lis.model.MapCell
import com.retronicgames.lis.model.buildings.AbstractBuilding
import com.retronicgames.lis.visual.DataVisual
import com.retronicgames.lis.visual.VisualMapObject

abstract class AbstractVisualBuilding<ModelType : AbstractBuilding<*>, VisualModelType : DataVisual>(
		cell: MapCell<ModelType>,
		tileW: Int, tileH: Int,
		val visualDataModel: VisualModelType) : VisualMapObject<ModelType>(cell, Assets.sprite("buildings", cell.model.data.id)) {
	init {
		sprite.setPosition((cell.x * tileW + visualDataModel.offset.x).toFloat(), (cell.y * tileH + visualDataModel.offset.y).toFloat())
	}
}

