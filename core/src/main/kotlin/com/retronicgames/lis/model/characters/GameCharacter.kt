package com.retronicgames.lis.model.characters

import com.retronicgames.lis.model.BaseMapCell
import com.retronicgames.utils.IntVector2
import com.retronicgames.utils.value.ReadOnlyValue

interface GameCharacter<DataType : DataCharacterModel, StateType : Enum<StateType>> {
	val data: DataType
	val state: ReadOnlyValue<StateType>
	val position: IntVector2
	val currentCell: ReadOnlyValue<BaseMapCell>

	fun update(delta: Float)

	fun moveTo(cell: BaseMapCell?): Boolean
}