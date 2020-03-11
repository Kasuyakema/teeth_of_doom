package com.kumascave.games.teeth_of_doom.core.mechanics.damage;

import com.kumascave.games.teeth_of_doom.util.jgoodies.VHolder;

public interface HasHitpoints extends DmgResolving {

	public HpHolder getHpHolder();

	public VHolder<Integer> getHpMaxHolder();

	public VHolder<Boolean> getAliveHolder();

	default public boolean isAlive() {
		return getAliveHolder().getValue();
	}
}
