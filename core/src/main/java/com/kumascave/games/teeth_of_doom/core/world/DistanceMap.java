package com.kumascave.games.teeth_of_doom.core.world;

import com.kumascave.games.teeth_of_doom.core.Constants;
import com.kumascave.games.teeth_of_doom.util.Util;

public class DistanceMap extends TileMapView<Float, PPos> {

	private static final long serialVersionUID = -1775678461821490663L;

	CollisionMap cMap;

	public DistanceMap(CollisionMap collisionMap) {
		super(PPos.fromTPos(collisionMap.getBotLeft()), CollisionMap.getSize() * Constants.PLANNINGGRID_RESOLUTION);
		cMap = collisionMap;
		cMap.addPropertyChangeListener(CollisionMap.PROPERTY_MAP, evt -> update());
		update();
	}

	private void update() {
		Util.iterate(tiles.length, (x, y) -> tiles[x][y] = Float.MAX_VALUE);

		cMap.iterate((pos, b) -> {
			if (b) {
				PPos botLeft = PPos.fromTPos(pos);
				Util.iterate(Constants.PLANNINGGRID_RESOLUTION, Constants.PLANNINGGRID_RESOLUTION, (x, y) -> {
					updateTile(0f, botLeft.sub(this.botLeft).add(x, y));
				});
			}
		});
	}

	@Override
	protected Float[][] createArray(int sizeX, int sizeY) {
		return new Float[sizeX][sizeY];
	}

	@Override
	protected PPos newGPos(int x, int y) {
		return new PPos(x, y);
	}

	private void updateTile(float dist, PPos pos) {
		if (pos.x >= 0 && pos.x < tiles.length && pos.y >= 0 && pos.y < tiles.length) {
			if (dist < Constants.DISTANCEMAP_IRRELEVANCE && tiles[pos.x][pos.y] > dist) {
				tiles[pos.x][pos.y] = dist;
				updateTile(dist + Constants.PLANNINGGRID_SIZE, pos.add(1, 1));
				updateTile(dist + Constants.PLANNINGGRID_SIZE, pos.add(1, -1));
				updateTile(dist + Constants.PLANNINGGRID_SIZE, pos.add(-1, 1));
				updateTile(dist + Constants.PLANNINGGRID_SIZE, pos.add(-1, -1));
				updateTile(dist + Constants.PLANNINGGRID_SIZE, pos.add(0, 1));
				updateTile(dist + Constants.PLANNINGGRID_SIZE, pos.add(1, 0));
				updateTile(dist + Constants.PLANNINGGRID_SIZE, pos.add(-1, 0));
				updateTile(dist + Constants.PLANNINGGRID_SIZE, pos.add(0, -1));
			}
		}
	}
}
