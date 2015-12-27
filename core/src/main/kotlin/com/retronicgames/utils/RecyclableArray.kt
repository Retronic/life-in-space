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

import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.Pool

class RecyclableArray<T>(private val pool: Pool<T>, size: Int) : Iterable<T>, Disposable {
	private val array = com.badlogic.gdx.utils.Array<T>(size)

	val size:Int
		get() = array.size

	override fun iterator() = array.iterator()

	override fun dispose() {
		pool.freeAll(array)
	}

	operator fun get(index: Int) = array.get(index)

	fun add(value: T) = array.add(value)

	override fun toString() = "[${array.toString(", ")}]"
}