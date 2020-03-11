package com.kumascave.games.teeth_of_doom.core.world;

import com.badlogic.gdx.math.Vector2;
import com.kumascave.games.teeth_of_doom.core.Constants;

import lombok.NoArgsConstructor;

/**
 * A Position on the planning Grid
 *
 * @author kuma
 *
 */
@NoArgsConstructor
public class PPos extends GPos<PPos> {

	protected static final float gridsize = Constants.PLANNINGGRID_SIZE;

	public PPos(int x, int y) {
		super(x, y);
	}

	@Override
	protected PPos _instance(int x, int y) {
		return new PPos(x, y);
	}

	public static PPos fromWorldPos(Vector2 wPos) {
		return new PPos(0, 0)._fromWorldPos(wPos);
	}

	public static PPos fromTPos(TPos pos) {
		return new PPos(pos.x * Constants.PLANNINGGRID_RESOLUTION, pos.y * Constants.PLANNINGGRID_RESOLUTION);
	}

	@Override
	protected float getGridsize() {
		return gridsize;
	}
}
