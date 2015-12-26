package com.retronicgames.lis.visual

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileSet
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.retronicgames.gdx.RGObjectsOnlyMapLayer
import com.retronicgames.gdx.RGOrthoCachedTiledMapRenderer
import com.retronicgames.lis.manager.Assets
import com.retronicgames.lis.model.BaseMapCell
import com.retronicgames.lis.model.GameMap
import com.retronicgames.lis.model.MapCell
import com.retronicgames.lis.model.buildings.Building
import com.retronicgames.lis.model.buildings.BuildingLandingZone
import com.retronicgames.lis.model.buildings.BuildingLivingBlock
import com.retronicgames.lis.visual.buildings.VisualBuildingLandingZone
import com.retronicgames.lis.visual.buildings.VisualBuildingLivingBlock
import com.retronicgames.utils.CameraListener
import com.retronicgames.utils.IntVector2

class VisualMap(private val map: GameMap, private val tileW: Int, private val tileH: Int) : CameraListener {
	private val visualMap = TiledMap()
	private val baseSize2idx = Array(3) { Array<com.badlogic.gdx.utils.Array<Int>?>(3) { null } }

	private val renderer = RGOrthoCachedTiledMapRenderer(visualMap)
	private val tileset = TiledMapTileSet()
	private val baseLayer = TiledMapTileLayer(map.width, map.height, tileW, tileH)
	private val buildingsLayer = RGObjectsOnlyMapLayer()

	init {
		val textureAtlas = Assets.textureAtlas("terrain") ?: throw RuntimeException("Cannot load terrain!")

		val tileRegex = Regex("tile(\\d)x(\\d)")
		var idx = 0
		for (region in textureAtlas.regions) {
			val match = tileRegex.matchEntire(region.name) ?: continue
			val w = match.groups[1]!!.value.toInt()
			val h = match.groups[2]!!.value.toInt()
			var list = baseSize2idx[h - 1][w - 1]
			if (list == null) {
				list = com.badlogic.gdx.utils.Array<Int>(4)
				baseSize2idx[h - 1][w - 1] = list
			}
			tileset.putTile(idx, StaticTiledMapTile(region))
			list.add(idx)

			idx++
		}

		visualMap.tileSets.addTileSet(tileset)
		visualMap.layers.add(baseLayer)
		visualMap.layers.add(buildingsLayer)

		rebuild()

		map.addOnCellListener({ x, y, cell ->
			rebuildCell(x, y, cell)
		})
	}

	override fun onCameraUpdate(cam: OrthographicCamera) {
		renderer.setView(cam)
	}

	private fun rebuild() {
		map.forEachCell(false) { x, y, row, cell ->
			rebuildCell(x, y, cell)
		}
		renderer.invalidateCache()
	}

	@Suppress("NOTHING_TO_INLINE")
	private inline fun rebuildCell(x: Int, y: Int, cell: BaseMapCell) {
		when (cell) {
			is MapCell<*> -> {
				when (cell.model) {
					is Building -> {
						createBuilding(cell as MapCell<Building<*>>)
					}
					else -> throw RuntimeException("Unknown model type! (${cell.model})")
				}
			}
			is BaseMapCell -> {
				val tileIdxs = baseSize2idx[cell.h - 1][cell.w - 1]!!
				val randomIdx = map.random(x, y, tileIdxs)
				baseLayer.setCell(x, y, VisualCell(cell, tileset.getTile(randomIdx)))
			}
			else -> throw RuntimeException("Unknown cell type! ($cell)")
		}
	}

	private fun createBuilding(cell: MapCell<Building<*>>) {
		val model = cell.model

		// FIXME: We should not be switching here, but caliing something that knows about the types (or make the model know about its visuals, but that's not nice...)
		buildingsLayer.add(when(cell.model) {
			is BuildingLandingZone -> VisualBuildingLandingZone(cell as MapCell<BuildingLandingZone>, tileW, tileH)
			is BuildingLivingBlock -> VisualBuildingLivingBlock(cell as MapCell<BuildingLivingBlock>, tileW, tileH)
			else -> throw RuntimeException("Unknown building type! ($model)")
		})
	}

	fun render() {
		renderer.render()
	}

	fun update(delta: Float) {

	}

	private fun cellAt(coords: IntVector2): VisualCell? {
		val cell = map.cellAt(coords) ?: return null
		return baseLayer.getCell(cell.x, cell.y) as VisualCell?
	}

	fun screen2cellCoords(screenX: Int, screenY: Int) = renderer.screen2cellCoords(baseLayer.width, baseLayer.height, baseLayer.tileWidth, baseLayer.tileHeight, screenX, screenY)

	/// Cell marking ///

	private var lastMarkedCell: Tintable? = null

	fun markCell(coords: IntVector2, color: Color): Boolean {
		val cell = map.cellAt(coords) ?: return false
		lastMarkedCell?.tint = null
		val visualCell = when (cell) {
			is MapCell<*> -> {
				when (cell.model) {
					is Building<*> -> { buildingsLayer.objects.first() { (it as VisualMapObject<*>).baseCell == cell } as VisualMapObject<*>? }
					else -> throw RuntimeException("Unknown model type! (${cell.model})")
				}
			}
			is BaseMapCell -> { cellAt(coords) }
			else -> throw RuntimeException("Unknown cell type! ($cell)")
		} ?: return false

		visualCell.tint = color
		lastMarkedCell = visualCell

		renderer.invalidateCache()

		return true
	}

	fun unmarkAllCells() {
		lastMarkedCell?.tint = null
		lastMarkedCell = null
	}
}