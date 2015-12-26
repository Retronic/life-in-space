package com.retronicgames.lis.model.buildings

import com.retronicgames.lis.model.DataModel

abstract class AbstractBuilding<DataModelType : DataModel>(override val data: DataModelType) : Building<DataModelType> {
}