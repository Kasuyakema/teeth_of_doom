package com.kumascave.games.firstgame.core.world;

import com.badlogic.gdx.math.Vector2;
import com.kumascave.games.firstgame.core.Constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A Position on the chunk Grid
 *
 * @author kuma
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CPos {
	public int x;
	public int y;

	public static CPos fromWorldPos(Vector2 wPos) {
		return CPos.fromGPos(GPos.fromWorldPos(wPos));
	}

	public static CPos fromGPos(GPos pos) {
		int x = (int) Math.floor((float) pos.x / Constants.CHUNKSIZE);
		int y = (int) Math.floor((float) pos.y / Constants.CHUNKSIZE);
		return new CPos(x, y);
	}

	@Override
	public CPos clone() {
		return new CPos(this.x, this.y);
	}

	public Vector2 toWorldPos() {
		return toGPos().toWorldPos();
	}

	public GPos toGPos() {
		return new GPos(x * Constants.CHUNKSIZE, y * Constants.CHUNKSIZE);
	}

	public CPos add(CPos diff) {
		return new CPos(this.x + diff.x, this.y + diff.y);
	}

	public CPos add(int x, int y) {
		return add(new CPos(x, y));
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

}
