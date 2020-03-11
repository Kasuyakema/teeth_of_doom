package com.kumascave.games.teeth_of_doom.core.world;

import com.badlogic.gdx.math.Vector2;
import com.kumascave.games.teeth_of_doom.core.Constants;

import lombok.NoArgsConstructor;

/**
 * A Position on the tile Grid
 *
 * @author kuma
 *
 */
@NoArgsConstructor
public class TPos extends GPos<TPos> {

	protected static final float gridsize = Constants.TILEGRID_SIZE;

	public TPos(int x, int y) {
		super(x, y);
	}

	@Override
	protected TPos _instance(int x, int y) {
		return new TPos(x, y);
	}

	public static TPos fromWorldPos(Vector2 wPos) {
		return new TPos(0, 0)._fromWorldPos(wPos);
	}

	@Override
	protected float getGridsize() {
		return gridsize;
	}
}
