package com.kumascave.games.firstgame.core.world;

import java.util.function.Consumer;
import java.util.function.Function;

import com.jgoodies.binding.beans.Model;
import com.kumascave.games.firstgame.core.save.Include;
import com.kumascave.games.firstgame.core.world.tiles.Tile;

import lombok.Getter;

public abstract class TileMapView<T> extends Model {

	private static final long serialVersionUID = 4670104237494320576L;

	public static final String PROPERTY_MAP = "map";
	public static final String PROPERTY_PREFIX_TILE = "tile";

	@Getter
	@Include
	protected T[][] tiles;

	@Getter
	@Include
	protected GPos botLeft;

	public TileMapView(GPos botleft, int size) {
		super();
		tiles = createArray(size);
		botLeft = botleft;
	}

	protected TileMapView() {
		super();
	}

	protected abstract T[][] createArray(int size);

	public T getTile(GPos pos) {
		return tiles[pos.x - botLeft.x][pos.y - botLeft.y];
	}

	public void setTile(T tile, GPos pos) {
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

	public Tile[][] getTiles(GPos botLeft, GPos topRight) {
		if (topRight.x < this.botLeft.x || botLeft.x < this.botLeft.x + tiles.length || topRight.y < this.botLeft.y
				|| botLeft.y > this.botLeft.y + tiles.length) {
			// outside of Chunk
			return new Tile[0][0];
		}
		// TODO: clip and return
		throw new RuntimeException("not jet implemented");
	}

	protected void initTiles(Function<GPos, T> supplier) {
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles.length; y++) {
				tiles[x][y] = supplier.apply(new GPos(x, y));
			}
		}
	}

	public boolean contains(GPos pos) {
		return pos.x >= botLeft.x && pos.x < botLeft.x + tiles.length && pos.y >= botLeft.y
				&& pos.y < botLeft.y + tiles.length;
	}

}
