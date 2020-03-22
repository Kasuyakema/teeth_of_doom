package com.kumascave.games.teeth_of_doom.core.ai.behavior;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.Mob;

public class WaitForPath<E extends Mob> extends LeafTask<E> {

	public WaitForPath() {
		super();
		setGuard(new Guard<E>(() -> getObject().getFuture() != null && getObject().getPath() == null));
	}

	@Override
	public Status execute() {
		return Status.SUCCEEDED;
	}

	@Override
	protected Task<E> copyTo(Task<E> task) {
		return task;
	}

}
