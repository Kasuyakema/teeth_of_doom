package com.kumascave.games.firstgame.core.world;

import com.badlogic.gdx.math.Vector2;
import com.kumascave.games.firstgame.core.Constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A Position on the tile Grid
 *
 * @author kuma
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class GPos {
	public int x;
	public int y;

	public static GPos fromWorldPos(Vector2 wPos) {
		int x = (int) Math.floor(wPos.x / Constants.GRIDSIZE);
		int y = (int) Math.floor(wPos.y / Constants.GRIDSIZE);
		return new GPos(x, y);
	}

	@Override
	public GPos clone() {
		return new GPos(this.x, this.y);
	}

	public Vector2 toWorldPos() {
		return new Vector2(x * Constants.GRIDSIZE, y * Constants.GRIDSIZE);
	}

	public GPos add(GPos diff) {
		return new GPos(this.x + diff.x, this.y + diff.y);
	}

	public GPos add(int diffX, int diffY) {
		return add(new GPos(diffX, diffY));
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
