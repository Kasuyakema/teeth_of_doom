package com.kumascave.games.teeth_of_doom.core.ai.behavior;

import java.util.function.Supplier;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

public class Guard<E> extends LeafTask<E> {

	protected Supplier<Boolean> check;

	Guard(Supplier<Boolean> check) {
		this.check = check;
	}

	@Override
	public Status execute() {
		if (check.get()) {
			return Status.SUCCEEDED;
		}
		return Status.FAILED;
	}

	@Override
	protected Task<E> copyTo(Task<E> task) {
		((Guard<E>) task).check = check;
		return task;
	}

}
