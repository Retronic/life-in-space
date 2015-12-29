package com.retronicgames.lis

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.retronicgames.api.gdx.PlatformSupport
import com.retronicgames.lis.mission.Mission1
import com.retronicgames.lis.screen.ScreenGame

class LISGame(private val platformSupport: PlatformSupport) : Game() {
	private companion object {
		val MARKER = LISGame::class.java.simpleName
	}

	override fun create() {
		// FIXME: Remove!
		Gdx.app.logLevel = Application.LOG_DEBUG

		Gdx.app.debug(MARKER, "${platformSupport.title} v${platformSupport.version}")

		setScreen(ScreenGame(Mission1(this)))
	}

	fun showErrorDialog(message: String) {
		throw RuntimeException(message)
	}
}