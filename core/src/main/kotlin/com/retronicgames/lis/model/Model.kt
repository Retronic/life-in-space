package com.retronicgames.lis.model

interface Model<DataModelType : DataModel> {
	val data: DataModelType
}