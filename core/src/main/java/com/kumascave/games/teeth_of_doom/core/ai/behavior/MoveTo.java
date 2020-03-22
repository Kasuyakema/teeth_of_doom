package com.kumascave.games.teeth_of_doom.core.ai.behavior;

import com.badlogic.gdx.ai.btree.branch.Selector;
import com.kumascave.games.teeth_of_doom.core.Constants;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.Mob;

public class MoveTo<E extends Mob> extends Selector<E> {

	@SuppressWarnings("unchecked")
	public MoveTo() {
		super(new FindPath<E>(), new WaitForPath<E>(), new FollowPath<E>());
		setGuard(new Guard<E>(() -> getObject().getTargetPosition() != null && !targetReached()));
	}

	private boolean targetReached() {
		return getObject().getTargetPosition().cpy().sub(getObject().getPose().getPos())
				.len2() < Constants.NAVIGATION_ZERO_DISTANCE_THRESHOLD;
	}

}
