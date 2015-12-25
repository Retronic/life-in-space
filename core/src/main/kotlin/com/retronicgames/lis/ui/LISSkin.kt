package com.retronicgames.lis.ui

import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.retronicgames.lis.manager.Assets

object LISSkin : Skin() {
	fun button(upImageId: String, downImageId: String? = null): Button {
		val upDrawable = drawable(upImageId)
		val downDrawable = drawable(downImageId)

		val style = Button.ButtonStyle(upDrawable, downDrawable, null)

		return Button(style)
	}

	@Suppress("NOTHING_TO_INLINE")
	private inline fun drawable(id: String?) = if (id != null) SpriteDrawable(Assets.sprite("ui", id)) else null
}