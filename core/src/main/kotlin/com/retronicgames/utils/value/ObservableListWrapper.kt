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
package com.retronicgames.utils.value

class ObservableListWrapper<T>(private val list: MutableList<T>) : Iterable<T> {
	private val listChangeListeners = com.badlogic.gdx.utils.Array<(ListChangeEvent<T>) -> Unit>(false, 4)

	val size: Int
		get() = list.size

	override fun iterator() = list.iterator()

	fun addListener(listener: (ListChangeEvent<T>) -> Unit) {
		listChangeListeners.add(listener)
	}

	fun add(value: T) {
		list.add(value)

		fireAdded(list.size - 1, 1)
	}

	fun remove(value: T) {
		list.remove(value)

		fireRemoved(value)
	}

	private fun fireAdded(position: Int, size: Int) {
		val ev = ListChangeEvent(list, true, position, position + size)
		for (listener in listChangeListeners) {
			listener(ev)
		}
	}

	private fun fireRemoved(vararg value: T) {
		val ev = ListChangeEvent(list, false, -1, -1, *value)
		for (listener in listChangeListeners) {
			listener(ev)
		}
	}
}

class ListChangeEvent<T>(val list: List<T>, private val added:Boolean, val from: Int, val to: Int, vararg val removed:T) {
	fun wasAdded() = added

	fun wasRemoved() = !added
}

