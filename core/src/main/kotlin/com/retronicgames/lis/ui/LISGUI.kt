package com.retronicgames.lis.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.utils.I18NBundle
import com.retronicgames.lis.ui.LISSkin.button
import com.retronicgames.utils.RGGUI

class LISGUI : RGGUI() {
	private val i18n = I18NBundle.createBundle(Gdx.files.internal("i18n/main"))
	private val dialog = Dialog("", LISSkin)

	init {
		val btnMenu = button("btnMenu")

		root.add(btnMenu).top().left().expand()
	}

	/**
	 * Show text dialog with optional buttons
	 */
	fun showModal(i18nKey: String) {
		dialog.clear()
		dialog.add(LISSkin.wrappedLabel(i18n.get(i18nKey))).fillX().expandX().row()
		dialog.show(stage)
	}
}