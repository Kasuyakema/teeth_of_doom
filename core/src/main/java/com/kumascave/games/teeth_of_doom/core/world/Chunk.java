package com.kumascave.games.teeth_of_doom.core.world;

import static com.kumascave.games.teeth_of_doom.core.Constants.CHUNK_SIZE;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.save.Include;
import com.kumascave.games.teeth_of_doom.core.world.tiles.Floor;
import com.kumascave.games.teeth_of_doom.core.world.tiles.Tile;
import com.kumascave.games.teeth_of_doom.core.world.tiles.Wall;

import lombok.Getter;

public class Chunk extends TileMapView<Tile, TPos> implements Serializable {

	private static final String JSON_FIELD_TILES = "tiles";
	private static final String JSON_FIELD_CPOS = "cpos";

	private static final long serialVersionUID = 2979258424335482285L;

	@Getter
	@Include
	private CPos chunkPos;

	public Chunk() {
		tiles = new Tile[1][1];
	}

	public Chunk(CPos chunkPos) {
		this.botLeft = chunkPos.toTPos();
		this.chunkPos = chunkPos;
		this.tiles = generateChunk(chunkPos);
	}

	private Tile[][] generateChunk(CPos chunkPos) {
		TPos botLeft = chunkPos.toTPos();
		Tile[][] readTiles = new Tile[CHUNK_SIZE][CHUNK_SIZE];
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_SIZE; y++) {
				if (Math.abs(botLeft.x + x) > 15 || Math.abs(botLeft.y + y) > 15) {
					readTiles[x][y] = new Wall(botLeft.x + x, botLeft.y + y);
				} else {
					readTiles[x][y] = new Floor(botLeft.x + x, botLeft.y + y);
				}
			}
		}
		return readTiles;
	}

	// public void unload() {
	// LayeredStage stage = GameContext.inst().getGameStage();
	// forEach(tile -> stage.getLayer(LayeredStage.TILE_LAYER).removeActor(tile));
	// }

	@Override
	protected Tile[][] createArray(int sizeX, int sizeY) {
		// notNeeded
		return null;
	}

	public void setTile(Tile tile) {
		TPos pos = tile.getGridPosition();
		Tile old = getTile(pos);
		if (tile.isColliding() != old.isColliding()) {
			GameContext.getGameStage().getTileLayer().getMap().getCollisionMap().setMapChanged(true);
		}
		old.dispose();
		super.setTile(tile, pos);
	}

	@Override
	public void write(Json json) {
		json.writeValue(JSON_FIELD_CPOS, chunkPos);
		json.writeValue(JSON_FIELD_TILES, tiles);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		// tiles = json.readValue(tiles.getClass(),
		// jsonData.getChild(JSON_FIELD_TILES));
		chunkPos = json.readValue(CPos.class, jsonData.getChild(JSON_FIELD_CPOS));
	}

	@Override
	protected TPos newGPos(int x, int y) {
		return new TPos(x, y);
	}

}
