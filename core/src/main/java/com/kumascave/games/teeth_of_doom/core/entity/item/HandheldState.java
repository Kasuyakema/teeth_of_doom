package com.kumascave.games.teeth_of_doom.core.entity.item;

public abstract class HandheldState extends ItemState {

	// Passively hold in hand
	public static final String FIXED = "FIXED";

	// melee attack animation
	public static final String SWING = "SWING";

	// ranged attack animation
	public static final String FIRE = "FIRE";

	// item is attached to multiple things (rope, harpoon)
	public static final String ATTACHED = "ATTACHED";
}
