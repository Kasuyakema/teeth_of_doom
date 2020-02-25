package com.kumascave.games.firstgame.core.mechanics.crafting;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class CraftingTreeTest {

	@Test
	void test() {
		if (!CraftingTree.RECIPIES.keySet().stream().allMatch(x -> CraftingTree.SUPPLIERS.keySet().contains(x))) {
			fail("Crafting System is missing a Supplier");
		}
	}

}
