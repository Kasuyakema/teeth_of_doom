package com.kumascave.games.firstgame.core.world;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.kumascave.games.firstgame.AppContext;
import com.kumascave.games.firstgame.ConfigKey;
import com.kumascave.games.firstgame.core.Constants;
import com.kumascave.games.firstgame.core.GameContext;
import com.kumascave.games.firstgame.core.save.SafeManager;
import com.kumascave.games.firstgame.core.world.tiles.Tile;
import com.kumascave.games.firstgame.core.world.tiles.Wall;

import lombok.Getter;

public class ChunkMap extends Action {

	@Getter
	private Map<CPos, Chunk> chunks = new HashMap<>();

	private CPos focus;

	private static final GPos DIFF_TOP = new GPos(0, 1);
	private static final GPos DIFF_RIGHT = new GPos(1, 0);
	private static final GPos DIFF_BOT = new GPos(0, -1);
	private static final GPos DIFF_LEFT = new GPos(-1, 0);

	private CollisionMap collisionMap;

	public ChunkMap(LayeredStage layeredStage, CPos focus) {
		super();
		layeredStage.addAction(this);
		this.focus = focus;
		load();
		collisionMap = new CollisionMap(focus, this);
	}

	private void updateFocus() {
		CPos newFocus = CPos.fromGPos(GPos.fromWorldPos(GameContext.inst().getPlayer().getBody().getPosition()));

		if (!newFocus.equals(focus)) {
			this.focus = newFocus;
			updateChunks();
		}
	}

	private void updateChunks() {
		// TODO: Forget is not needed for the minimum game
		// forget();
		load();
	}

	private void forget() {
		int forgetRange = AppContext.inst().getConfigManager().getInteger(ConfigKey.WORLD_CHUNK_FORGET);
		int xMax = focus.x + forgetRange;
		int xMin = focus.x - forgetRange;
		int yMax = focus.y + forgetRange;
		int yMin = focus.y - forgetRange;

		Set<CPos> keysToForget = new HashSet<>();

		for (Chunk chunk : chunks.values()) {
			CPos pos = chunk.getChunkPos();
			if (pos.x < xMin || pos.x > xMax || pos.y < yMin || pos.y > yMax) {
				keysToForget.add(pos);
				// chunk.unload();
			}
		}
		keysToForget.forEach(key -> chunks.remove(key));

	}

	private void load() {
		// chunks = SafeManager.readChunks();
		int lookahead = AppContext.inst().getConfigManager().getInteger(ConfigKey.WORLD_CHUNK_LOOKAHEAD);
		int xMax = focus.x + lookahead;
		int yMax = focus.y + lookahead;
		for (int x = focus.x - lookahead; x <= xMax; x++) {
			for (int y = focus.y - lookahead; y <= yMax; y++) {
				CPos pos = new CPos(x, y);
				if (!chunks.containsKey(pos)) {
					load(pos);
				}
			}
		}
	}

	private void load(CPos pos) {
		Chunk chunk;
		if (SafeManager.chunkExists(pos)) {
			try {
				chunk = SafeManager.readChunk(pos);
			} catch (IOException e) {
				throw new RuntimeException("map files unreadable for Cpos " + pos.toString(), e);
			}
		} else {
			chunk = new Chunk(pos);
		}
		chunks.put(pos.clone(), chunk);
	}

	public Tile getTile(GPos pos) {
		return chunks.get(CPos.fromGPos(pos)).getTile(pos);
	}

	public void setTile(Tile tile) {
		chunks.get(CPos.fromGPos(tile.getGridPosition())).setTile(tile);
	}

	public Tile[][] getTiles(GPos botLeft, GPos topRight) {
		int width = topRight.x - botLeft.x + 1;
		int heigth = topRight.y - botLeft.y + 1;
		Tile[][] tiles = new Tile[width][heigth];
		// TODO: optimize
		// for (Chunk chunk : chunks.values()) {
		// Tile[][] part = chunk.getTiles(botLeft, topRight);
		// if (part.length > 0) {
		// GPos chunkBotLeft = chunk.getBotLeft();
		//
		// }
		// }

		for (int y = 0; y < heigth - 1; y++) {
			for (int x = 0; x < width - 1; x++) {
				tiles[x][y] = getTile(new GPos(botLeft.x + x, botLeft.y + y));
			}
		}

		return tiles;
	}

	@Override
	public boolean act(float delta) {
		updateFocus();
		collisionMap.update(focus);
		// new Thread(() -> collisionMap.update(focus)).start();
		return false;
	}

	public CollisionMap getcollisionMap() {
		return collisionMap;
	}

	public Wall getNearestWall(Vector2 pos) {
		GPos gPos = GPos.fromWorldPos(pos);
		Tile candidate = getTile(gPos);
		if (candidate instanceof Wall) {
			return (Wall) candidate;
		}
		Map<Float, GPos> checks = new HashMap<>();
		float diffL = Math.abs(pos.x - gPos.toWorldPos().x);
		diffL = diffL * diffL;
		float diffR = Constants.GRIDSIZE - diffL;
		diffR = diffR * diffR;
		checks.put(diffL, DIFF_LEFT);
		checks.put(diffR, DIFF_RIGHT);
		float diffB = Math.abs(pos.y - gPos.toWorldPos().y);
		diffB = diffB * diffB;
		float diffT = Constants.GRIDSIZE - diffB;
		diffT = diffT * diffT;
		checks.put(diffB, DIFF_BOT);
		checks.put(diffT, DIFF_TOP);

		// Diagonals
		checks.put(diffB + diffL, DIFF_BOT.add(DIFF_LEFT));
		checks.put(diffB + diffR, DIFF_BOT.add(DIFF_RIGHT));
		checks.put(diffT + diffL, DIFF_TOP.add(DIFF_LEFT));
		checks.put(diffT + diffR, DIFF_TOP.add(DIFF_RIGHT));

		// List of diffs sortet by distance to target point
		List<GPos> checksl = checks.entrySet().stream().sorted((x, y) -> Float.compare(x.getKey(), y.getKey()))
				.map(x -> x.getValue()).collect(Collectors.toList());

		for (GPos diff : checksl) {
			candidate = getTile(gPos.add(diff));
			if (candidate instanceof Wall) {
				return (Wall) candidate;
			}
		}
		return null;
	}

	public Chunk getChunk(CPos pos) {
		if (!chunks.containsKey(pos)) {
			load(pos);
		}
		return chunks.get(pos);
	}
}
