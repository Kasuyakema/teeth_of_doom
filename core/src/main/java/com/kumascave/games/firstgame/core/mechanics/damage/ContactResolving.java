package com.kumascave.games.firstgame.core.mechanics.damage;

import com.kumascave.games.firstgame.core.HasId;

public interface ContactResolving extends HasId {

	public default void resolveContact(ContactResolving contact) {
		// do nothing
	}

}
