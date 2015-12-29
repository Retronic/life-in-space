/**
 * Copyright (C) 2015 Oleg Dolya
 * Copyright (C) 2015 Eduardo Garcia
 *
 * This file is part of Life in Space, by Retronic Games
 *
 * Life in Space is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Life in Space is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Life in Space.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.retronicgames.lis.mission.resources

import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.OrderedMap
import com.retronicgames.gdx.set
import com.retronicgames.lis.mission.Mission
import com.retronicgames.utils.value.MutableValue

class ResourceManager(mission: Mission):Iterable<ObjectMap.Entry<ResourceType, MutableValue<Int>>> {
	private val resources = OrderedMap<ResourceType, MutableValue<Int>>(ResourceType.values().size)

	init {
		ResourceType.values().forEach { resourceType ->
			resources[resourceType] = MutableValue(0)
		}
		mission.initialResources().forEach { entry ->
			resources[entry.key].value = entry.value
		}
	}

	override fun iterator() = resources.iterator()

	fun getResourceQuantity(type: ResourceType) = resources.get(type).value
}

