package com.retronicgames.lis.model

import com.retronicgames.utils.IntVector2

interface Model {
	// This properties are part of the instance, instead of the class. Not optimal, but will do for now...
	val id: String
	val size: IntVector2
}