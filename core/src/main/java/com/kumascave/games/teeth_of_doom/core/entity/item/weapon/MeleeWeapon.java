package com.kumascave.games.teeth_of_doom.core.entity.item.weapon;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape;
import com.kumascave.games.teeth_of_doom.core.DelayedAction;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.entity.Transition;
import com.kumascave.games.teeth_of_doom.core.entity.item.Handheld;
import com.kumascave.games.teeth_of_doom.core.entity.item.HandheldState;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.antropomorph.Hand.HandType;
import com.kumascave.games.teeth_of_doom.core.physics.Friction;
import com.kumascave.games.teeth_of_doom.core.physics.Pose;

public abstract class MeleeWeapon extends Handheld {

	protected DelayedAction stopAction;

	protected MeleeWeapon(Vector2 actorSize, Pose odom, Shape shape, BodyType bodyType, float density,
			Friction friction, float restitution) {
		super(actorSize, odom, shape, bodyType, density, friction, restitution);
		stateMachine = new MeleeWeaponStateMachine<MeleeWeapon>(this);
	}

	@Override
	public void use() {
		stateMachine.transition(HandheldState.SWING);
	}

	@Override
	protected Pose getFixedPose() {
		if (position == HandType.LEFT) {
			return new Pose(user.getHeading().cpy().rotateRad((float) Math.PI / 2)
					.setLength(user.getLeadComponent().getWidth() / 2 + getSize().y / 2)
					.add(user.getBody().getPosition()).add(user.getHeading().cpy().setLength(getSize().x * 0.4f)),
					user.getBody().getAngle());
		} else {
			return new Pose(user.getHeading().cpy().rotateRad((float) -Math.PI / 2)
					.setLength(user.getLeadComponent().getWidth() / 2 + getSize().y / 2)
					.add(user.getBody().getPosition()).add(user.getHeading().cpy().setLength(getSize().x * 0.4f)),
					user.getBody().getAngle());
		}
	}

	protected void setupStopAction() {
		stopAction = new DelayedAction(0.3f, () -> requireSwitchState(HandheldState.FIXED));
		GameContext.inst();
		GameContext.getGameStage().addAction(stopAction);
	}

	protected abstract void stopSwing();

	protected abstract void startSwing();

	protected abstract Pose getSwingPose();

	class MeleeWeaponStateMachine<T extends MeleeWeapon> extends HandheldStateMachine<T> {

		MeleeWeaponStateMachine(T subject) {
			super(subject);
			List<Transition> transitionz = new ArrayList<>();

			transitionz.add(new Transition(HandheldState.FIXED, HandheldState.SWING,
					() -> checkPosition(getSwingPose().getPos()), args -> getSubject().startSwing()));
			transitionz.add(new Transition(HandheldState.SWING, HandheldState.FIXED,
					() -> checkPosition(getFixedPose().getPos()), args -> getSubject().stopSwing()));

			this.addTransitions(transitionz);
		}

	}
}
