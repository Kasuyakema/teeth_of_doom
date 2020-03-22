package com.kumascave.games.teeth_of_doom.core.ai.behavior;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

public class RunLeaf<E> extends LeafTask<E> {

	protected Runnable runnable;

	RunLeaf(Runnable aRunnable) {
		this.runnable = aRunnable;
	}

	@Override
	public Status execute() {
		runnable.run();
		return Status.SUCCEEDED;
	}

	@Override
	protected Task<E> copyTo(Task<E> task) {
		((RunLeaf<E>) task).runnable = runnable;
		return task;
	}

}
