package com.retronicgames.lis.platform

import com.badlogic.gdx.backends.iosrobovm.IOSApplication
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration
import com.retronicgames.api.gdx.PlatformSupport
import com.retronicgames.lis.LISGame
import org.robovm.apple.foundation.NSAutoreleasePool
import org.robovm.apple.foundation.NSBundle
import org.robovm.apple.uikit.UIApplication

object IOSLauncher : IOSApplication.Delegate() {
	override fun createApplication(): IOSApplication {
		val config = IOSApplicationConfiguration()
		config.orientationLandscape = true
		config.orientationPortrait = false

		val version = NSBundle.getMainBundle().getInfoDictionaryObject("CFBundleShortVersionString").toString()

		return IOSApplication(LISGame(object : PlatformSupport(version) {}), config);
	}

	@JvmStatic public fun main(argv: Array<String>) {
		val pool = NSAutoreleasePool()
		UIApplication.main<UIApplication, IOSLauncher>(argv, null, IOSLauncher::class.java)
		pool.close()
	}
}