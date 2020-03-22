package com.kumascave.games.teeth_of_doom.core.ai.behavior;

import java.util.concurrent.CompletableFuture;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.kumascave.games.teeth_of_doom.core.ai.navigation.GlobalPlanner;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.Mob;

public class FindPath<E extends Mob> extends LeafTask<E> {

	public FindPath() {
		super();
		this.setGuard(new Guard<E>(() -> getObject().getPath() == null && getObject().getFuture() == null));
	}

	@Override
	public Status execute() {
		// request path
		getObject().setFuture(CompletableFuture.supplyAsync(() -> {
			return GlobalPlanner.findPath(getObject().getPose().getPos(), getObject().getTargetPosition(),
					getObject().getRadius());
		}).thenAccept(l -> getObject().setPath(l)).thenRun(() -> getObject().setFuture(null)));

		return Status.SUCCEEDED;
	}

	@Override
	protected Task<E> copyTo(Task<E> task) {
		return task;
	}

}
