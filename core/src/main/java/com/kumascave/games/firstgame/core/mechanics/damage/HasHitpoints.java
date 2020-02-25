package com.kumascave.games.firstgame.core.mechanics.damage;

import com.kumascave.games.firstgame.util.jgoodies.VHolder;

public interface HasHitpoints extends DmgResolving {

	public HpHolder getHpHolder();

	public VHolder<Integer> getHpMaxHolder();

	public VHolder<Boolean> getAliveHolder();

	default public boolean isAlive() {
		return getAliveHolder().getValue();
	}
}
