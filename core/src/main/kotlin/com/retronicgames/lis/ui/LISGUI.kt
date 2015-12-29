package com.retronicgames.lis.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.I18NBundle
import com.retronicgames.lis.ui.LISSkin.textButton9
import com.retronicgames.utils.RGGUI

class LISGUI : RGGUI() {
	companion object {
		val i18n = I18NBundle.createBundle(Gdx.files.internal("i18n/main"))
	}

	init {
		addButtonMenu()
		addDate()

		addResourcesPanel()
	}

	private fun addButtonMenu() {
		val btnMenu = textButton9(i18n.get("button.menu"), LISSkin.MAIN_PALETTE_COLORS[3], "btnMenu")

		root.add(btnMenu).top().left().expandX()
	}

	private fun addDate() {
		val label = Label("Month: 2  Day: 27", LISSkin, "panel1")
		root.add(label).top().right().row()
	}

	private fun addResourcesPanel() {
		val pnlResources = LISSkin.panel("panel2")
		pnlResources.pad(5f)
		pnlResources.columnDefaults(0).padRight(2f)
		pnlResources.add("38")
		pnlResources.add(LISSkin.image("ui", "icon_resource_power")).row()
		pnlResources.add("42")
		pnlResources.add(LISSkin.image("ui", "icon_resource_research")).row()
		pnlResources.add("55")
		pnlResources.add(LISSkin.image("ui", "icon_resource_bricks")).row()

		root.add()
		root.add(pnlResources).top().right().expand()
	}

	fun <T> showList(titleI18N: String, vararg items: T, callback: (result: T?) -> Unit) {
		val dialog = LISSkin.dialog(i18n.get(titleI18N))
		val scroll = LISSkin.scrollList(*items)
		val list = scroll.children[0] as com.badlogic.gdx.scenes.scene2d.ui.List<T>

		dialog.contentTable.add(scroll).fillX().expandX().width(200f).row()
		dialog.button(i18n.get("dialog.common.button.ok"), true)
		dialog.button(i18n.get("dialog.common.button.cancel"), false)
		dialog.setCallback({ result ->
			callback(if (result == true) list.selected else null)
		})
		dialog.show(stage)
	}
}