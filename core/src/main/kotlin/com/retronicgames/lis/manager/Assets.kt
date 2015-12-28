package com.retronicgames.lis.manager

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.retronicgames.utils.NinePatchSprite

object Assets {
	private val manager = AssetManager()

	fun tex(name: String): Texture {
		return Texture("textures/$name.png")
	}

	fun textureAtlas(atlas: String): TextureAtlas? {
		val atlasPath = "textures/$atlas.atlas"
		if (!manager.isLoaded(atlasPath)) {
			manager.load(atlasPath, TextureAtlas::class.java)
			manager.finishLoadingAsset(atlasPath)
		}
		return manager.get(atlasPath, TextureAtlas::class.java)
	}

	fun sprite(atlas: String, sprite: String, idx: Int = -1): Sprite {
		val textureAtlas = textureAtlas(atlas)
		// FIXME: Cache!
		return textureAtlas!!.createSprite(sprite, idx) ?: throw RuntimeException("Can't find the sprite '$sprite' with index $idx on the atlas '$atlas'")
	}

	fun ninePatch(atlas: String, id: String): NinePatch {
		val textureAtlas = textureAtlas(atlas)
		// FIXME: Cache!
		return textureAtlas!!.createPatch(id)
	}

	fun ninePatchSprite(atlas: String, id: String): NinePatchSprite {
		val ninePatch = ninePatch(atlas, id)

		return NinePatchSprite(ninePatch)
	}
}
