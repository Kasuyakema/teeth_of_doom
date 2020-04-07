package com.kumascave.games.teeth_of_doom.core.mechanics;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Disposable;
import com.kumascave.games.teeth_of_doom.core.DelayedAction;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.Mob;
import com.kumascave.games.teeth_of_doom.core.mechanics.spawn.Spawnpoint;
import com.kumascave.games.teeth_of_doom.util.DynamicVariables;
import com.kumascave.games.teeth_of_doom.util.jgoodies.VHolder;

import lombok.Getter;

public class HuntDirector implements Disposable {
	private static VHolder<Integer> spawnCounter;
	private static VHolder<Integer> livingCounter;
	@Getter
	private static int huntCounter;
	@Getter
	private static int groupSize;

	private static List<Spawnpoint<Mob>> spawnpoints;

	public HuntDirector() {
		super();
		spawnpoints = new ArrayList<>();
		spawnCounter = new VHolder<>(0);
		livingCounter = new VHolder<>(0);
		huntCounter = 1;
		groupSize = 2;
		GameContext.getHuntOngoing()
				.addPropertyChangeListener(evt -> spawnpoints.forEach(x -> x.setAwake((Boolean) evt.getNewValue())));
		spawnCounter.addPropertyChangeListener(evt -> endSpawningIfDone());
		livingCounter.addPropertyChangeListener(evt -> endHuntIfDone());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void registerSpawnpoint(Spawnpoint spawnpoint) {
		spawnpoints.add(spawnpoint);
	}

	public static void addSpawned(List<Mob> mobs) {
		spawnCounter.setValue(spawnCounter.getValue() + mobs.size());
		livingCounter.setValue(livingCounter.getValue() + mobs.size());
		mobs.forEach(x -> x.getAliveHolder().addPropertyChangeListener(evt -> {
			livingCounter.setValue(livingCounter.getValue() - 1);
		}));
	}

	protected void endSpawningIfDone() {
		if (spawnCounter.getValue() > DynamicVariables.huntBaseAmount * huntCounter) {
			spawnpoints.forEach(x -> x.setAwake(false));
		}
	}

	protected void endHuntIfDone() {
		if (livingCounter.getValue() <= 0) {
			GameContext.getHuntOngoing().setValue(false);
			spawnCounter.setValue(0);
			livingCounter.setValue(0);
			huntCounter++;
			groupSize = (int) (huntCounter * DynamicVariables.groupSizeMultiplier);
			GameContext.getGameStage().addAction(
					new DelayedAction(DynamicVariables.calmTime, () -> GameContext.getHuntOngoing().setValue(true)));
		}
	}

	@Override
	public void dispose() {
		spawnCounter = null;
		livingCounter = null;
		spawnpoints.clear();
		spawnpoints = null;
	}

}
