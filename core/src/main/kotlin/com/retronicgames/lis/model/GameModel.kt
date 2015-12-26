package com.retronicgames.lis.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Json

object GameModel {
	val dataBuildings:DataBuildings

	init {
		val json = Json()
		dataBuildings = json.fromJson(DataBuildings::class.java, Gdx.files.internal("data/buildings.json"))
	}
}