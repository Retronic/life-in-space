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
package com.retronicgames.gdx

import com.badlogic.gdx.math.RandomXS128
import com.badlogic.gdx.utils.IdentityMap
import com.badlogic.gdx.utils.IntMap
import com.badlogic.gdx.utils.ObjectMap

@Suppress("NOTHING_TO_INLINE")
operator inline fun <K, V> IdentityMap<K, V>.set(key: K, value: V) {
	put(key, value)
}

@Suppress("NOTHING_TO_INLINE")
operator inline fun <K, V> ObjectMap<K, V>.set(key: K, value: V) {
	put(key, value)
}

@Suppress("NOTHING_TO_INLINE")
operator inline fun <V> IntMap<V>.set(key: Int, value: V) {
	put(key, value)
}

@Suppress("NOTHING_TO_INLINE")
inline fun <T> RandomXS128.from(array: com.badlogic.gdx.utils.Array<T>) = if (array.size < 1) null else array[nextInt(array.size)]