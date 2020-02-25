package com.kumascave.games.firstgame.util;

import java.util.stream.IntStream;

public abstract class Util {
	public static void callXTimes(int x, Runnable func) {
		IntStream.range(0, x).forEach(i -> func.run());
	}
}
