package com.kumascave.games.teeth_of_doom.core.ai.behavior;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.Vector2;
import com.kumascave.games.teeth_of_doom.core.Constants;
import com.kumascave.games.teeth_of_doom.core.ai.navigation.GlobalPlanner;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.Mob;

public class ChooseAttackPosition<E extends Mob> extends LeafTask<E> {

	float prefferedDist;

	public ChooseAttackPosition(float prefferedDist) {
		super();
		this.setGuard(new Guard<E>(() -> getObject().getAttackVector() == null || attackPositionInvalid()));
		this.prefferedDist = prefferedDist;
	}

	@Override
	public Status execute() {

		findPrefferedPos();
		getObject().resetPath();

		return Status.SUCCEEDED;
	}

	private void findPrefferedPos() {
		Vector2 target = getObject().getTargetEntity().getPose().getPos();
		Vector2 position = getObject().getPose().getPos();

		float distance = prefferedDist + getObject().getTargetEntity().getRadius();

		Vector2 attackVector = target.cpy().sub(position).setLength(distance);

		Vector2 candidate = target.cpy().sub(attackVector);

		if (GlobalPlanner.checkNode(candidate, getObject().getRadius())) {
			getObject().setTargetPosition(candidate);
			getObject().setAttackVector(attackVector);
			return;
		}

		throw new RuntimeException("Check for alternative positions not implemented");
	}

	@Override
	protected Task<E> copyTo(Task<E> task) {
		return task;
	}

	private boolean attackPositionInvalid() {
		Vector2 target = getObject().getTargetEntity().getPose().getPos();

		Vector2 attackPos = target.cpy().sub(getObject().getAttackVector());

		return Constants.NAVIGATION_MOVEMENT_THRESHOLD < getObject().getTargetPosition().cpy().sub(attackPos).len2();
	}

}
