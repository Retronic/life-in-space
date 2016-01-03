package com.retronicgames.lis.visual

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.MapObject
import com.retronicgames.lis.model.DataModel
import com.retronicgames.lis.model.ModelMapCell
import com.retronicgames.lis.model.Model

open class VisualMapObject<ModelType: Model<out DataModel>>(val baseCell: ModelMapCell<ModelType>, var sprite: Sprite) : MapObject(), Tintable {
	override var tint: Color?
		get() = color
		set(value) {
			color = value
		}
}