package com.retronicgames.lis.platform

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.retronicgames.api.gdx.PlatformSupport
import com.retronicgames.lis.LISGame

object DesktopLauncher {
	@JvmStatic fun main(arg: Array<String>) {
		val gamePackage = LISGame::class.java.`package`
		val version = gamePackage.implementationVersion ?: "<version>"
		var title = gamePackage.implementationTitle ?: "<title>"

		val config = LwjglApplicationConfiguration()

		config.title = title

		config.width = 1280
		config.height= 1024

		LwjglApplication(LISGame(object : PlatformSupport(version, title) {}), config)
	}
}