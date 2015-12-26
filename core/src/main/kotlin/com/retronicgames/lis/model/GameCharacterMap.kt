package com.retronicgames.lis.model

import com.retronicgames.lis.model.characters.GameCharacter

class GameCharacterMap : Iterable<GameCharacter<*, *>> {
	private val characters = com.badlogic.gdx.utils.Array<GameCharacter<*, *>>(16)

	override fun iterator() = characters.iterator()

	fun add(character: GameCharacter<*, *>) {
		characters.add(character)
	}
}