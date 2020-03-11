package com.kumascave.games.teeth_of_doom.core.world;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import com.jgoodies.binding.beans.Model;
import com.kumascave.games.teeth_of_doom.core.ai.navigation.Neighbourhood;
import com.kumascave.games.teeth_of_doom.core.ai.navigation.Neighbourhood.Position;
import com.kumascave.games.teeth_of_doom.core.save.Include;
import com.kumascave.games.teeth_of_doom.util.Util;

import lombok.Getter;

public abstract class TileMapView<T, P extends GPos<P>> extends Model {

	private static final long serialVersionUID = 4670104237494320576L;

	public static final String PROPERTY_MAP = "map";
	public static final String PROPERTY_PREFIX_TILE = "tile";

	@Getter
	@Include
	protected T[][] tiles;

	@Getter
	@Include
	protected P botLeft;

	public TileMapView(P botleft, int size) {
		super();
		tiles = createArray(size);
		botLeft = botleft;
	}

	protected TileMapView() {
		super();
	}

	protected T[][] createArray(int size) {
		return createArray(size, size);
	}

	protected abstract T[][] createArray(int sizeX, int sizeY);

	public T getTile(int x, int y) {
		return tiles[x - botLeft.x][y - botLeft.y];
	}

	public T getTile(P pos) {
		return getTile(pos.x, pos.y);
	}

	private T getTileOrNull(int x, int y) {
		return getTileOrNull(newGPos(x, y));
	}

	private T getTileOrNull(P pos) {
		P local = toLocalGPos(pos);
		if (local.x < 0 || local.x >= tiles.length || local.y < 0 || local.y >= tiles.length) {
			return null;
		} else {
			return getTile(pos.x, pos.y);
		}
	}

	public void setTile(T tile, P pos) {
		T oldTile = getTile(pos);
		tiles[pos.x - botLeft.x][pos.y - botLeft.y] = tile;
		if (tile != oldTile) {
			firePropertyChange(PROPERTY_PREFIX_TILE + pos, oldTile, tile);
		}
	}

	public void forEach(Consumer<T> func) {
		for (T[] tile : tiles) {
			for (int y = 0; y < tiles.length; y++) {
				func.accept(tile[y]);
			}
		}
	}

	public void iterate(BiConsumer<P, T> func) {
		Util.iterate(tiles.length - 1, (x, y) -> {
			P pos = botLeft.add(x, y);
			func.accept(pos, getTile(pos));
		});
	}

	public T[][] getTiles(P cutBotLeft, P cutTopRight) {
		int sizeX = cutTopRight.x - cutBotLeft.x + 1;
		int sizeY = cutTopRight.y - cutBotLeft.y + 1;
		T[][] result = createArray(sizeX, sizeY);

		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				result[x][y] = getTileOrNull(cutBotLeft.x + x, cutBotLeft.y + y);
			}
		}
		return result;
	}

	public Neighbourhood<T> getNeighbourhood(P pos) {
		Neighbourhood<T> neigbours = new Neighbourhood<>();
		neigbours.put(Position.TOP_RIGHT, getTileOrNull(pos.add(1, 1)));
		neigbours.put(Position.TOP_LEFT, getTileOrNull(pos.add(-1, 1)));
		neigbours.put(Position.BOT_LEFT, getTileOrNull(pos.add(-1, -1)));
		neigbours.put(Position.BOT_RIGHT, getTileOrNull(pos.add(1, -1)));
		neigbours.put(Position.TOP_MID, getTileOrNull(pos.add(0, 1)));
		neigbours.put(Position.BOT_MID, getTileOrNull(pos.add(0, -1)));
		neigbours.put(Position.MID_LEFT, getTileOrNull(pos.add(-1, 0)));
		neigbours.put(Position.MID_RIGHT, getTileOrNull(pos.add(1, 0)));
		return neigbours;
	}

	private P toLocalGPos(P global) {
		return global.sub(botLeft);
	}

	protected void initTiles(Function<P, T> supplier) {
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles.length; y++) {
				tiles[x][y] = supplier.apply(newGPos(x, y));
			}
		}
	}

	protected abstract P newGPos(int x, int y);

	public boolean contains(P pos) {
		return pos.x >= botLeft.x && pos.x < botLeft.x + tiles.length && pos.y >= botLeft.y
				&& pos.y < botLeft.y + tiles.length;
	}

}
