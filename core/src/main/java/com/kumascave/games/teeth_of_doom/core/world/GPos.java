package com.kumascave.games.teeth_of_doom.core.world;

import com.badlogic.gdx.math.Vector2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A Position on a Grid
 *
 * @author kuma
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public abstract class GPos<T extends GPos> {
	public int x;
	public int y;

	protected T _fromWorldPos(Vector2 wPos) {
		int x = (int) Math.floor(wPos.x / getGridsize());
		int y = (int) Math.floor(wPos.y / getGridsize());
		return _instance(x, y);
	}

	protected abstract float getGridsize();

	protected abstract T _instance(int x, int y);

	@Override
	public T clone() {
		return _instance(this.x, this.y);
	}

	public Vector2 botLeftWorldPos() {
		return new Vector2(x * getGridsize(), y * getGridsize());
	}

	public Vector2 centerWorldPos() {
		return new Vector2((x + .5f) * getGridsize(), (y + .5f) * getGridsize());
	}

	public T add(T diff) {
		return _instance(this.x + diff.x, this.y + diff.y);
	}

	public T add(int diffX, int diffY) {
		return add(_instance(diffX, diffY));
	}

	public T sub(T diff) {
		return _instance(this.x - diff.x, this.y - diff.y);
	}

	public T sub(int diffX, int diffY) {
		return sub(_instance(diffX, diffY));
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
