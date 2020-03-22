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
	@Getter
	private static int huntCounter;
	@Getter
	private static int groupSize;

	private static List<Spawnpoint<Mob>> spawnpoints;

	public HuntDirector() {
		super();
		spawnpoints = new ArrayList<>();
		spawnCounter = new VHolder<>(0);
		huntCounter = 1;
		groupSize = 2;
		GameContext.getHuntOngoing()
				.addPropertyChangeListener(evt -> spawnpoints.forEach(x -> x.setAwake((Boolean) evt.getNewValue())));
		spawnCounter.addPropertyChangeListener(evt -> endHuntIfDone());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void registerSpawnpoint(Spawnpoint spawnpoint) {
		spawnpoints.add(spawnpoint);
	}

	public static void addSpawned(int i) {
		spawnCounter.setValue(spawnCounter.getValue() + i);
	}

	protected void endHuntIfDone() {
		if (spawnCounter.getValue() > DynamicVariables.huntBaseAmount * huntCounter) {
			GameContext.getHuntOngoing().setValue(false);
			spawnCounter.setValue(0);
			huntCounter++;
			groupSize = (int) (huntCounter * DynamicVariables.groupSizeMultiplier);
			GameContext.getPlayer().getHpHolder().add(100);
			GameContext.getGameStage().addAction(
					new DelayedAction(DynamicVariables.calmTime, () -> GameContext.getHuntOngoing().setValue(true)));
		}
	}

	@Override
	public void dispose() {
		spawnCounter = null;
		spawnpoints.clear();
		spawnpoints = null;
	}

}
