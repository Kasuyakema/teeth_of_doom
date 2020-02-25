package com.kumascave.games.firstgame.core.world;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.kumascave.games.firstgame.core.GameContext;
import com.kumascave.games.firstgame.core.save.Include;
import com.kumascave.games.firstgame.core.world.tiles.Tile;
import com.kumascave.games.firstgame.forlater.Bucket;

import lombok.Getter;

public class Chunk extends TileMapView<Tile> implements Serializable {

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
		this.botLeft = chunkPos.toGPos();
		this.chunkPos = chunkPos;
		this.tiles = new Bucket().generateChunk(chunkPos);
	}

	// public void unload() {
	// LayeredStage stage = GameContext.inst().getGameStage();
	// forEach(tile -> stage.getLayer(LayeredStage.TILE_LAYER).removeActor(tile));
	// }

	@Override
	protected Tile[][] createArray(int size) {
		// notNeeded
		return null;
	}

	public void setTile(Tile tile) {
		GPos pos = tile.getGridPosition();
		Tile old = getTile(pos);
		if (tile.isColliding() != old.isColliding()) {
			GameContext.inst().getGameStage().getTileLayer().getMap().getcollisionMap().setMapChanged(true);
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

}
