package com.retronicgames.lis.platform

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.retronicgames.api.gdx.PlatformSupport
import com.retronicgames.lis.LISGame

object DesktopLauncher {
	@JvmStatic fun main(arg: Array<String>) {
		val gamePackage = LISGame::class.java.`package`
		val version = gamePackage.specificationVersion ?: "<version>"
		var title = gamePackage.implementationTitle ?: "<title>"

		val config = LwjglApplicationConfiguration()

		config.title = title

		LwjglApplication(LISGame(object : PlatformSupport(version) {}), config)
	}
}