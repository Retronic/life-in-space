package com.retronicgames.lis.model.characters

class CharacterSettler(x: Int, y: Int) : AbstractCharacter<DataCharacterSettler, StateSettler>(x, y, DataCharacterSettler) {
	override val state = StateSettler.IDLE
}

enum class StateSettler {
	IDLE, WAVING, WALKING
}
