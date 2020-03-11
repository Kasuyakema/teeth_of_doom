package com.kumascave.games.teeth_of_doom.core.mechanics.crafting;

import org.junit.Assert;
import org.junit.Test;

public class CraftingTreeTest {

	@Test
	public void test() {
		if (!CraftingTree.RECIPIES.keySet().stream().allMatch(x -> CraftingTree.SUPPLIERS.keySet().contains(x))) {
			Assert.fail("Crafting System is missing a Supplier");
		}
	}

}
