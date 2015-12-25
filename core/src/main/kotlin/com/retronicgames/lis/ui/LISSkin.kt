package com.retronicgames.lis.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.retronicgames.lis.manager.Assets

object LISSkin : Skin() {
	// FIXME: All these fonts should be part of a texture atlas, instead of creating a texture per font
	private val fontDefault = createFont("default", "fonts/PixelOperator/PixelOperator.ttf", 15)
	private val fontDefaultBold = createFont("defaultBold", "fonts/PixelOperator/PixelOperator-Bold.ttf", 15)

	init {
		addStyleLabel()
		addStyleDialog()
	}

	private fun addStyleLabel() {
		val style = Label.LabelStyle(fontDefault, Color.BLACK)
		add("default", style)
	}

	private fun addStyleDialog() {
		val style = Window.WindowStyle(fontDefault, Color.BLACK, drawable9("backDialog"))
		add("default", style)
	}

	fun button(upImageId: String, downImageId: String? = null): Button {
		val upDrawable = drawable(upImageId)
		val downDrawable = drawable(downImageId)

		val style = Button.ButtonStyle(upDrawable, downDrawable, null)

		return Button(style)
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
}