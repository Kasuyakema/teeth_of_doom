package com.kumascave.games.firstgame.core.entity.item.weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kumascave.games.firstgame.AppContext;
import com.kumascave.games.firstgame.core.GameContext;
import com.kumascave.games.firstgame.core.entity.mobs.antropomorph.Hand.HandType;
import com.kumascave.games.firstgame.core.mechanics.damage.Damage;
import com.kumascave.games.firstgame.core.mechanics.damage.DmgResolving;
import com.kumascave.games.firstgame.core.physics.Friction;
import com.kumascave.games.firstgame.core.physics.Pose;
import com.kumascave.games.firstgame.core.physics.shapes.Rectangle;
import com.kumascave.games.firstgame.core.world.CollisionFilters;

public class PegLeg extends MeleeWeapon {

	private static Friction friction = new Friction(0.5f, 0.5f);

	private static Vector2 size = new Vector2(0.5f, 0.04f);
	private static Vector2 actorSize = new Vector2(size.x, 2 * size.y);

	private static float density = Rectangle.densityFromWeight(1.0f, size);
	private static float restitution = 0.7f;

	private float swingAngle = 1.5f;

	protected static final int STATE_SWING = 20;

	RevoluteJoint revJoint;
	DistanceJoint distJoint;

	public PegLeg(Pose odom) {
		super(actorSize, odom, new Rectangle(size.x, size.y), BodyType.DynamicBody, density, friction, restitution);
		setDrawable(new TextureRegionDrawable(
				new TextureRegion(AppContext.inst().getAssetManager().get("pegleg.png", Texture.class))));
	}

	@Override
	protected void stopSwing() {
		body.setBullet(false);
		setCollisionFilter(CollisionFilters.GROUND_CATEGORY, CollisionFilters.SMALL_ITEM_MASK);
		GameContext.inst().getWorld().destroyJoint(distJoint);
		GameContext.inst().getWorld().destroyJoint(revJoint);
		fix();
	}

	@Override
	protected void startSwing() {
		int positionSign = 1;
		if (position == HandType.LEFT) {
			positionSign = -1;
		}

		unFix();
		body.setBullet(true);
		setCollisionFilter(CollisionFilters.ALL_CATEGORY, CollisionFilters.WEAPON_MASK,
				CollisionFilters.BALLGAME_GROUP);
		Pose pose = getSwingPose();
		setPositionFull(pose);

		RevoluteJointDef def = new RevoluteJointDef();
		def.initialize(body, user.getBody(), user.getBody().getWorldCenter());
		def.enableLimit = true;
		def.upperAngle = swingAngle;
		def.lowerAngle = -swingAngle;
		def.motorSpeed = positionSign * -1000;
		def.maxMotorTorque = user.getStrength() * 15;
		def.enableMotor = true;
		revJoint = (RevoluteJoint) GameContext.inst().getWorld().createJoint(def);

		DistanceJointDef defDist = new DistanceJointDef();
		defDist.initialize(user.getBody(), body, user.getBody().getPosition(), pose.getPos());
		defDist.frequencyHz = 0;
		defDist.dampingRatio = 1;
		distJoint = (DistanceJoint) GameContext.inst().getWorld().createJoint(defDist);

		setupStopAction();
	}

	protected Pose getSwingPose() {
		if (position == HandType.LEFT) {
			return new Pose(user.getHeading().cpy().setLength(0.55f).rotateRad(swingAngle / 2)
					.add(user.getBody().getPosition()), user.getBody().getAngle() + swingAngle / 2);
		} else {
			return new Pose(user.getHeading().cpy().setLength(0.55f).rotateRad(-swingAngle / 2)
					.add(user.getBody().getPosition()), user.getBody().getAngle() - swingAngle / 2);
		}
	}

	@Override
	public Damage calculateDmg(DmgResolving target, WorldManifold worldManifold) {
		Vector2 vel = getBody().getLinearVelocity();
		float velInDir = Math.abs((float) Math.cos(worldManifold.getNormal().angleRad(vel)) * vel.len());
		int dmg = (int) velInDir;
		System.out.println("PENG! " + dmg + " DMG! " + System.currentTimeMillis() / 1000.0f);

		// if (status == HandheldState.SWING) {
		// // TODO: Check if we hit something unmoving/heavy?
		// stopAction.setDelay(0.1f);
		// }

		return new Damage(this, dmg, worldManifold);
	}

	@Override
	protected Vector2 getSize() {
		return size;
	}

}