package com.retronicgames.lis.model.characters

import com.retronicgames.lis.model.DataModel
import com.retronicgames.utils.MutableIntVector2

abstract class AbstractCharacter<DataType : DataModel, StateType : Enum<StateType>>(x: Int, y: Int, override val data: DataType) : GameCharacter<DataType, StateType> {
	override val position = MutableIntVector2(x, y)
}