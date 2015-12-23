package com.retronicgames.lis

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.retronicgames.api.gdx.PlatformSupport

class LISGame(private val platformSupport: PlatformSupport) : Game() {
	override fun create() {

	}

	override fun render() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
	}
}