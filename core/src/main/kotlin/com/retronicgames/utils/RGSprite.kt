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
package com.retronicgames.utils

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2

interface RGSprite {
	val x: Float
	val y: Float
	val w: Float
	val h: Float

	fun setPosition(x: Float, y: Float)
	fun setSize(w: Float, h: Float)
	fun setOrigin(x: Float, y: Float)

	fun draw(batch: Batch)
}

class RGSpriteWrapper(private val sprite: Sprite) : RGSprite {
	override val x: Float
		get() = sprite.x
	override val y: Float
		get() = sprite.y
	override val w: Float
		get() = sprite.width
	override val h: Float
		get() = sprite.height

	override fun draw(batch: Batch) = sprite.draw(batch)

	override fun setOrigin(x: Float, y: Float) = sprite.setOrigin(x, y)

	override fun setPosition(x: Float, y: Float) = sprite.setPosition(x, y)

	override fun setSize(w: Float, h: Float) = sprite.setSize(w, h)
}

abstract class AbstractRGSprite : RGSprite {
	protected val position = Vector2();
	protected val size = Vector2();
	protected val origin = Vector2();

	override val x: Float
		get() = position.x
	override val y: Float
		get() = position.y
	override val w: Float
		get() = size.x
	override val h: Float
		get() = size.y

	override fun setPosition(x: Float, y: Float) {
		position.set(x, y)
	}

	override fun setSize(w: Float, h: Float) {
		size.set(w, h)
	}

	override fun setOrigin(x: Float, y: Float) {
		origin.set(x, y)
	}
}

class RGCompositeSprite(val sprite1: RGSprite, val sprite2: RGSprite) : RGSprite {
	override val x: Float
		get() = throw UnsupportedOperationException()
	override val y: Float
		get() = throw UnsupportedOperationException()
	override val w: Float
		get() = throw UnsupportedOperationException()
	override val h: Float
		get() = throw UnsupportedOperationException()

	override fun setOrigin(x: Float, y: Float) {
		throw UnsupportedOperationException()
	}

	override fun draw(batch: Batch) {
		sprite1.draw(batch)
		sprite2.draw(batch)
	}

	override fun setPosition(x: Float, y: Float) {
		throw UnsupportedOperationException()
	}

	override fun setSize(w: Float, h: Float) {
		throw UnsupportedOperationException()
	}
}