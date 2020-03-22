package com.kumascave.games.teeth_of_doom.core.entity.item.weapon;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kumascave.games.teeth_of_doom.AppContext;
import com.kumascave.games.teeth_of_doom.core.DelayedAction;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.ManagedJointReference;
import com.kumascave.games.teeth_of_doom.core.entity.Transition;
import com.kumascave.games.teeth_of_doom.core.entity.item.HandheldState;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.antropomorph.Hand.HandType;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.Damage;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.DmgResolving;
import com.kumascave.games.teeth_of_doom.core.physics.Friction;
import com.kumascave.games.teeth_of_doom.core.physics.PhysicalEntity;
import com.kumascave.games.teeth_of_doom.core.physics.Pose;
import com.kumascave.games.teeth_of_doom.core.physics.WorldUtil;
import com.kumascave.games.teeth_of_doom.core.physics.shapes.Rectangle;
import com.kumascave.games.teeth_of_doom.core.world.CollisionFilters;

public class Spear extends MeleeWeapon {

	private static Friction friction = new Friction(0.5f, 0.5f);

	private static Vector2 size = new Vector2(0.75f, 0.075f);
	private static Vector2 actorSize = new Vector2(size.x, 2.5f * size.y);

	private static float density = Rectangle.densityFromWeight(1.0f, size);
	private static float restitution = 0.7f;

	private static final int baseDmg = 10;
	private int dmg = 0;

	protected ManagedJointReference stickJoint = new ManagedJointReference(null);

	Runnable stickRunnable;

	private DelayedAction stopAction;

	private PrismaticJoint joint;

	public Spear(Pose odom) {
		super(actorSize, odom, new Rectangle(size.x, size.y), BodyType.DynamicBody, density, friction, restitution);
		stickJoint.addDestructionListener(evt -> onStickJointDestruction());
		getLeadComponent().setDrawable(new TextureRegionDrawable(
				new TextureRegion(AppContext.inst().getAssetManager().get("harpoon.png", Texture.class))));
		stateMachine = new SpearStateMachine<Spear>(this);
	}

	@Override
	public void prepareThrow() {
		super.prepareThrow();
		dmg = baseDmg;
	}

	private void onStickJointDestruction() {
		switchState(HandheldState.FREE);
	}

	@Override
	protected Pose getFixedPose() {
		if (position == HandType.LEFT) {
			return new Pose(user.getHeading().cpy().rotateRad((float) Math.PI / 2)
					.setLength(user.getLeadComponent().getWidth() / 2 + size.y / 2).add(user.getBody().getPosition()),
					user.getBody().getAngle());
		} else {
			return new Pose(user.getHeading().cpy().rotateRad((float) -Math.PI / 2)
					.setLength(user.getLeadComponent().getWidth() / 2 + size.y / 2).add(user.getBody().getPosition()),
					user.getBody().getAngle());
		}
	}

	@Override
	protected Pose getSwingPose() {
		return new Pose(user.getHeading().cpy().setLength(user.getLeadComponent().getWidth() / 2)
				.add(user.getBody().getPosition()), user.getBody().getAngle());
	}

	@Override
	protected void startSwing() {
		unFix();
		Pose pose = getSwingPose();
		setPositionFull(pose);

		setCollisionFilter(CollisionFilters.ALL_CATEGORY, CollisionFilters.WEAPON_MASK);

		PrismaticJointDef def = new PrismaticJointDef();
		def.initialize(user.getBody(), getBody(), user.getBody().getPosition(),
				pose.getPos().cpy().sub(user.getBody().getPosition()));
		def.collideConnected = false;
		def.enableLimit = true;
		def.upperTranslation = getLeadComponent().getWidth() / 2f;
		def.lowerTranslation = 0f;
		GameContext.inst();
		joint = (PrismaticJoint) WorldUtil.createJoint(def);

		getBody().applyLinearImpulse(user.getHeading().cpy().setLength(user.getStrength()), getBody().getPosition(),
				true);

		stopAction = new DelayedAction(0.3f, () -> requireSwitchState(HandheldState.FIXED));
		GameContext.inst();
		GameContext.getGameStage().addAction(stopAction);
		setupStopAction();
	}

	@Override
	protected void stopSwing() {
		getBody().setBullet(false);
		setCollisionFilter(CollisionFilters.GROUND_CATEGORY, CollisionFilters.SMALL_ITEM_MASK);
		GameContext.inst();
		WorldUtil.destroyJoint(joint);
		fix();
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
		weld.initialize(target.getBody(), getBody(), target.getBody().getPosition());
		weld.collideConnected = false;
		weld.frequencyHz = 0;
		weld.dampingRatio = 1;
		GameContext.inst();
		stickJoint.setJoint(WorldUtil.createJoint(weld));
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

	class SpearStateMachine<T extends Spear> extends MeleeWeaponStateMachine<T> {

		SpearStateMachine(T subject) {
			super(subject);

			List<Transition> transitionz = new ArrayList<>();

			transitionz.add(new Transition(HandheldState.THROWN, HandheldState.STICKING,
					() -> stickRunnable != null && user == null, args -> {
						stickRunnable.run();
						stickRunnable = null;
					}));

			transitionz.add(new Transition(HandheldState.STICKING, HandheldState.FREE, () -> user == null,
					args -> getSubject().unequip()));

			this.addTransitions(transitionz);
		}

	}
}
