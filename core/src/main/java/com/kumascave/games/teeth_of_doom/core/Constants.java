package com.kumascave.games.teeth_of_doom.core;

public class Constants {

	public static final short TYPE_MOB = 1;
	public static final short TYPE_WALL = 2;
	public static final short TYPE_ITEM = 4;
	// public static final short TYPE_PLAYER = 8;

	public static final float TILEGRID_SIZE = 0.5f;
	public static final int CHUNK_SIZE = 100;

	public static final int PLANNINGGRID_RESOLUTION = 3;
	public static final float PLANNINGGRID_SIZE = TILEGRID_SIZE / PLANNINGGRID_RESOLUTION;

	public static final float DISTANCEMAP_IRRELEVANCE = .8f;

	public static final float CONTACT_SLEEP = 0.25f;
	public static final float DISPOSE_TIME = 2000;

	public static final float PATHFINDING_BUFFER = .1f;
	public static final float PATHFINDING_PATH_CHECK_STEP = .1f;
	// public static final float ZERO_SPEED_THRESHOLD = .01f;
	public static final float NAVIGATION_ZERO_DISTANCE_THRESHOLD = .001f;
	public static final float NAVIGATION_BREAK_DISTANCE = 0.2f;
	// if a Target moves this far we need a new Path
	public static final float NAVIGATION_MOVEMENT_THRESHOLD = 0.1f;

	// if a Target moves this far we need a new Path
	public static final int TARGET_FPS = 60;

}
