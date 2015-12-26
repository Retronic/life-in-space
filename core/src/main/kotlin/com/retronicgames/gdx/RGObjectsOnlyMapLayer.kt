package com.retronicgames.gdx

import com.badlogic.gdx.maps.MapLayer
import com.retronicgames.lis.visual.VisualMapObject

class RGObjectsOnlyMapLayer : MapLayer() {
	fun add(obj : VisualMapObject<*>) = objects.add(obj)
}