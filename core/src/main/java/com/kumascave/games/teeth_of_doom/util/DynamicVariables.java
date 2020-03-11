package com.kumascave.games.teeth_of_doom.util;

import com.kumascave.games.teeth_of_doom.util.jgoodies.VHolder;

public abstract class DynamicVariables {

	public static VHolder<Boolean> debugWorld = new VHolder<>(true);
	public static VHolder<Boolean> debugStage = new VHolder<>(false);

	public static VHolder<Boolean> printMouse = new VHolder<>(false);

	public static VHolder<Boolean> collisionOverlay = new VHolder<>(false);
	public static VHolder<Boolean> gridOverlay = new VHolder<>(false);
	public static VHolder<Boolean> navigationOverlay = new VHolder<>(false);
}