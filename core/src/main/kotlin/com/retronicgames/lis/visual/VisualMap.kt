package com.retronicgames.lis.visual

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileSet
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.retronicgames.gdx.RGOrthoCachedTiledMapRenderer
import com.retronicgames.lis.manager.Assets
import com.retronicgames.lis.model.GameMap
import com.retronicgames.utils.CameraListener

class VisualMap(width: Int, height: Int, tileW: Int, tileH: Int) : CameraListener {
	private val visualMap = TiledMap()
	private val size2idx = Array(3) { Array<com.badlogic.gdx.utils.Array<Int>?>(3) { null } }

	private val renderer = RGOrthoCachedTiledMapRenderer(visualMap)
	private val tileset = TiledMapTileSet()
	private val layer = TiledMapTileLayer(width, height, tileW, tileH)

	init {
		val textureAtlas = Assets.textureAtlas("terrain") ?: throw RuntimeException("Cannot load terrain!")

		val tileRegex = Regex("tile(\\d)x(\\d)")
		var idx = 0
		for (region in textureAtlas.regions) {
			val match = tileRegex.matchEntire(region.name) ?: continue
			val w = match.groups[1]!!.value.toInt()
			val h = match.groups[2]!!.value.toInt()
			var list = size2idx[h - 1][w - 1]
			if (list == null) {
				list = com.badlogic.gdx.utils.Array<Int>(4)
				size2idx[h - 1][w - 1] = list
			}
			tileset.putTile(idx, StaticTiledMapTile(region))
			list.add(idx)

			idx++
		}

		visualMap.tileSets.addTileSet(tileset)
		visualMap.layers.add(layer)
	}

	override fun onCameraUpdate(cam: OrthographicCamera) {
		renderer.setView(cam)
	}

	fun rebuild(map: GameMap) {
		map.forEachCell(false) { x, y, cell ->
			val tileIdxs = size2idx[cell.h - 1][cell.w - 1]!!
			val randomIdx = map.random(x, y, tileIdxs)
			layer.setCell(x, y, VisualCell(cell, tileset.getTile(randomIdx)))
		}
		renderer.invalidateCache()
	}

	fun render() {
		renderer.render()
	}

	fun update(delta: Float) {

	}
}