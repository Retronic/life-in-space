/*******************************************************************************
 * Copyright 2014 See AUTHORS file.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.retronicgames.gdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch.*
import com.badlogic.gdx.graphics.g2d.SpriteCache
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Disposable
import com.retronicgames.lis.visual.VisualCell
import com.retronicgames.utils.IntVector2
import com.retronicgames.utils.MutableIntVector2

/** Renders ortho tiles by caching geometry on the GPU. How much is cached is controlled by [.setOverCache]. When the
 * view reaches the edge of the cached tiles, the cache is rebuilt at the new view position.
 *
 * This class may have poor performance when tiles are often changed dynamically, since the cache must be rebuilt after each
 * change.
 *
 * @author Justin Shapcott
 * @author Nathan Sweet
 * @author Edu Garcia
 */
class RGOrthoCachedTiledMapRenderer(protected val map: TiledMap, protected var unitScale: Float = 1f, cacheSize: Int = 2000) : TiledMapRenderer, Disposable {
	companion object {
		private val tolerance = 0.00001f
		protected val NUM_VERTICES = 20
	}

	val spriteCache: SpriteCache

	protected val vertices = FloatArray(20)
	protected var blending: Boolean = false
	protected val viewBounds = Rectangle()
	protected val cacheBounds = Rectangle()

	/** Sets the percentage of the view that is cached in each direction. Default is 0.5.
	 *
	 * Eg, 0.75 will cache 75% of the width of the view to the left and right of the view, and 75% of the height of the view above
	 * and below the view.  */
	protected var overCache = 0.50f
	protected var maxTileWidth: Float = 0.toFloat()
	protected var maxTileHeight: Float = 0.toFloat()
	/** Returns true if tiles are currently cached.  */
	var isCached: Boolean = false
		protected set
	protected var count: Int = 0
	protected var canCacheMoreN: Boolean = false
	protected var canCacheMoreE: Boolean = false
	protected var canCacheMoreW: Boolean = false
	protected var canCacheMoreS: Boolean = false

	private val tempIntVector2 = MutableIntVector2(-1, -1)

	var defaultCellColor = Color.WHITE

	init {
		spriteCache = SpriteCache(cacheSize, true)
	}

	override fun setView(camera: OrthographicCamera) {
		spriteCache.projectionMatrix = camera.combined
		val width = camera.viewportWidth * camera.zoom + maxTileWidth * 2f * unitScale
		val height = camera.viewportHeight * camera.zoom + maxTileHeight * 2f * unitScale
		viewBounds.set(camera.position.x - width / 2, camera.position.y - height / 2, width, height)

		if (canCacheMoreW && viewBounds.x < cacheBounds.x - tolerance ||
				canCacheMoreS && viewBounds.y < cacheBounds.y - tolerance ||
				canCacheMoreE && viewBounds.x + viewBounds.width > cacheBounds.x + cacheBounds.width + tolerance ||
				canCacheMoreN && viewBounds.y + viewBounds.height > cacheBounds.y + cacheBounds.height + tolerance
		)
			isCached = false
	}

	override fun setView(projection: Matrix4, x: Float, y: Float, width: Float, height: Float) {
		var x = x
		var y = y
		var width = width
		var height = height
		spriteCache.projectionMatrix = projection
		x -= maxTileWidth * unitScale
		y -= maxTileHeight * unitScale
		width += maxTileWidth * 2f * unitScale
		height += maxTileHeight * 2f * unitScale
		viewBounds.set(x, y, width, height)

		if (canCacheMoreW && viewBounds.x < cacheBounds.x - tolerance ||
				canCacheMoreS && viewBounds.y < cacheBounds.y - tolerance ||
				canCacheMoreE && viewBounds.x + viewBounds.width > cacheBounds.x + cacheBounds.width + tolerance ||
				canCacheMoreN && viewBounds.y + viewBounds.height > cacheBounds.y + cacheBounds.height + tolerance
		)
			isCached = false
	}

	override fun render() {
		if (!isCached) {
			isCached = true
			count = 0
			spriteCache.clear()

			val extraWidth = viewBounds.width * overCache
			val extraHeight = viewBounds.height * overCache
			cacheBounds.x = viewBounds.x - extraWidth
			cacheBounds.y = viewBounds.y - extraHeight
			cacheBounds.width = viewBounds.width + extraWidth * 2
			cacheBounds.height = viewBounds.height + extraHeight * 2

			for (layer in map.layers) {
				spriteCache.beginCache()
				if (layer is TiledMapTileLayer) {
					renderTileLayer(layer)
				} else if (layer is TiledMapImageLayer) {
					renderImageLayer(layer)
				}
				spriteCache.endCache()
			}
		}

		if (blending) {
			Gdx.gl.glEnable(GL20.GL_BLEND)
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
		}
		spriteCache.begin()
		val mapLayers = map.layers
		var i = 0
		val j = mapLayers.count
		while (i < j) {
			val layer = mapLayers.get(i)
			if (layer.isVisible) {
				spriteCache.draw(i)
				renderObjects(layer)
			}
			i++
		}
		spriteCache.end()
		if (blending) Gdx.gl.glDisable(GL20.GL_BLEND)
	}

	override fun render(layers: IntArray) {
		if (!isCached) {
			isCached = true
			count = 0
			spriteCache.clear()

			val extraWidth = viewBounds.width * overCache
			val extraHeight = viewBounds.height * overCache
			cacheBounds.x = viewBounds.x - extraWidth
			cacheBounds.y = viewBounds.y - extraHeight
			cacheBounds.width = viewBounds.width + extraWidth * 2
			cacheBounds.height = viewBounds.height + extraHeight * 2

			for (layer in map.layers) {
				spriteCache.beginCache()
				if (layer is TiledMapTileLayer) {
					renderTileLayer(layer)
				} else if (layer is TiledMapImageLayer) {
					renderImageLayer(layer)
				}
				spriteCache.endCache()
			}
		}

		if (blending) {
			Gdx.gl.glEnable(GL20.GL_BLEND)
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
		}
		spriteCache.begin()
		val mapLayers = map.layers
		for (i in layers) {
			val layer = mapLayers.get(i)
			if (layer.isVisible) {
				spriteCache.draw(i)
				renderObjects(layer)
			}
		}
		spriteCache.end()
		if (blending) Gdx.gl.glDisable(GL20.GL_BLEND)
	}

	override fun renderObjects(layer: MapLayer) {
		for (`object` in layer.objects) {
			renderObject(`object`)
		}
	}

	override fun renderObject(`object`: MapObject) {
	}

	override fun renderTileLayer(layer: TiledMapTileLayer) {
		val defaultColor = Color.toFloatBits(defaultCellColor.r, defaultCellColor.g, defaultCellColor.b, layer.opacity)

		val layerWidth = layer.width
		val layerHeight = layer.height

		val layerTileWidth = layer.tileWidth * unitScale
		val layerTileHeight = layer.tileHeight * unitScale

		val col1 = Math.max(0, (cacheBounds.x / layerTileWidth).toInt())
		val col2 = Math.min(layerWidth, ((cacheBounds.x + cacheBounds.width + layerTileWidth) / layerTileWidth).toInt())

		val row1 = Math.max(0, (cacheBounds.y / layerTileHeight).toInt())
		val row2 = Math.min(layerHeight, ((cacheBounds.y + cacheBounds.height + layerTileHeight) / layerTileHeight).toInt())

		canCacheMoreN = row2 < layerHeight
		canCacheMoreE = col2 < layerWidth
		canCacheMoreW = col1 > 0
		canCacheMoreS = row1 > 0

		val vertices = this.vertices
		for (row in row2 downTo row1) {
			for (col in col1..col2 - 1) {
				val cell = layer.getCell(col, row) ?: continue

				val tile = cell.tile ?: continue

				count++

				val region = tile.textureRegion
				val texture = region.texture

				val x1 = col * layerTileWidth + tile.offsetX * unitScale
				val y1 = row * layerTileHeight + tile.offsetY * unitScale
				val x2 = x1 + region.regionWidth * unitScale
				val y2 = y1 + region.regionHeight * unitScale

				val adjustX = 0.5f / texture.width
				val adjustY = 0.5f / texture.height
				val u1 = region.u + adjustX
				val v1 = region.v2 - adjustY
				val u2 = region.u2 - adjustX
				val v2 = region.v + adjustY

				val color = if ((cell as VisualCell).color != null) {
					val newColor = cell.color!!
					Color.toFloatBits(newColor.r, newColor.g, newColor.b, layer.opacity)
				} else {
					defaultColor
				}
				vertices[X1] = x1
				vertices[Y1] = y1
				vertices[C1] = color
				vertices[U1] = u1
				vertices[V1] = v1

				vertices[X2] = x1
				vertices[Y2] = y2
				vertices[C2] = color
				vertices[U2] = u1
				vertices[V2] = v2

				vertices[X3] = x2
				vertices[Y3] = y2
				vertices[C3] = color
				vertices[U3] = u2
				vertices[V3] = v2

				vertices[X4] = x2
				vertices[Y4] = y1
				vertices[C4] = color
				vertices[U4] = u2
				vertices[V4] = v1

				// No flipping or rotation for now
				/*val flipX = cell.flipHorizontally
				val flipY = cell.flipVertically
				val rotations = cell.rotation
				if (flipX) {
					var temp = vertices[U1]
					vertices[U1] = vertices[U3]
					vertices[U3] = temp
					temp = vertices[U2]
					vertices[U2] = vertices[U4]
					vertices[U4] = temp
				}
				if (flipY) {
					var temp = vertices[V1]
					vertices[V1] = vertices[V3]
					vertices[V3] = temp
					temp = vertices[V2]
					vertices[V2] = vertices[V4]
					vertices[V4] = temp
				}
				if (rotations != 0) {
					when (rotations) {
						TiledMapTileLayer.Cell.ROTATE_90 -> {
							val tempV = vertices[V1]
							vertices[V1] = vertices[V2]
							vertices[V2] = vertices[V3]
							vertices[V3] = vertices[V4]
							vertices[V4] = tempV

							val tempU = vertices[U1]
							vertices[U1] = vertices[U2]
							vertices[U2] = vertices[U3]
							vertices[U3] = vertices[U4]
							vertices[U4] = tempU
						}
						TiledMapTileLayer.Cell.ROTATE_180 -> {
							var tempU = vertices[U1]
							vertices[U1] = vertices[U3]
							vertices[U3] = tempU
							tempU = vertices[U2]
							vertices[U2] = vertices[U4]
							vertices[U4] = tempU
							var tempV = vertices[V1]
							vertices[V1] = vertices[V3]
							vertices[V3] = tempV
							tempV = vertices[V2]
							vertices[V2] = vertices[V4]
							vertices[V4] = tempV
						}
						TiledMapTileLayer.Cell.ROTATE_270 -> {
							val tempV = vertices[V1]
							vertices[V1] = vertices[V4]
							vertices[V4] = vertices[V3]
							vertices[V3] = vertices[V2]
							vertices[V2] = tempV

							val tempU = vertices[U1]
							vertices[U1] = vertices[U4]
							vertices[U4] = vertices[U3]
							vertices[U3] = vertices[U2]
							vertices[U2] = tempU
						}
					}
				}*/
				spriteCache.add(texture, vertices, 0, NUM_VERTICES)
			}
		}
	}

	override fun renderImageLayer(layer: TiledMapImageLayer) {
		val color = Color.toFloatBits(1.0f, 1.0f, 1.0f, layer.opacity)
		val vertices = this.vertices

		val region = layer.textureRegion ?: return

		val x = layer.x
		val y = layer.y
		val x1 = x * unitScale
		val y1 = y * unitScale
		val x2 = x1 + region.regionWidth * unitScale
		val y2 = y1 + region.regionHeight * unitScale

		val u1 = region.u
		val v1 = region.v2
		val u2 = region.u2
		val v2 = region.v

		vertices[X1] = x1
		vertices[Y1] = y1
		vertices[C1] = color
		vertices[U1] = u1
		vertices[V1] = v1

		vertices[X2] = x1
		vertices[Y2] = y2
		vertices[C2] = color
		vertices[U2] = u1
		vertices[V2] = v2

		vertices[X3] = x2
		vertices[Y3] = y2
		vertices[C3] = color
		vertices[U3] = u2
		vertices[V3] = v2

		vertices[X4] = x2
		vertices[Y4] = y1
		vertices[C4] = color
		vertices[U4] = u2
		vertices[V4] = v1

		spriteCache.add(region.texture, vertices, 0, NUM_VERTICES)
	}

	/** Causes the cache to be rebuilt the next time it is rendered.  */
	fun invalidateCache() {
		isCached = false
	}

	/** Expands the view size in each direction, ensuring that tiles of this size or smaller are never culled from the visible
	 * portion of the view. Default is 0,0.
	 *
	 *
	 * The amount of tiles cached is computed using `(view size + max tile size) * overCache`, meaning the max tile size
	 * increases the amount cached and possibly [.setOverCache] can be reduced.
	 *
	 *
	 * If the view size and [.setOverCache] are configured so the size of the cached tiles is always larger than the
	 * largest tile size, this setting is not needed.  */
	fun setMaxTileSize(maxPixelWidth: Float, maxPixelHeight: Float) {
		this.maxTileWidth = maxPixelWidth
		this.maxTileHeight = maxPixelHeight
	}

	override fun dispose() {
		spriteCache.dispose()
	}

	fun screen2cellCoords(layerWidth: Int, layerHeight: Int, tileWidth: Float, tileHeight: Float, screenX: Int, screenY: Int): IntVector2 {
		val layerTileWidth = tileWidth * unitScale
		val layerTileHeight = tileHeight * unitScale

		val floatX = (viewBounds.x + screenX) / layerTileWidth
		val floatY = (viewBounds.y + viewBounds.height - screenY) / layerTileHeight

		val x = if (floatX < 0 || floatX > layerWidth) -1 else floatX.toInt()
		val y = if (floatY < 0 || floatY > layerHeight) -1 else floatY.toInt()

		return if (x < 0 || y < 0) IntVector2.MINUS_ONE else tempIntVector2.set(x, y)
	}
}
