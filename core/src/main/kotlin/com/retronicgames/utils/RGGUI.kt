package com.retronicgames.utils

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.ScreenViewport

open class RGGUI {
	private val stage = Stage(ScreenViewport())
	protected val root = Table()

	init {
		root.setFillParent(true)

		stage.addActor(root)
	}

	fun inputProcessor(): InputProcessor = stage

	fun resize(width: Int, height: Int) {
		stage.viewport.update(width, height)
	}

	fun render() {
		stage.draw()
	}

	fun update(delta: Float) {
		stage.act(delta)
	}
}