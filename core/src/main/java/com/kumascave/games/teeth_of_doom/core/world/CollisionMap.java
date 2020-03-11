package com.kumascave.games.teeth_of_doom.core.world;

import static com.kumascave.games.teeth_of_doom.core.Constants.CHUNK_SIZE;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.kumascave.games.teeth_of_doom.AppContext;
import com.kumascave.games.teeth_of_doom.ConfigKey;

import lombok.Getter;
import lombok.Setter;

public class CollisionMap extends TileMapView<Boolean, TPos> {

	private static final long serialVersionUID = 7496316412217228379L;
	@Getter
	private CPos focus;
	private static int lookahead = AppContext.inst().getConfigManager().getInteger(ConfigKey.WORLD_CHUNK_LOOKAHEAD);
	@Getter
	private static int size = CHUNK_SIZE * (1 + lookahead * 2);
	private ChunkMap map;
	@Setter
	private boolean mapChanged;

	public CollisionMap(CPos focus, ChunkMap map) {
		super(new CPos(focus.x - lookahead, focus.y - lookahead).toTPos(), size);
		this.focus = focus.clone();
		this.map = map;
		load();
	}

	private void load() {
		int xMax = focus.x + lookahead;
		int yMax = focus.y + lookahead;
		for (int x = focus.x - lookahead; x <= xMax; x++) {
			for (int y = focus.y - lookahead; y <= yMax; y++) {
				CPos pos = new CPos(x, y);
				map.getChunk(pos).forEach(t -> setTile(t.isColliding(), t.getGridPosition()));
			}
		}
	}

	@Override
	protected Boolean[][] createArray(int sizeX, int sizeY) {
		return new Boolean[sizeX][sizeY];
	}

	public List<ChainShape> findWalls() {
		int[][] hEdges = new int[size][size];
		int[][] vEdges = new int[size][size];
		for (int y = 1; y < size - 1; y++) {
			for (int x = 1; x < size - 1; x++) {
				boolean here = tiles[x][y];
				if (here != tiles[x + 1][y]) {
					vEdges[x][y] = here ? -1 : 1;
				}
				if (here != tiles[x][y + 1]) {
					hEdges[x][y] = here ? -1 : 1;
				}
			}
		}
		List<ChainShape> chains = new ArrayList<>();
		while (true) {
			TPos pos = getUnvisited(hEdges);
			boolean horizontal = true;
			if (pos == null) {
				pos = getUnvisited(vEdges);
				horizontal = false;
			}
			if (pos == null) {
				break;
			}
			List<Vector2> verticles = new ArrayList<>();

			while (pos != null) {
				erase(pos, horizontal, hEdges, vEdges);
				Duo<TPos, Boolean> next = findNext(pos, horizontal, hEdges, vEdges);
				Vector2 botR = new TPos(botLeft.x + pos.x + 1, botLeft.y + pos.y + (horizontal ? 1 : 0))
						.botLeftWorldPos();
				if (verticles.size() == 2) {
					// Reverse first Verticles if needed
					if (verticles.get(0).dst2(botR) < verticles.get(1).dst2(botR)) {
						verticles.add(verticles.remove(0));
					}
				}

				addIfValid(verticles, botR);
				if (horizontal) {
					Vector2 botL = new TPos(botLeft.x + pos.x, botLeft.y + pos.y + (horizontal ? 1 : 0))
							.botLeftWorldPos();
					addIfValid(verticles, botL);

				} else {
					Vector2 topR = new TPos(botLeft.x + pos.x + 1, botLeft.y + pos.y + 1 + (horizontal ? 1 : 0))
							.botLeftWorldPos();
					addIfValid(verticles, topR);
				}

				pos = next.a;
				horizontal = next.b;
			}
			ChainShape chain = new ChainShape();
			if (verticles.size() > 2) {
				if (verticles.get(0).equals(verticles.get(verticles.size() - 1))) {
					verticles.remove(verticles.size() - 1);
					chain.createLoop(verticles.toArray(new Vector2[1]));
				} else {
					chain.createChain(verticles.toArray(new Vector2[1]));
				}
				chain.setRadius(50f);
				chains.add(chain);
			}
			verticles = new ArrayList<>();
		}

		return chains;
	}

	private void addIfValid(List<Vector2> verticles, Vector2 candidate) {
		if (!verticles.contains(candidate)) {
			verticles.add(candidate);
			return;
		}
		for (int i = Math.min(3, verticles.size()); i > 0; i--) {
			if (verticles.get(verticles.size() - i).equals(candidate)) {
				return;
			}
		}
		verticles.add(candidate);
	}

	private Duo<TPos, Boolean> findNext(TPos pos, boolean hori, int[][] hEdges, int[][] vEdges) {
		int dir = Math.max(get(pos.x, pos.y, hori, hEdges, vEdges), 0);
		int x = pos.x;
		int y = pos.y;

		int xa = hori ? x : x + 1 - dir;
		int xb = hori ? x + 1 : x;
		int xc = hori ? x : x + dir;

		int ya = hori ? y + 1 - dir : y;
		int yb = hori ? y : y + 1;
		int yc = hori ? y + dir : y;

		if (get(xa, ya, !hori, hEdges, vEdges) != 0) {
			return new Duo<>(new TPos(xa, ya), !hori);
		}
		if (get(xb, yb, hori, hEdges, vEdges) != 0) {
			return new Duo<>(new TPos(xb, yb), hori); // TODO Auto-generated method stub
		}
		if (get(xc, yc, !hori, hEdges, vEdges) != 0) {
			return new Duo<>(new TPos(xc, yc), !hori);
		}
		// TODO: Second check can never happen?
		if (hori) {
			xa = xb = xc = x - 1;
		} else {
			ya = yb = yc = y - 1;
		}
		if (get(xa, ya, !hori, hEdges, vEdges) != 0) {
			return new Duo<>(new TPos(xa, ya), !hori);
		}
		if (get(xb, yb, hori, hEdges, vEdges) != 0) {
			return new Duo<>(new TPos(xb, yb), hori);
		}
		if (get(xc, yc, !hori, hEdges, vEdges) != 0) {
			return new Duo<>(new TPos(xc, yc), !hori);
		}
		return new Duo<>(null, hori);
	}

	private int get(int x, int y, boolean horizontal, int[][] hEdges, int[][] vEdges) {
		if (horizontal) {
			return hEdges[x][y];
		}
		return vEdges[x][y];
	}

	private void erase(TPos pos, boolean horizontal, int[][] hEdges, int[][] vEdges) {
		if (horizontal) {
			hEdges[pos.x][pos.y] = 0;
			return;
		}
		vEdges[pos.x][pos.y] = 0;
	}

	private TPos getUnvisited(int[][] edges) {
		for (int y = 1; y < edges[0].length - 1; y++) {
			for (int x = 1; x < edges.length - 1; x++) {
				if (edges[x][y] != 0) {
					return new TPos(x, y);
				}
			}
		}
		return null;
	}

	public void update(CPos newFocus) {
		if (!this.focus.equals(newFocus) || mapChanged) {
			this.focus = newFocus.clone();
			this.botLeft = new CPos(newFocus.x - lookahead, newFocus.y - lookahead).toTPos();
			Boolean[][] oldTiles = tiles;
			load();
			if (changed(oldTiles)) {
				firePropertyChange(PROPERTY_MAP, this, this);
			}
		}
	}

	private boolean changed(Boolean[][] oldTiles) {
		boolean result = false;
		for (int y = 1; y < oldTiles[0].length - 1; y++) {
			for (int x = 1; x < oldTiles.length - 1; x++) {
				result = result || tiles[x][y] ^ oldTiles[x][y];
			}
		}
		return result;
	}

	@Override
	protected TPos newGPos(int x, int y) {
		return new TPos(x, y);
	}
}
