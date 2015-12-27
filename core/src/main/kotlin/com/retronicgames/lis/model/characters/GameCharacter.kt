package com.retronicgames.lis.model.characters

import com.retronicgames.lis.model.BaseMapCell
import com.retronicgames.lis.model.DataModel
import com.retronicgames.utils.IntVector2

interface GameCharacter<DataType : DataModel, StateType : Enum<StateType>> {
	val data: DataType
	val state: StateType
	val position: IntVector2
	var currentCell: BaseMapCell

	fun moveTo(cell: BaseMapCell?): Boolean
}