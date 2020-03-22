package com.kumascave.games.teeth_of_doom.util;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public abstract class Util {

	public static void callXTimes(int max, Runnable func) {
		IntStream.range(0, max).forEach(i -> func.run());
	}

	public static void iterate(int max, Consumer<Integer> func) {
		IntStream.range(0, max).forEach(i -> func.accept(i));
	}

	public static void iterate(int maxX, int maxY, BiConsumer<Integer, Integer> func) {
		IntStream.range(0, maxY).forEach(y -> IntStream.range(0, maxX).forEach(x -> func.accept(x, y)));
	}

	public static void iterate(int max, BiConsumer<Integer, Integer> func) {
		iterate(max, max, func);
	}
}
