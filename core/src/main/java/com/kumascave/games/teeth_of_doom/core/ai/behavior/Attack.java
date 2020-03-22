package com.kumascave.games.teeth_of_doom.core.ai.behavior;

import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.Vector2;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.Mob;

public class Attack<E extends Mob> extends com.badlogic.gdx.ai.btree.LeafTask<E> {

	public Attack() {
		super();
		setGuard(new Guard<E>(() -> getObject().getTargetEntity() != null && inRange()));
		// setGuard(new Guard<E>(() -> !getObject().attackOnCooldown()));
	}

	@Override
	public Status execute() {
		getObject().setLookAtWaypount(false);
		getObject().setMovementDir(Vector2.Zero);
		if (!getObject().attackOnCooldown()) {
			getObject().attack();
		}
		return Status.SUCCEEDED;
	}

	@Override
	protected Task<E> copyTo(Task<E> task) {
		return task;
	}

	private boolean inRange() {
		float range = getObject().getAttackRange();
		float dist = getObject().distanceTo(getObject().getTargetEntity());
		return dist <= range;
	}

}
