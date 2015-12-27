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

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array

open class ReadOnlyValue<T>(initialValue: T) {
	private val callbacks = arrayListOf<T.() -> Unit>()

	open var value: T = initialValue
		protected set(value) {
			if (field == value) {
				return
			}
			field = value
			fireChange()
		}

	protected fun fireChange() {
		callbacks.forEach { it(value) }
	}

	// TODO: Might be a good idea to call the function as soon as the callback is added? Right now we have a lot of usages with initialValue == the value we add to the callback function
	fun onChange(function: T.() -> Unit) {
		callbacks.add(function)
	}
}

open class MutableValue<T>(initialValue: T) : ReadOnlyValue<T>(initialValue) {
	override public var value: T
		get() = super.value
		set(value) {
			super.value = value
		}
}

@JvmName("timesFloatFloatValue") operator fun NumberReadOnlyValue<Float>.times(other: NumberReadOnlyValue<Float>) = NumberOpReadOnlyValue(this, other, Float::times)
@JvmName("timesIntIntValue") operator fun NumberReadOnlyValue<Int>.times(other: NumberReadOnlyValue<Int>) = NumberOpReadOnlyValue(this, other, Int::times)
@JvmName("timesFloatFloat") operator fun NumberReadOnlyValue<Float>.times(other: Float) = NumberOpReadOnlyValue(this, NumberReadOnlyValue(other), Float::times)
@JvmName("timesIntInt") operator fun NumberReadOnlyValue<Int>.times(other: Int) = NumberOpReadOnlyValue(this, NumberReadOnlyValue(other), Int::times)
@JvmName("divFloatFloatValue") operator fun NumberReadOnlyValue<Float>.div(other: NumberReadOnlyValue<Float>) = NumberOpReadOnlyValue(this, other, Float::div)
@JvmName("divIntIntValue") operator fun NumberReadOnlyValue<Int>.div(other: NumberReadOnlyValue<Int>) = NumberOpReadOnlyValue(this, other, Int::div)
@JvmName("divFloatFloat") operator fun NumberReadOnlyValue<Float>.div(other: Float) = NumberOpReadOnlyValue(this, NumberReadOnlyValue(other), Float::div)
@JvmName("divIntInt") operator fun NumberReadOnlyValue<Int>.div(other: Int) = NumberOpReadOnlyValue(this, NumberReadOnlyValue(other), Int::div)

infix operator fun Float.times(other: NumberReadOnlyValue<Float>) = NumberOpReadOnlyValue(NumberReadOnlyValue(this), other, Float::times)
infix operator fun Float.div(other: NumberReadOnlyValue<Float>) = NumberOpReadOnlyValue(NumberReadOnlyValue(this), other, Float::div)
infix operator fun Int.times(other: NumberReadOnlyValue<Int>) = NumberOpReadOnlyValue(NumberReadOnlyValue(this), other, Int::times)
infix operator fun Int.div(other: NumberReadOnlyValue<Int>) = NumberOpReadOnlyValue(NumberReadOnlyValue(this), other, Int::div)

open class NumberReadOnlyValue<T : Number>(initialValue: T) : ReadOnlyValue<T>(initialValue)

class NumberMutableValue<T : Number>(initialValue: T) : NumberReadOnlyValue<T>(initialValue) {
	override public var value: T
		get() = super.value
		set(value) {
			super.value = value
		}
}

class NumberOpReadOnlyValue<T : Number>(val left: NumberReadOnlyValue<T>, val right: NumberReadOnlyValue<T>, private val op: (T, T) -> T) : NumberReadOnlyValue<T>(op(left.value, right.value)) {
	init {
		left.onChange { refresh() }
		right.onChange { refresh() }
	}

	private fun refresh() {
		value = op(left.value, right.value)
	}
}

// FIXME: `value` is accessible, we need a better way
class ListReadOnlyValue<T : ObservableListWrapper<Y>, Y>(value: T) : ReadOnlyValue<T>(value), Iterable<Y> {
	private val callbacksAdded = Array<(Y) -> Unit>()
	private val callbacksRemoved = Array<(Y) -> Unit>()

	val size: Int
		get() = value.size

	override fun iterator() = value.iterator()

	init {
		value.addListener({ e ->
//			while (e.next()) {
				if (e.wasAdded()) {
					for (j in e.from..e.to - 1) {
						fireAdded(e.list[j])
					}
				} else if (e.wasRemoved()) {
					for (v in e.removed) {
						fireRemoved(v)
					}
				}
//			}
			fireChange()
		})
	}

	private fun fireAdded(value:Y) {
		callbacksAdded.forEach { it(value) }
	}

	fun onAdded(function: (Y) -> Unit) {
		callbacksAdded.add(function)
	}

	private fun fireRemoved(value:Y) {
		callbacksRemoved.forEach { it(value) }
	}

	fun onRemoved(function: (Y) -> Unit) {
		callbacksRemoved.add(function)
	}
}

// FIXME: `value` is accessible, we need a better way
open class Vector2ReadOnlyValue(value: Vector2) : ReadOnlyValue<Vector2>(value) {
	open var x: Float
		get() = value.x
		protected set(value) {
			this.value.x = value

			fireChange()
		}

	open var y: Float
		get() = value.y
		protected set(value) {
			this.value.y = value

			fireChange()
		}

	protected open fun set(x: Float, y: Float) {
		this.value.x = x
		this.value.y = y

		fireChange()
	}

	protected open fun add(x: Float, y: Float) {
		this.value.x += x
		this.value.y += y

		fireChange()
	}
}

class Vector2MutableValue(value: Vector2) : Vector2ReadOnlyValue(value) {
	override var x: Float
		get() = super.x
		set(value) {
			super.x = value
		}
	override var y: Float
		get() = super.y
		set(value) {
			super.y = value
		}

	override public fun set(x: Float, y: Float) {
		super.set(x, y)
	}

	override public fun add(x: Float, y: Float) {
		super.add(x, y)
	}
}