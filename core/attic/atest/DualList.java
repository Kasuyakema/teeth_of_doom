package com.kumascave.games.firstgame.atest;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.Setter;

public class DualList<T> {

	@Setter
	Function<Integer, T> filler;
	@Getter
	ArrayList<T> pos = new ArrayList<>();
	@Getter
	ArrayList<T> neg = new ArrayList<>();

	public DualList(Function<Integer, T> aFiller) {
		super();
		this.filler = aFiller;
		neg.add(null);
	}

	// public List<T> get(int fromIndex, int toIndex) {
	// if (index >= 0) {
	// return pos.get(index);
	// }
	// return neg.get(-index);
	// }

	public Stream<T> stream() {
		return Stream.generate(new DualStreamer(this)).limit(size());
	}

	public void forEach(Consumer<T> action) {
		if (neg.size() > 1) {
			neg.subList(1, neg.size() - 1).forEach(action);
		}
		pos.forEach(action);
	}

	public int size() {
		return pos.size() + neg.size() - 1;
	}

	public T get(int index) {
		if (outOfBounds(index) != 0) {
			fillTo(index);
		}
		if (index >= 0) {
			return pos.get(index);
		}
		return neg.get(-index);
	}

	private void fillTo(int index) {
		if (index >= 0) {
			int curr = pos.size();
			do {
				pos.add(filler.apply(curr));
				curr++;
			} while (curr <= index);
			return;
		}
		int curr = -neg.size();
		do {
			neg.add(filler.apply(curr));
			curr--;
		} while (curr > index);
		return;
	}

	private int outOfBounds(int index) {
		if (index >= 0) {
			int diff = index - pos.size() + 1;
			return Math.max(0, diff);
		}
		int diff = index + pos.size() - 1;
		return Math.min(0, diff);

	}

	public void put(int index, T element) {
		if (outOfBounds(index) != 0) {
			fillTo(index);
		}
		if (index >= 0) {
			pos.set(index, element);
			return;
		}
		neg.set(-index, element);
		return;
	}

	class DualStreamer implements Supplier<T> {
		DualList<T> list;
		int index;

		public DualStreamer(DualList<T> list) {
			super();
			this.list = list;
			index = Math.min(-list.getNeg().size() + 1, 0);
		}

		@Override
		public T get() {
			int i = index;
			index++;
			return list.get(i);
		}

	}
}
