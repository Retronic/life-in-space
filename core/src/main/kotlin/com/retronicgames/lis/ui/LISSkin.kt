package com.retronicgames.lis.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.retronicgames.lis.manager.Assets
import com.retronicgames.utils.RGDialog

object LISSkin : Skin() {
	// FIXME: All these fonts should be part of a texture atlas, instead of creating a texture per font
	private val fontDefault = createFont("default", "fonts/PixelOperator/PixelOperator.ttf", 15)
	private val fontDefaultBold = createFont("defaultBold", "fonts/PixelOperator/PixelOperator-Bold.ttf", 15)

	val MAIN_PALETTE = intArrayOf(0xD9D5C3FF.toInt(), 0x999082FF.toInt(), 0x575B67FF, 0x3D3940FF)
	val MAIN_PALETTE_COLORS = MAIN_PALETTE.map { Color(it) }.toTypedArray()

	init {
		addStyleLabel()
		addStyleButton()
		addStyleDialog()
		addStyleList()
		addStyleScrollPane()
	}

	private fun addStyleLabel() {
		val style = Label.LabelStyle(fontDefault, MAIN_PALETTE_COLORS[3])
		add("default", style)
		val stylePanel1 = Label.LabelStyle(fontDefault, MAIN_PALETTE_COLORS[3])
		stylePanel1.background = drawable9("panel1")
		add("panel1", stylePanel1)
		val stylePanel2 = Label.LabelStyle(fontDefault, MAIN_PALETTE_COLORS[3])
		stylePanel2.background = drawable9("panel2")
		add("panel2", stylePanel2)
	}

	private fun addStyleButton() {
		val style = Button.ButtonStyle(drawable9("buttonUp"), drawable9("buttonDown"), null)
		style.over = drawable9("buttonOver")
		add("default", style)

		val styleText = TextButton.TextButtonStyle(drawable9("buttonUp"), drawable9("buttonDown"), null, fontDefaultBold)
		styleText.over = drawable9("buttonOver")
		add("default", styleText)
	}

	private fun addStyleDialog() {
		val style = Window.WindowStyle(fontDefault, Color.BLACK, drawable9("backDialog"))
		add("default", style)
	}

	private fun addStyleList() {
		val style = List.ListStyle(fontDefault, MAIN_PALETTE_COLORS[0], MAIN_PALETTE_COLORS[2], drawable("listSelection"))
		add("default", style)
	}

	private fun addStyleScrollPane() {
		val style = ScrollPane.ScrollPaneStyle()
		add("default", style)
	}

	fun button(upImageId: String, downImageId: String? = null): Button {
		val upDrawable = drawable(upImageId)
		val downDrawable = drawable(downImageId)

		val style = Button.ButtonStyle(upDrawable, downDrawable, null)

		return Button(style)
	}

	fun button9(upImageId: String, downImageId: String? = null): Button {
		val upDrawable = drawable9(upImageId)
		val downDrawable = drawable9(downImageId)

		val style = Button.ButtonStyle(upDrawable, downDrawable, null)

		return Button(style)
	}

	fun textButton9(text: String, textColor: Color, upImageId: String, downImageId: String? = null): Button {
		val upDrawable = drawable9(upImageId)
		val downDrawable = drawable9(downImageId)

		val style = TextButton.TextButtonStyle(upDrawable, downDrawable, null, fontDefault)
		style.fontColor = textColor

		return TextButton(text, style)
	}

	fun dialog(title: String): RGDialog {
		val result = RGDialog(title, this)

		result.isMovable = false
		result.isModal = true

		return result
	}

	private fun createFont(fontName: String, fontPath: String, fontSize: Int): BitmapFont {
		val parameter = FreeTypeFontGenerator.FreeTypeFontParameter();
		val generator = FreeTypeFontGenerator(Gdx.files.internal(fontPath));

		parameter.size = scaled(fontSize);
		parameter.kerning = true

		val font = generator.generateFont(parameter);
		font.color = Color.BLACK;

		add(fontName, font)

		generator.dispose()

		return font
	}

	private fun scaled(fontSize: Int) = fontSize

	@Suppress("NOTHING_TO_INLINE")
	private inline fun drawable(id: String?) = if (id != null) SpriteDrawable(Assets.sprite("ui", id)) else null

	@Suppress("NOTHING_TO_INLINE")
	private inline fun drawable9(id: String?) = if (id != null) NinePatchDrawable(Assets.ninePatch("ui", id)) else null

	fun wrappedLabel(text: String): Label {
		val result = Label(text, this)
		result.setWrap(true)
		return result
	}

	fun <T> scrollList(vararg items: T): ScrollPane {
		val list = com.badlogic.gdx.scenes.scene2d.ui.List<T>(this)
		list.setItems(*items)

		val scroll = ScrollPane(list, this)
		return scroll
	}

	fun panel(id: String): Table {
		val result = Table(LISSkin)

		result.background = drawable9(id)

		return result
	}

	fun image(atlas: String, id: String) = Image(Assets.sprite(atlas, id))
}