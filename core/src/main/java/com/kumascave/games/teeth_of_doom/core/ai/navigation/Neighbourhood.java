package com.kumascave.games.teeth_of_doom.core.ai.navigation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

import com.kumascave.games.teeth_of_doom.core.world.Duo;

public class Neighbourhood<T>
		extends HashMap<com.kumascave.games.teeth_of_doom.core.ai.navigation.Neighbourhood.Position, T> {
	private static final long serialVersionUID = 2337988640820638243L;

	public Map<Position, T> getDiagonals() {
		Map<Position, T> result = new HashMap<Position, T>();

		transferElement(result, Position.BOT_LEFT);
		transferElement(result, Position.TOP_LEFT);
		transferElement(result, Position.TOP_RIGHT);
		transferElement(result, Position.BOT_RIGHT);

		return result;
	}

	public Map<Position, T> getStraights() {
		Map<Position, T> result = new HashMap<Position, T>();

		transferElement(result, Position.MID_LEFT);
		transferElement(result, Position.TOP_MID);
		transferElement(result, Position.MID_RIGHT);
		transferElement(result, Position.BOT_MID);

		return result;
	}

	protected void transferElement(Map<Position, T> target, Position key) {
		T elem = get(key);
		if (elem != null) {
			target.put(key, elem);
		}
	}

	@Override
	public T put(Position key, T value) {
		if (value != null) {
			return super.put(key, value);
		} else {
			return remove(key);
		}
	}

	public void iterate(BiConsumer<Duo<Integer, Integer>, T> func) {
		IntStream.range(-1, 2).forEach(y -> IntStream.range(-1, 2).forEach(x -> {
			if (x != 0 || y != 0) {
				func.accept(new Duo<>(x, y), get(Position.getByXY(x, y)));
			}
		}));
	}

	public enum Position {
		BOT_LEFT(-1, -1), MID_LEFT(-1, 0), TOP_LEFT(-1, 1), TOP_MID(0, 1), TOP_RIGHT(1, 1), MID_RIGHT(1, 0),
		BOT_RIGHT(1, -1), BOT_MID(0, -1);

		int x, y;

		Position(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public static Position getByXY(int x, int y) {
			return Arrays.stream(values()).filter(pos -> (pos.x == x && pos.y == y)).findFirst().get();
		}
	}
}
