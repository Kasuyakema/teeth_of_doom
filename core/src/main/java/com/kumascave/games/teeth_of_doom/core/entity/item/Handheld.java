package com.kumascave.games.teeth_of_doom.core.entity.item;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.entity.Transition;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.antropomorph.Hand;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.antropomorph.Hand.HandType;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.antropomorph.Human;
import com.kumascave.games.teeth_of_doom.core.physics.Friction;
import com.kumascave.games.teeth_of_doom.core.physics.Pose;
import com.kumascave.games.teeth_of_doom.core.physics.WorldUtil;
import com.kumascave.games.teeth_of_doom.core.world.CollisionFilters;

public abstract class Handheld extends Item implements EquipUsable {

	protected Human user;
	protected HandType position;

	protected WeldJoint fixedJoint;

	protected Handheld(Vector2 actorSize, Pose odom, Shape shape, BodyType bodyType, float density, Friction friction,
			float restitution) {
		super(actorSize, odom, shape, bodyType, density, friction, restitution);
		stateMachine = new HandheldStateMachine<Handheld>(this);
	}

	public void prepareThrow() {
		unFix();
		setCollisionFilter(CollisionFilters.ALL_CATEGORY, CollisionFilters.WEAPON_MASK,
				CollisionFilters.PROJECTILE_GROUP);
		this.setPositionFull(getThrowPose());
	}

	protected void stopThrow() {
		setCollisionFilter(CollisionFilters.GROUND_CATEGORY, CollisionFilters.SMALL_ITEM_MASK);
	}

	protected Pose getThrowPose() {
		return new Pose(
				user.getHeading().cpy().setLength(user.getLeadComponent().getWidth() / 2 + getSize().y / 2 + 0.1f)
						.add(user.getBody().getPosition()),
				user.getBody().getAngle());
	}

	protected abstract Vector2 getSize();

	@Override
	public void onEquip(Human user) {
		requireSwitchState(HandheldState.FIXED, user);
	}

	@Override
	public void onUnequip() {
		switchState(HandheldState.FREE);
	}

	protected void unequip() {
		unFix();
		user = null;
		this.position = null;
	}

	protected void equip(Human user) {
		this.user = user;
		position = Hand.handTypeFromEquipmentSlot(user.getEquipmentHolder().getSlot(this));
		fix();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (isInState(HandheldState.THROWN)) {
			switchState(ProjectileState.FREE);
		}
	}

	@Override
	public EquipmentSlotType getEquipmentSlotType() {
		return EquipmentSlotType.HAND;
	}

	protected void fix() {
		setCollisionFilter(CollisionFilters.GROUND_CATEGORY, CollisionFilters.SMALL_ITEM_MASK);
		this.setPositionFull(getFixedPose());
		WeldJointDef weld = new WeldJointDef();
		weld.initialize(user.getBody(), getBody(), user.getBody().getPosition());
		weld.collideConnected = false;
		weld.frequencyHz = 0;
		weld.dampingRatio = 1;
		GameContext.inst();
		fixedJoint = (WeldJoint) WorldUtil.createJoint(weld);
	}

	protected void unFix() {
		GameContext.inst();
		WorldUtil.destroyJoint(fixedJoint, getBody());
	}

	protected abstract Pose getFixedPose();

	public class HandheldStateMachine<T extends Handheld> extends ItemStateMachine<T> {

		protected HandheldStateMachine(T subject) {
			super(subject);

			List<Transition> transitionz = new ArrayList<>();

			transitionz.add(new Transition(HandheldState.FREE, HandheldState.FIXED,
					args -> getSubject().equip((Human) args[0])));
			transitionz.add(new Transition(HandheldState.FIXED, HandheldState.FREE, args -> getSubject().unequip()));

			transitionz.add(
					new Transition(HandheldState.FIXED, HandheldState.THROWN, args -> getSubject().prepareThrow()));

			transitionz.add(new Transition(HandheldState.THROWN, HandheldState.FREE, () -> !getBody().isAwake(),
					args -> getSubject().stopThrow()));

			transitionz.add(new Transition(HandheldState.THROWN, HandheldState.FIXED, args -> {
				getSubject().stopThrow();
				getSubject().equip((Human) args[0]);
			}));

			this.addTransitions(transitionz);
		}
	}
}
