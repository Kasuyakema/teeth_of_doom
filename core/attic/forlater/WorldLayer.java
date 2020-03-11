package com.kumascave.games.firstgame.forlater;

import com.kumascave.games.firstgame.core.world.CPos;
import com.kumascave.games.firstgame.core.world.tiles.Tile;

public class WorldLayer {
	private Bucket bucket;

	public WorldLayer() {
		this.bucket = new Bucket();
	}

	public Tile[][] getChunk(CPos chunkPos) {
		return bucket.generateChunk(chunkPos);
		// Tile[][] tiles = new Tile[CHUNKSIZE][CHUNKSIZE];
		// GPos botLeft = chunkPos.toGPos();
		// for (int x = 0; x < CHUNKSIZE; x++) {
		// for (int y = 0; y < CHUNKSIZE; y++) {
		// if (Math.abs(botLeft.x + x) > 15 || Math.abs(botLeft.y + y) > 15) {
		// tiles[x][y] = new Wall(botLeft.x + x, botLeft.y + y);
		// } else {
		// tiles[x][y] = new Floor(botLeft.x + x, botLeft.y + y);
		// }
		// }
		// }
		// return tiles;
	}

}
