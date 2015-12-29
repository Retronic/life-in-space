package com.retronicgames.gdx

import com.badlogic.gdx.maps.MapLayer
import com.retronicgames.lis.visual.VisualMapObject
import java.util.Comparator

class RGObjectsOnlyMapLayer : MapLayer() {
	// FIXME: This is too convoluted, just because we can't modify the underlying `objects` instance
	fun add(obj: VisualMapObject<*>) {
		objects.add(obj)
		val ordered = objects.sortedWith(Comparator<com.badlogic.gdx.maps.MapObject> { o1, o2 ->
			val s1 = (o1 as VisualMapObject<*>).sprite
			val s2 = (o2 as VisualMapObject<*>).sprite

			when {
				s1.y > s2.y -> -1
				s2.y > s1.y -> 1
				else -> {
					when {
						s1.x > s2.x -> 1
						s2.x > s1.x -> -1
						else -> 0
					}
				}
			}
		})

		val it = objects.iterator()
		while (it.hasNext()) {
			it.next()
			it.remove()
		}

		ordered.forEach { objects.add(it) }
	}
}