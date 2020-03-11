package com.kumascave.games.teeth_of_doom.core.entity.item;

public abstract class ItemState {

	// item is in a free state. Probably on the ground
	public static final String FREE = "FREE";
	// item is in an inventory
	public static final String INVENTORY = "INVENTORY";
	// item is stuck to something e.g. an arrow. One Joint
	public static final String STICKING = "STICKING";
	// item was thrown
	public static final String THROWN = "THROWN";

}
