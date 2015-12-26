package com.retronicgames.lis.model

import com.retronicgames.utils.IntVector2

class DataBuildings {
	var buildings = hashMapOf<String, DataBuilding>()
}

class DataBuilding {
	lateinit var model : DataBuildingModel
	var visual : DataBuildingVisual? = null
}

class DataBuildingModel {
	lateinit var size: IntVector2
	var constructionTime = 0
}

class DataBuildingVisual {
	lateinit var offset: IntVector2
}
