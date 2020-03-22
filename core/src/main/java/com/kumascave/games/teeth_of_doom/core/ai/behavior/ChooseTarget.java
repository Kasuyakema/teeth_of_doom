package com.kumascave.games.teeth_of_doom.core.ai.behavior;

import java.util.Optional;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.Mob;
import com.kumascave.games.teeth_of_doom.core.physics.PhysicalEntity;

public class ChooseTarget<E extends Mob> extends LeafTask<E> {

	public ChooseTarget() {
		super();
		this.setGuard(new Guard<E>(() -> getObject().getTargetEntity() == null || closerTargetExists()));
	}

	@Override
	public Status execute() {

		PhysicalEntity target;

		Optional<PhysicalEntity> hostile = getObject().getProximity().getClosestHostilePhysicalEntity();

		if (hostile.isPresent()) {
			target = hostile.get();
		} else {
			target = GameContext.getMother();
		}
		getObject().setTargetEntity(target);
		getObject().resetPath();
		return Status.SUCCEEDED;
	}

	@Override
	protected Task<E> copyTo(Task<E> task) {
		return task;
	}

	private boolean closerTargetExists() {
		Optional<PhysicalEntity> hostile = getObject().getProximity().getClosestHostilePhysicalEntity();
		if (hostile.isPresent()) {
			return !hostile.get().equals(getObject().getTargetEntity());
		}
		return false;
	}

}
