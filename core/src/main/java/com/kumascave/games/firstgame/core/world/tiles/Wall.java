package com.kumascave.games.firstgame.core.world.tiles;

import com.kumascave.games.firstgame.core.GameContext;
import com.kumascave.games.firstgame.core.world.GPos;

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
		GPos pos = getGridPosition();
		GameContext.inst().getGameStage().getTileLayer().setTile(new Floor(pos.x, pos.y));
	}

}
