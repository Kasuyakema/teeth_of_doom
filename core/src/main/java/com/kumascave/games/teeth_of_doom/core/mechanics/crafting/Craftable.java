package com.kumascave.games.teeth_of_doom.core.mechanics.crafting;

public interface Craftable {

	public static Object factory() {
		if (getFactory() == null) {
			createFactory();
		}
		return getFactory();

	}

	private static void createFactory() {
		// TODO Auto-generated method stub
	}

	public static Object getFactory() {
		// TODO Auto-generated method stub
		return null;
	}

}
