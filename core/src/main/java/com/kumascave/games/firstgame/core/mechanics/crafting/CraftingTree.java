package com.kumascave.games.firstgame.core.mechanics.crafting;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.kumascave.games.firstgame.core.entity.Entity;
import com.kumascave.games.firstgame.core.entity.item.Stone;
import com.kumascave.games.firstgame.core.entity.item.weapon.PegLeg;
import com.kumascave.games.firstgame.core.entity.mobs.antropomorph.Inventory;
import com.kumascave.games.firstgame.core.physics.Pose;

public class CraftingTree {

	public static final Map<Class<?>, Recipe> RECIPIES = getRecipies();

	public static final Map<Class<?>, Supplier<Entity>> SUPPLIERS = getSuppliers();

	private static Map<Class<?>, Recipe> getRecipies() {
		Map<Class<?>, Recipe> map = new HashMap<>();

		// PegLeg
		Recipe pegLeg = new Recipe();
		pegLeg.put(Stone.class, 2);
		map.put(PegLeg.class, pegLeg);

		return map;
	}

	private static Map<Class<?>, Supplier<Entity>> getSuppliers() {
		Map<Class<?>, Supplier<Entity>> map = new HashMap<>();

		map.put(PegLeg.class, () -> new PegLeg(new Pose(0, 0, 0)));

		return map;
	}

	public static boolean canCraft(Class<?> clazz, Inventory inventory) {
		return RECIPIES.get(clazz).canCraft(inventory);
	}

	public static Entity craft(Class<?> clazz, Inventory inventory) {
		if (canCraft(clazz, inventory)) {
			inventory.removeItems(RECIPIES.get(clazz));
			return SUPPLIERS.get(clazz).get();
		}
		throw new IllegalArgumentException("unable to craft " + clazz.getName());
	}
}
