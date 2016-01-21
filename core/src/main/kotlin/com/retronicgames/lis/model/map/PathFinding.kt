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
package com.retronicgames.lis.model.map

import com.badlogic.gdx.ai.pfa.*
import com.badlogic.gdx.ai.pfa.indexed.DefaultIndexedGraph
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder
import com.badlogic.gdx.ai.pfa.indexed.IndexedNode
import com.badlogic.gdx.utils.Pool
import com.retronicgames.lis.model.BaseMapCell
import com.retronicgames.lis.model.GameMap
import com.retronicgames.lis.model.ModelMapCell
import com.retronicgames.lis.model.buildings.Building
import com.retronicgames.lis.model.buildings.ConstructionSite
import com.retronicgames.utils.IntVector2
import com.retronicgames.utils.MutableIntVector2
import com.retronicgames.utils.RecyclableArray

class PathFinding(private val map: GameMap) {
	@Transient
	private val intVector2Pool = object : Pool<IntVector2>(32) {
		override fun newObject() = MutableIntVector2(-1, -1)
	}

	private val outGraph = DefaultGraphPath<Connection<PathFindingCell>>(100)

	private val passableFlatNodes = com.badlogic.gdx.utils.Array<PathFindingCell>(map.width * map.height)
	private val passableNodes = Array(map.height) { y -> Array (map.width) { x -> val newValue = PathFindingCell(x, y); passableFlatNodes.add(newValue); newValue } }

	private object TiledManhattanDistance : Heuristic<PathFindingCell> {
		override fun estimate(node: PathFindingCell, endNode: PathFindingCell): Float {
			return Math.abs(endNode.x - node.x) + Math.abs(endNode.y - node.y).toFloat();
		}
	}

	init {
		rebuildPathMap()

		map.addOnCellListener { cellX, cellY, cell ->
			// TODO: This might not work as well as we expect. Around this cell there can be cells that span multiple cells as well, as we're only refreshing its edges
			// Rebuild this cell & cells around it as well
			val x1 = cell.x - 1
			val x2 = cell.x + cell.w
			val y1 = cell.y - 1
			val y2 = cell.y + cell.h

			for (y in y1..y2) {
				val row = passableNodes.getOrNull(y) ?: continue
				for (x in x1..x2) {
					val c = row.getOrNull(x) ?: continue
					c.rebuild(map, passableNodes)
				}
			}
		}
	}

	private fun rebuildPathMap() {
		PathFindingCell.reset()

		passableNodes.forEach { it.forEach { cell -> cell.rebuild(map, passableNodes) } }
	}

	fun findPath(start: IntVector2, end: IntVector2): RecyclableArray<IntVector2> {
		return findPath(start.x, start.y, end.x, end.y)
	}

	fun findPath(startX: Int, startY: Int, endX: Int, endY: Int): RecyclableArray<IntVector2> {
		val graph = DefaultIndexedGraph(passableFlatNodes)
		val pathFinding = IndexedAStarPathFinder(graph)

		outGraph.clear()

		pathFinding.searchConnectionPath(passableNodes[startY][startX], passableNodes[endY][endX], TiledManhattanDistance, outGraph)

		return recyclableArrayFromPath(outGraph)
	}

	private fun recyclableArrayFromPath(graph: GraphPath<Connection<PathFindingCell>>): RecyclableArray<IntVector2> {
		val result = RecyclableArray(intVector2Pool, graph.count + 1)

		for (connection in graph) {
			val toNode = connection.toNode
			val value = (intVector2Pool.obtain() as MutableIntVector2).set(toNode.x, toNode.y)
			result.add(value)
		}

		return result
	}
}

private class PathFindingCell(val x: Int, val y: Int) : IndexedNode<PathFindingCell> {
	companion object {
		var lastIdx = 0

		val ZERO = com.badlogic.gdx.utils.Array<Connection<PathFindingCell>>(0)

		fun reset() {
			lastIdx = 0
		}
	}

	private val index = lastIdx++
	private lateinit var connections: com.badlogic.gdx.utils.Array<Connection<PathFindingCell>>

	fun rebuild(map: GameMap, pathMap: Array<Array<PathFindingCell>>) {
		val result = com.badlogic.gdx.utils.Array<Connection<PathFindingCell>>(8)

		addConnection(result, map, pathMap, x, y + 1)
		addConnection(result, map, pathMap, x, y - 1)
		addConnection(result, map, pathMap, x + 1, y)
		addConnection(result, map, pathMap, x - 1, y)

		addConnection(result, map, pathMap, x + 1, y + 1)
		addConnection(result, map, pathMap, x + 1, y - 1)
		addConnection(result, map, pathMap, x - 1, y + 1)
		addConnection(result, map, pathMap, x - 1, y - 1)

		connections = if (result.size > 0) result else ZERO
	}

	private fun addConnection(connections: com.badlogic.gdx.utils.Array<Connection<PathFindingCell>>,
	                          map: GameMap,
	                          pathMap: Array<Array<PathFindingCell>>,
	                          x: Int, y: Int) {
		val cell = map[x, y] ?: return
		val passable = when {
			cell is ModelMapCell<*> -> {
				val model = cell.model
				when {
					model is Building && model.data.passable -> true
					model is ConstructionSite -> true
					else -> false
				}
			}
			cell.javaClass == BaseMapCell::class.java -> true
			else -> false
		}
		if (!passable) return

		val pathCell = pathMap.getOrNull(y)?.getOrNull(x) ?: return
		// I thought connections could be reused, but it seems you cannot (so `from -> to` is not the same as `to -> from`)
		connections.add(DefaultConnection(this, pathCell))

	}

	override fun getConnections() = connections

	override fun getIndex() = index
	override fun toString(): String {
		return "$x, $y"
	}
}
