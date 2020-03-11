package com.kumascave.games.teeth_of_doom.core.world;

import com.badlogic.gdx.math.Vector2;
import com.kumascave.games.teeth_of_doom.core.Constants;

import lombok.NoArgsConstructor;

/**
 * A Position on the chunk Grid
 *
 * @author kuma
 *
 */
@NoArgsConstructor
public class CPos extends GPos<CPos> {

	protected static final float gridsize = Constants.CHUNK_SIZE * Constants.TILEGRID_SIZE;

	public CPos(int x, int y) {
		super(x, y);
	}

	@Override
	protected CPos _instance(int x, int y) {
		return new CPos(x, y);
	}

	public static CPos fromWorldPos(Vector2 wPos) {
		return new CPos(0, 0)._fromWorldPos(wPos);
	}

	public static CPos fromTPos(TPos pos) {
		int x = (int) Math.floor((float) pos.x / Constants.CHUNK_SIZE);
		int y = (int) Math.floor((float) pos.y / Constants.CHUNK_SIZE);
		return new CPos(x, y);
	}

	public TPos toTPos() {
		return new TPos(x * Constants.CHUNK_SIZE, y * Constants.CHUNK_SIZE);
	}

	@Override
	protected float getGridsize() {
		return gridsize;
	}

}
