package com.kumascave.games.teeth_of_doom.core.mechanics.damage;

import com.kumascave.games.teeth_of_doom.core.HasId;

public interface ContactResolving extends HasId {

	public default void resolveContact(ContactResolving contact) {
		// do nothing
	}

}
