package com.retronicgames.utils

open class IntVector2(x: Int, y: Int) {
	companion object {
		val MINUS_ONE = IntVector2(-1, -1)
		val ZERO = IntVector2(0, 0)
		val ONE = IntVector2(1, 1)
	}

	open var x: Int = x
		protected set
	open var y: Int = y
		protected set

	constructor() : this(0, 0)

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

	fun set(x: Int, y: Int): MutableIntVector2 {
		this.x = x
		this.y = y

		return this
	}

	fun set(other: IntVector2): MutableIntVector2 {
		this.x = other.x
		this.y = other.y

		return this
	}
}
