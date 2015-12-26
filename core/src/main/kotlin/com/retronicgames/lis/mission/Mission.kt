package com.retronicgames.lis.mission

import com.retronicgames.lis.model.GameMap
import com.retronicgames.utils.IntVector2

interface Mission {
	val map: GameMap
	val initialCameraCenter: IntVector2
}