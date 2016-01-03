package com.retronicgames.lis.visual.buildings

import com.retronicgames.lis.manager.Assets
import com.retronicgames.lis.model.ModelMapCell
import com.retronicgames.lis.model.buildings.ConstructionSite
import com.retronicgames.lis.model.buildings.DataConstructionSite
import com.retronicgames.lis.visual.VisualMap
import com.retronicgames.lis.visual.VisualMapObject

@Suppress("NOTHING_TO_INLINE")
class VisualConstructionSite(cell : ModelMapCell<ConstructionSite>) : VisualMapObject<ConstructionSite>(cell, Assets.sprite("buildings", getSpriteId(cell.model.data))) {
	private companion object {
		inline fun getSpriteId(data: DataConstructionSite) = "${data.id}${data.size.x}x${data.size.y}"
	}

	init {
		sprite.setPosition((cell.x * VisualMap.TILE_W).toFloat(), (cell.y * VisualMap.TILE_H).toFloat())
	}
}

