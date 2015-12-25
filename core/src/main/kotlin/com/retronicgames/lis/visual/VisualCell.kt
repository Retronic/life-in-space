package com.retronicgames.lis.visual

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.retronicgames.lis.screen.MapCell

class VisualCell(cell: MapCell, tile: TiledMapTile) : TiledMapTileLayer.Cell() {
	var color: Color? = null

	init {
		setTile(tile)
	}
}
