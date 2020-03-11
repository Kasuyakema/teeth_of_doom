package com.kumascave.games.firstgame.forlater;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kumascave.games.firstgame.AppContext;
import com.kumascave.games.firstgame.core.DelayedAction;
import com.kumascave.games.firstgame.core.GameContext;
import com.kumascave.games.firstgame.core.ManagedJointReference;
import com.kumascave.games.firstgame.core.entity.Transition;
import com.kumascave.games.firstgame.core.entity.item.Handheld;
import com.kumascave.games.firstgame.core.entity.item.HandheldState;
import com.kumascave.games.firstgame.core.entity.item.Rope;
import com.kumascave.games.firstgame.core.entity.item.RopeElement;
import com.kumascave.games.firstgame.core.entity.mobs.antropomorph.Hand.HandType;
import com.kumascave.games.firstgame.core.mechanics.damage.Damage;
import com.kumascave.games.firstgame.core.mechanics.damage.DmgResolving;
import com.kumascave.games.firstgame.core.physics.Friction;
import com.kumascave.games.firstgame.core.physics.PhysicalEntity;
import com.kumascave.games.firstgame.core.physics.Pose;
import com.kumascave.games.firstgame.core.physics.shapes.Rectangle;
import com.kumascave.games.firstgame.core.world.CollisionFilters;

public class Harpoon extends Handheld {

	protected static final int STATE_THROW = 40;
	protected static final int STATE_THROWN_TUMBLE = 49;
	protected static final int STATE_THROWN_STICKING = 41;

	private static Friction friction = new Friction(0.5f, 0.5f);

	private static Vector2 size = new Vector2(0.05f, 0.5f);
	private static Vector2 actorSize = new Vector2(2.5f * size.x, size.y);

	private static float density = Rectangle.densityFromWeight(1.0f, size);
	private static float restitution = 0.7f;

	private static final int baseDmg = 10;
	private int dmg = 0;

	protected ManagedJointReference stickJoint = new ManagedJointReference(null);

	Runnable stickRunnable;
	private Rope rope;

	public Harpoon(Pose odom) {
		super(actorSize, odom, new Rectangle(size.x, size.y), BodyType.DynamicBody, density, friction, restitution);
		stickJoint.addDestructionListener(evt -> onStickJointDestruction());
		setDrawable(new TextureRegionDrawable(
				new TextureRegion(AppContext.inst().getAssetManager().get("harpoon.png", Texture.class))));
		stateMachine = new HarpoonStateMachine<Harpoon>(this);
	}

	@Override
	public void use() {
		boolean fired = switchState(HandheldState.FIRE);
		if (!fired) {
			switchState(HandheldState.FIXED);
		}
	}

	@Override
	public void prepareThrow() {
		super.prepareThrow();
		dmg = baseDmg;
	}

	protected void pull() {
		RopeElement last = rope.getElements().get(rope.getElements().size() - 1);

		last.getBody().applyLinearImpulse(
				getBody().getPosition().cpy().sub(user.getBody().getPosition()).setLength(user.getStrength()),
				last.getBody().getPosition(), true);

		GameContext.inst().getGameStage().addAction(new DelayedAction(0.5f, () -> retrieve()));
	}

	private void retrieve() {
		stickJoint.destroyJoint();
		if (rope != null) {
			rope.destroyRope();
			rope = null;
		}
		fix();
	}

	private void onStickJointDestruction() {
		boolean free = switchState(HandheldState.FREE);
		if (!free) {
			requireSwitchState(HandheldState.FIXED);
		}
	}

	@Override
	protected Pose getFixedPose() {
		if (position == HandType.LEFT) {
			return new Pose(user.getHeading().cpy().rotateRad((float) Math.PI / 2)
					.setLength(user.getWidth() / 2 + size.y / 2).add(user.getBody().getPosition()),
					user.getBody().getAngle());
		} else {
			return new Pose(user.getHeading().cpy().rotateRad((float) -Math.PI / 2)
					.setLength(user.getWidth() / 2 + size.y / 2).add(user.getBody().getPosition()),
					user.getBody().getAngle() + (float) Math.PI);
		}

	}

	protected void startFire() {
		unFix();
		Pose pose = getThrowPose();
		setPositionFull(pose);
		rope = new Rope(this.getBody(), user.getBody(), new Vector2(0, -size.y / 2f), Vector2.Zero);
		setCollisionFilter(CollisionFilters.ALL_CATEGORY, CollisionFilters.WEAPON_MASK,
				CollisionFilters.PROJECTILE_GROUP);
		dmg = baseDmg;
		getBody().applyLinearImpulse(user.getHeading().cpy().setLength(user.getStrength()), getBody().getPosition(),
				true);

		Runnable stopFireRunnable = () -> {
			if (isInState(HandheldState.FIRE)) {
				switchState(HandheldState.FIXED);
			}
		};
		GameContext.inst().getGameStage().addAction(new DelayedAction(0.5f, stopFireRunnable));
	}

	@Override
	public Damage calculateDmg(DmgResolving target, WorldManifold worldManifold) {
		if (isInStates(HandheldState.THROWN, HandheldState.FIRE)) {
			if (target.equals(user)) {
				return new Damage(this, 0, worldManifold);
			}
			if (target instanceof PhysicalEntity && dmg > 0) {
				stickRunnable = () -> stick((PhysicalEntity) target, worldManifold);
			}
			int oldDmg = dmg;
			dmg = 0;
			return new Damage(this, oldDmg, worldManifold);

		} else {
			return super.calculateDmg(target, worldManifold);
		}

	}

	private void stick(PhysicalEntity target, WorldManifold worldManifold) {
		setCollisionFilter(CollisionFilters.BASE_CATEGORY, CollisionFilters.COLLISION_DEACTIVATED_MASK);
		Vector2 contactPoint = worldManifold.getPoints()[0];
		Pose pose = new Pose(contactPoint.cpy().sub(getHeading().cpy().setLength(size.y / 2f)),
				getHeading().angleRad() - (float) Math.PI / 2.0f);
		setPositionFull(pose);
		WeldJointDef weld = new WeldJointDef();
		weld.initialize(target.getBody(), body, target.getBody().getPosition());
		weld.collideConnected = false;
		weld.frequencyHz = 0;
		weld.dampingRatio = 1;
		stickJoint.setJoint(GameContext.inst().getWorld().createJoint(weld));
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (isInStates(HandheldState.THROWN, HandheldState.FIRE)) {
			switchState(HandheldState.STICKING);
		}
	}

	@Override
	protected Vector2 getSize() {
		return size;
	}

	class HarpoonStateMachine<T extends Harpoon> extends HandheldStateMachine<T> {

		HarpoonStateMachine(T subject) {
			super(subject);

			List<Transition> transitionz = new ArrayList<>();

			transitionz.add(new Transition(HandheldState.FIXED, HandheldState.FIRE, args -> getSubject().startFire()));

			transitionz.add(
					new Transition(HandheldState.FIRE, HandheldState.STICKING, () -> stickRunnable != null, args -> {
						stickRunnable.run();
						stickRunnable = null;
					}));

			transitionz.add(
					new Transition(HandheldState.THROWN, HandheldState.STICKING, () -> stickRunnable != null, args -> {
						stickRunnable.run();
						stickRunnable = null;
					}));

			transitionz.add(new Transition(HandheldState.FIRE, HandheldState.FIXED, () -> stickRunnable == null,
					args -> retrieve()));

			transitionz.add(
					new Transition(HandheldState.STICKING, HandheldState.FIXED, () -> user == null, args -> pull()));

			transitionz.add(new Transition(HandheldState.STICKING, HandheldState.FREE, () -> user == null,
					args -> getSubject().unequip()));

			this.addTransitions(transitionz);
		}

	}
}
