package com.kumascave.games.teeth_of_doom.core.mechanics.spawn;

import java.util.function.Supplier;

import com.kumascave.games.teeth_of_doom.core.RepeatingAction;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.Mob;

public class Spawnpoint<M extends Mob> extends RepeatingAction {
	public Spawnpoint(float cooldown, Supplier<M> supplier) {
		super(cooldown, () -> supplier.get().addToWorld());
	}
}
