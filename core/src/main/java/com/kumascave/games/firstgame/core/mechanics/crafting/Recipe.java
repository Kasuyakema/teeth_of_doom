package com.kumascave.games.firstgame.core.mechanics.crafting;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.kumascave.games.firstgame.core.entity.item.Item;
import com.kumascave.games.firstgame.core.entity.mobs.antropomorph.Inventory;

public class Recipe extends HashMap<Class<? extends Item>, Integer> {
	private static final long serialVersionUID = -7972276156421415498L;

	public boolean canCraft(Inventory inventory) {
		List<Class<? extends Item>> items = inventory.getList().stream().map(x -> x.getClass())
				.collect(Collectors.toList());
		for (Entry<Class<? extends Item>, Integer> e : entrySet()) {
			Class<? extends Item> itemType = e.getKey();
			int required = e.getValue();
			long count = items.stream().filter(x -> x.equals(itemType)).count();
			if (count < required) {
				return false;
			}
		}

		return true;
	}
}
