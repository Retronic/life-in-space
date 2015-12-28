/**
 * Copyright (C) 2015 Oleg Dolya
 * Copyright (C) 2015 Eduardo Garcia
 *
 * This file is part of Life in Space, by Retronic Games
 *
 * Life in Space is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Life in Space is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Life in Space.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.retronicgames.lis.visual

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileSet
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.retronicgames.gdx.RGObjectsOnlyMapLayer
import com.retronicgames.gdx.RGOrthoCachedTiledMapRenderer
import com.retronicgames.lis.manager.Assets
import com.retronicgames.lis.model.BaseMapCell
import com.retronicgames.lis.model.GameCharacterMap
import com.retronicgames.lis.model.GameMap
import com.retronicgames.lis.model.MapCell
import com.retronicgames.lis.model.buildings.Building
import com.retronicgames.lis.model.buildings.BuildingDigSite
import com.retronicgames.lis.model.buildings.BuildingLandingZone
import com.retronicgames.lis.model.buildings.BuildingLivingBlock
import com.retronicgames.lis.model.characters.CharacterSettler
import com.retronicgames.lis.model.characters.GameCharacter
import com.retronicgames.lis.visual.buildings.VisualBuildingDigSite
import com.retronicgames.lis.visual.buildings.VisualBuildingLandingZone
import com.retronicgames.lis.visual.buildings.VisualBuildingLivingBlock
import com.retronicgames.lis.visual.characters.VisualCharacterSettler
import com.retronicgames.utils.CameraListener
import com.retronicgames.utils.IntVector2
import com.retronicgames.utils.MutableIntVector2

@Suppress("NOTHING_TO_INLINE")
class VisualMap(private val map: GameMap, private val characterMap: GameCharacterMap) : CameraListener {
	companion object {
		val TILE_W = 32
		val TILE_H = 32

		fun cellToPixelPosition(cell: BaseMapCell, position: MutableIntVector2, offsetX: Float = 0.5f, offsetY: Float = 0.5f) {
			position.set(
					cell.x * TILE_W + (TILE_W * offsetX).toInt(),
					cell.y * TILE_H + (TILE_H * offsetY).toInt()
			)
		}
		fun cellCoordsToPixelPosition(cellCoords: IntVector2, position: MutableIntVector2, offsetX: Float = 0.5f, offsetY: Float = 0.5f) {
			position.set(
					cellCoords.x * TILE_W + (TILE_W * offsetX).toInt(),
					cellCoords.y * TILE_H + (TILE_H * offsetY).toInt()
			)
		}
	}

	private val visualMap = TiledMap()
	private val baseSize2idx = Array(3) { Array<com.badlogic.gdx.utils.Array<Int>?>(3) { null } }

	private val renderer = RGOrthoCachedTiledMapRenderer(visualMap)
	private val tileset = TiledMapTileSet()
	private val baseLayer = TiledMapTileLayer(map.width, map.height, TILE_W, TILE_H)
	private val buildingsLayer = RGObjectsOnlyMapLayer()
	private val effectsLayer = TiledMapTileLayer(0, 0, 0, 0)

	private var cellSelection = CellSelection()

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

		effectsLayer.objects.add(cellSelection)

		visualMap.tileSets.addTileSet(tileset)
		visualMap.layers.add(baseLayer)
		visualMap.layers.add(buildingsLayer)
		visualMap.layers.add(effectsLayer)

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
		characterMap.forEach {
			rebuildCharacter(it)
		}
		renderer.invalidateCache()
	}

	private inline fun rebuildCell(x: Int, y: Int, cell: BaseMapCell) {
		when (cell) {
			is MapCell<*> -> {
				when (cell.model) {
					is Building -> {
						createBuilding(cell as MapCell<Building>)
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

	private inline fun rebuildCharacter(character: GameCharacter<*, *>) {
		// FIXME: We should not be switching here, but caliing something that knows about the types (or make the model know about its visuals, but that's not nice...)
		val visualCharacter = when (character) {
			is CharacterSettler -> VisualCharacterSettler(character)
			else -> throw RuntimeException("Unknown character type! ($character)")
		}
		baseLayer.objects.add(visualCharacter)
	}

	private fun createBuilding(cell: MapCell<Building>) {
		val model = cell.model

		// FIXME: We should not be switching here, but caliing something that knows about the types (or make the model know about its visuals, but that's not nice...)
		buildingsLayer.add(when(cell.model) {
			is BuildingLandingZone -> VisualBuildingLandingZone(cell as MapCell<BuildingLandingZone>)
			is BuildingLivingBlock -> VisualBuildingLivingBlock(cell as MapCell<BuildingLivingBlock>)
			is BuildingDigSite -> VisualBuildingDigSite(cell as MapCell<BuildingDigSite>)
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

	fun markCell(coords: IntVector2): Boolean {
		val cell = map.cellAt(coords) ?: return false

		cellSelection.isVisible = true
		cellSelection.selectCell(cell)

		return true
	}

	fun unmarkCells() {
		cellSelection.isVisible = false
	}
}