package com.kumascave.games.teeth_of_doom.core.ai.behavior;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.Vector2;
import com.kumascave.games.teeth_of_doom.core.Constants;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.Mob;

public class FollowPath<E extends Mob> extends LeafTask<E> {

	public FollowPath() {
		super();
		setGuard(new Guard<E>(() -> getObject().getPath() != null && getObject().getPath().size() > 0));
	}

	@Override
	public Status execute() {
		getObject().setLookAtWaypount(true);
		if (getObject().getPose().getPos().cpy().sub(getObject().getPath().get(0))
				.len2() < Constants.NAVIGATION_ZERO_DISTANCE_THRESHOLD) {

			getObject().getPath().remove(0);

			if (getObject().getPath().size() == 0) {
				getObject().setLookAtWaypount(false);
				getObject().setMovementDir(Vector2.Zero);
			}
		}

		if (getObject().getPath().size() > 0) {
			float desiredSpeedRatio = 1f;
			if (getObject().getPath().size() == 1) {
				desiredSpeedRatio = Math.min(getObject().distanceToCenter(getObject().getPath().get(0))
						/ Constants.NAVIGATION_BREAK_DISTANCE, 1.0f);
			}
			getObject().setMovementDir(getObject().getPath().get(0).cpy().sub(getObject().getPose().getPos())
					.setLength(desiredSpeedRatio));
		}

		return Status.SUCCEEDED;
	}

	@Override
	protected Task<E> copyTo(Task<E> task) {
		return task;
	}

}
