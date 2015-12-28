package com.retronicgames.lis.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.I18NBundle
import com.retronicgames.lis.ui.LISSkin.button
import com.retronicgames.utils.RGGUI

class LISGUI : RGGUI() {
	companion object {
		val i18n = I18NBundle.createBundle(Gdx.files.internal("i18n/main"))
	}

	init {
		val btnMenu = button("btnMenu")

		root.add(btnMenu).top().left().expand()
	}

	fun <T> showList(titleI18N: String, vararg items: T, callback : (success:Boolean) -> Unit) {
		val dialog = LISSkin.dialog(i18n.get(titleI18N))
		val list = LISSkin.scrollList(*items)

		dialog.contentTable.add(list).fillX().expandX().width(200f).row()
		dialog.button(i18n.get("dialog.common.button.ok"), true)
		dialog.button(i18n.get("dialog.common.button.cancel"), false)
		dialog.setCallback({ result -> callback(result as Boolean) })
		dialog.show(stage)
	}
}