// package com.kumascave.games.firstgame.atest;
//
// import com.kumascave.games.firstgame.core.world.Duo;
// import com.kumascave.games.firstgame.core.world.Tile;
//
// public class TileMap {
// private DynamicMartix<Tile> tiles = new DynamicMartix<>(pos -> new
// Tile(pos.x, pos.y));
// private boolean[][] collisionMap;
// boolean collisionMapValid = false;
//
// public void put(int x, int y, Tile tile) {
// tiles.put(x, y, tile);
// if (collisionMapValid && tile.isColliding()) {
// collisionMapValid = false;
// }
// }
//
// public Tile get(int x, int y) {
// return tiles.get(x, y);
// }
//
// public boolean[][] getcollisionMap() {
// if (collisionMapValid == false) {
// buildCollisionMap();
// }
// return collisionMap;
// }
//
// private void buildCollisionMap() {
// Duo<Integer, Integer> size = tiles.size();
// collisionMap = new boolean[Math.max(size.a, 1)][Math.max(size.b, 1)];
// tiles.forEach(x -> mapCollision(x));
// collisionMapValid = true;
// }
//
// private void mapCollision(Tile x) {
// GPos pos = x.getGridPosition();
// collisionMap[pos.x][pos.y] = x.isColliding();
// }
//
// }
