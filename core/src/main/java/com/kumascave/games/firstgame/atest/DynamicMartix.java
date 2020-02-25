// package com.kumascave.games.firstgame.atest;
//
// import java.util.function.Consumer;
// import java.util.function.Function;
//
// public class DynamicMartix<T> {
// DualList<DualList<T>> mat;
//
// Function<GPos, T> filler;
//
// public DynamicMartix(Function<GPos, T> filler) {
// super();
// this.filler = filler;
// createMat();
// }
//
// public void forEach(Consumer<T> action) {
// mat.forEach(x -> x.forEach(action));
// }
//
// private void createMat() {
// mat = new DualList<>(x -> new DualList<>(y -> filler.apply(new GPos(x, y))));
// }
//
// public Duo<Integer, Integer> size() {
// int ySize = mat.stream().map(x -> x.size()).max((x, y) -> Integer.compare(x,
// y)).orElseGet(() -> 0);
// return new Duo<Integer, Integer>(mat.size(), ySize);
// }
//
// public void put(int x, int y, T element) {
// mat.get(x).put(y, element);
// }
//
// public T get(int x, int y) {
// return mat.get(x).get(y);
// }
// //
// // public T get(GPos topLeft, GPos bottomRight) {
// // return mat.get(x).get(y);
// // }
// }
