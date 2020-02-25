package com.kumascave.games.firstgame.core.mechanics.spawn;

import java.util.function.Supplier;

import com.kumascave.games.firstgame.core.entity.mobs.Mob;
import com.kumascave.games.firstgame.core.mechanics.RepeatingAction;

public class Spawnpoint<M extends Mob> extends RepeatingAction {
	public Spawnpoint(float cooldown, Supplier<M> supplier) {
		super(cooldown, () -> supplier.get().addToWorld());
	}
}
