package com.kumascave.games.teeth_of_doom.core.mechanics.spawn;

import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.RepeatingAction;
import com.kumascave.games.teeth_of_doom.core.ai.navigation.GlobalPlanner;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.Mob;
import com.kumascave.games.teeth_of_doom.core.mechanics.HuntDirector;
import com.kumascave.games.teeth_of_doom.util.Util;

public class Spawnpoint<M extends Mob> extends RepeatingAction {

	public Spawnpoint(float cooldown, Supplier<M> supplier, float radius) {
		super(cooldown, () -> {
			int groupsize = GameContext.RANDOM.nextInt(HuntDirector.getGroupSize()) + 1;
			Util.callXTimes(groupsize, () -> spawn(supplier.get(), radius));
			HuntDirector.addSpawned(groupsize);
		});
		setAwake(GameContext.getHuntOngoing().getValue());
		HuntDirector.registerSpawnpoint(this);
	}

	private static void spawn(Mob m, float radius) {
		int tries = 10;
		Vector2 start = new Vector2(m.getActorGroup().getX(), m.getActorGroup().getY());
		for (int i = 0; i < tries; i++) {
			float dist = GameContext.RANDOM.nextFloat() * radius;
			Vector2 offset = Vector2.X;
			offset.rotate(GameContext.RANDOM.nextFloat() * 360f).setLength(dist);
			Vector2 candidate = start.cpy().add(offset);
			if (GlobalPlanner.checkNode(candidate, m.getRadius())) {
				m.setPositionFull(candidate);
				m.addToWorld();
				return;
			}
		}
		Gdx.app.error(Spawnpoint.class.getSimpleName(),
				"unable to Place " + m.getClass().getSimpleName() + " at " + start + "+" + radius);
	}

}
