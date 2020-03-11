package com.kumascave.games.firstgame.forlater;

import static com.kumascave.games.firstgame.core.Constants.CHUNKSIZE;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.kumascave.games.firstgame.core.world.CPos;
import com.kumascave.games.firstgame.core.world.GPos;
import com.kumascave.games.firstgame.core.world.TileMapView;
import com.kumascave.games.firstgame.core.world.tiles.Floor;
import com.kumascave.games.firstgame.core.world.tiles.Tile;
import com.kumascave.games.firstgame.core.world.tiles.Wall;

public class Bucket extends TileMapView<BucketTile> {

	private static final long serialVersionUID = 4062659740183779567L;
	private static final short STONE = 10;
	private static final short BIG_CAVE = -10;
	private static final short BIG_SPACING = -11;

	private static int bucketsize = 3;

	private int bigCave = 15;
	private int bigCaveGrid = 30;
	private int bigCaveSpacing = 3;

	private long seed = 123456789;

	public Bucket() {
		super(new GPos(0, 0), CHUNKSIZE * bucketsize);
	}

	@Override
	protected BucketTile[][] createArray(int size) {
		return new BucketTile[size][size];
	}

	public Tile[][] generateChunk(CPos pos) {
		int offset = -bucketsize / 2;
		botLeft = pos.add(offset, offset).toGPos();
		// initTiles(x -> new BucketTile());
		// placeBigCaves();

		// fillStone();

		return readChunk(pos);
	}

	private void fillStone() {
		forEach(x -> {
			if (x.info.isEmpty()) {
				x.info.add(STONE);
			}
		});
	}

	private Tile[][] readChunk(CPos pos) {
		GPos botLeft = pos.toGPos();
		Tile[][] readTiles = new Tile[CHUNKSIZE][CHUNKSIZE];
		for (int x = 0; x < CHUNKSIZE; x++) {
			for (int y = 0; y < CHUNKSIZE; y++) {
				if (Math.abs(botLeft.x + x) > 15 || Math.abs(botLeft.y + y) > 15) {
					readTiles[x][y] = new Wall(botLeft.x + x, botLeft.y + y);
				} else {
					readTiles[x][y] = new Floor(botLeft.x + x, botLeft.y + y);
				}
			}
		}
		return readTiles;
	}

	private void placeBigCaves() {
		List<GPos> bigCaves = new ArrayList<>();
		for (int x = 0; x < tiles.length / bigCaveGrid; x++) {
			for (int y = 0; y < tiles.length / bigCaveGrid; y++) {
				GPos pos = new GPos(x, y);
				if (pos.equals(new GPos(0, 0)) || new Random(applyToSeed(pos.add(botLeft))).nextFloat() < 0.05f) {
					// TODO: distort
					bigCaves.add(pos);
				}
			}
		}

		for (GPos pos : bigCaves) {
			for (int x = pos.x - bigCave; x < pos.x + bigCave; x++) {
				for (int y = pos.y - bigCave; x < pos.y + bigCave; y++) {
					getTile(new GPos(x, y).add(botLeft)).info.add(BIG_CAVE);
				}
			}
		}
	}

	private void resetTiles() {
		for (BucketTile[] tileLine : tiles) {
			for (BucketTile tile : tileLine) {
				tile.info.clear();
			}
		}
	}

	private Tile parseTile(BucketTile bTile, GPos pos) {
		if (bTile.info.contains(STONE)) {
			return new Wall(pos.x, pos.y);
		}
		return new Floor(pos.x, pos.y);
	}

	private long applyToSeed(GPos pos) {
		return applyToSeed(pos.x, pos.y);
	}

	private long applyToSeed(int x, int y) {
		return seed + x + 1000000000 * y;
	}

	@Override
	public void setTile(BucketTile tile, GPos pos) {
		try {
			super.setTile(tile, pos);
		} catch (IndexOutOfBoundsException e) {
			// ignore
		}
	}

	@Override
	public BucketTile getTile(GPos pos) {
		try {
			return super.getTile(pos);
		} catch (IndexOutOfBoundsException e) {
			return new BucketTile();
		}
	}
}
