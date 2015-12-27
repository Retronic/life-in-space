package com.retronicgames.utils

open class IntVector2(x: Int, y: Int) {
	private val callbacks = arrayListOf<(oldX: Int, oldY: Int, newX: Int, newY: Int) -> Unit>()

	constructor() : this(Int.MIN_VALUE, Int.MIN_VALUE)

	companion object {
		val MINUS_ONE = IntVector2(-1, -1)
		val ZERO = IntVector2(0, 0)
		val ONE = IntVector2(1, 1)
	}

	protected var _x: Int = x
	protected var _y: Int = y

	open var x: Int
		get() = _x
		protected set(value) {
			val old = _x
			_x = value

			fireChange(old, _y)
		}

	open var y: Int
		get() = _y
		protected set(value) {
			val old = _y
			_y = value

			fireChange(_x, old)
		}

	protected fun fireChange(oldX: Int, oldY: Int) {
		if (oldX == _x && oldY == _y) return

		callbacks.forEach { it(oldX, oldY, _x, _y) }
	}

	fun onChange(function: (oldX: Int, oldY: Int, newX: Int, newY: Int) -> Unit) {
		callbacks.add(function)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is IntVector2) return false

		if (x != other.x) return false
		if (y != other.y) return false

		return true
	}

	override fun hashCode(): Int {
		var result = x
		result += 31 * result + y
		return result
	}

	infix operator fun compareTo(other: IntVector2): Int {
		return (x + y).compareTo(other.x + other.y)
	}

	override fun toString(): String {
		return "[$x, $y]"
	}
}

class MutableIntVector2(x: Int, y: Int) : IntVector2(x, y) {
	constructor() : this(Int.MIN_VALUE, Int.MIN_VALUE)

	override var x: Int
		get() = super.x
		set(value) {
			super.x = value
		}
	override var y: Int
		get() = super.y
		set(value) {
			super.y = value
		}

	fun set(other: IntVector2) = set(other.x, other.y)

	fun set(x: Int, y: Int): MutableIntVector2 {
		val oldX = _x
		val oldY = _y

		_x = x
		_y = y

		fireChange(oldX, oldY)

		return this
	}
}
