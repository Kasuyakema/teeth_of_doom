package com.kumascave.games.firstgame.core.entity.mobs.antropomorph;

public abstract class HandState {
	// Hand is empty
	public static final String EMPTY = "EMPTY";
	// Hand has grabbed a non equippable Object
	public static final String GRABBED = "GRABBED";
	// Hand has equipped an Object
	public static final String EQUIPPED = "EQUIPPED";
	// Hand is Throwing an object (should directly transition to empty)
	public static final String THROW = "THROW";
}
