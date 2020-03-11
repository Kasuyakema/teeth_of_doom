package com.kumascave.games.teeth_of_doom.core.world.tiles;

import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.world.TPos;

public class Wall extends Tile {

	@SuppressWarnings("unused")
	private Wall() {
		super(0, 0, true);
	}

	public Wall(int x, int y) {
		super(x, y, true);
	}

	@Override
	public void destroy() {
		// TODO: spawn Loot :D a Boulder?
		TPos pos = getGridPosition();
		GameContext.inst();
		GameContext.getGameStage().getTileLayer().setTile(new Floor(pos.x, pos.y));
	}

}
