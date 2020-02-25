package com.kumascave.games.firstgame.core.world.tiles;

public class Floor extends Tile {

	@SuppressWarnings("unused")
	private Floor() {
		super(0, 0, false);
	}

	public Floor(int x, int y) {
		super(x, y, false);
	}

	@Override
	public void destroy() {
		// do nothing
	}

}
