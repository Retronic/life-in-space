package com.retronicgames.lis.ui

import com.retronicgames.lis.ui.LISSkin.button
import com.retronicgames.utils.RGGUI

class LISGUI : RGGUI() {
	init {
		val btnMenu = button("btnMenu")

		root.add(btnMenu).top().left().expand()
	}
}