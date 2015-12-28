package com.retronicgames.lis.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.utils.I18NBundle
import com.retronicgames.lis.ui.LISSkin.button
import com.retronicgames.utils.RGGUI

class LISGUI : RGGUI() {
	companion object {
		val i18n = I18NBundle.createBundle(Gdx.files.internal("i18n/main"))
	}

	private val dialog = Dialog("", LISSkin)

	init {
		dialog.isMovable = false
		dialog.isModal = true

		val btnMenu = button("btnMenu")

		root.add(btnMenu).top().left().expand()
	}

	/**
	 * Show text dialog with optional buttons
	 */
	fun showModal(i18nKey: String) {
		dialog.contentTable.clear()
		dialog.contentTable.add(LISSkin.wrappedLabel(i18n.get(i18nKey))).fillX().expandX().width(200f).row()
		dialog.show(stage)
	}

	fun <T> showList(titleI18N: String, vararg items: T) {
		dialog.contentTable.clear()
		dialog.titleLabel.setText(i18n.get(titleI18N))
		val list = LISSkin.scrollList(*items)
		dialog.contentTable.add(list).fillX().expandX().width(200f).row()
		dialog.show(stage)
	}
}