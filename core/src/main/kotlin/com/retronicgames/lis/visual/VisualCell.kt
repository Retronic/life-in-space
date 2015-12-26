package com.retronicgames.lis.visual

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.retronicgames.lis.model.BaseMapCell

class VisualCell(cell: BaseMapCell, tile: TiledMapTile) : TiledMapTileLayer.Cell(), Tintable {
	override var tint:Color? = null

	init {
		setTile(tile)
	}
}
