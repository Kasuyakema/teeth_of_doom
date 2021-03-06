package com.kumascave.games.teeth_of_doom.core.entity.item.weapon;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kumascave.games.teeth_of_doom.AppContext;
import com.kumascave.games.teeth_of_doom.core.DelayedAction;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.entity.Transition;
import com.kumascave.games.teeth_of_doom.core.entity.item.Arrow;
import com.kumascave.games.teeth_of_doom.core.entity.item.Handheld;
import com.kumascave.games.teeth_of_doom.core.entity.item.HandheldState;
import com.kumascave.games.teeth_of_doom.core.entity.item.ProjectileState;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.antropomorph.Hand.HandType;
import com.kumascave.games.teeth_of_doom.core.physics.Friction;
import com.kumascave.games.teeth_of_doom.core.physics.Pose;
import com.kumascave.games.teeth_of_doom.core.physics.WorldUtil;
import com.kumascave.games.teeth_of_doom.core.physics.shapes.DShape;

public class Bow extends Handheld {

	protected static final int STATE_FIRE = 40;

	private static Friction friction = new Friction(0.5f, 0.5f);
	private static Vector2 size = new Vector2(0.1f, 0.5f);
	private static float density = DShape.densityFromWeight(1.0f, size);
	private static float restitution = 0.7f;
	private int dmg = 15;
	private float power = 0.3f;

	// VHolder<Boolean> firePosCheckResult = new VHolder<>();

	public Bow(Pose odom) {
		super(size, odom, new DShape(size.x, size.y), BodyType.DynamicBody, density, friction, restitution);
		getLeadComponent().setDrawable(new TextureRegionDrawable(
				new TextureRegion(AppContext.inst().getAssetManager().get("bow.png", Texture.class))));
		stateMachine = new BowStateMachine<Bow>(this);
//		firePosCheckResult.addPropertyChangeListener(evt -> {
//			if (evt.getNewValue() != null) {
//				if ((Boolean) evt.getNewValue()) {
//
//					firePosCheckResult.setValue(null);
//				} else {
//					firePosCheckResult.setValue(null);
//				}
//			}
//		});
	}

	@Override
	public void use() {
		stateMachine.transition(HandheldState.FIRE);
	}

	@Override
	protected Pose getFixedPose() {
		if (position == HandType.LEFT) {
			return new Pose(user.getHeading().cpy().rotateRad((float) Math.PI / 2)
					.setLength(user.getLeadComponent().getWidth() / 2 + size.x / 2).add(user.getBody().getPosition()),
					user.getBody().getAngle() + (float) Math.PI / 2);
		} else {
			return new Pose(user.getHeading().cpy().rotateRad((float) -Math.PI / 2)
					.setLength(user.getLeadComponent().getWidth() / 2 + size.x / 2).add(user.getBody().getPosition()),
					user.getBody().getAngle() - (float) Math.PI / 2);
		}
	}

	protected void stopFire() {
		WorldUtil.destroyJoint(fixedJoint, getBody());
		fix();
	}

	protected void startFire() {
		unFix();
		Pose pose = getFirePose();
		setPositionFull(pose);
		WeldJointDef weld = new WeldJointDef();
		weld.initialize(user.getBody(), getBody(), user.getBody().getPosition());
		weld.collideConnected = false;
		weld.frequencyHz = 0;
		weld.dampingRatio = 1;
		fixedJoint = (WeldJoint) WorldUtil.createJoint(weld);
		Arrow arrow = new Arrow(getArrowPose());
		arrow.addToWorld();
		arrow.requireSwitchState(ProjectileState.FIRED, dmg, user.getHeading().cpy().setLength(power));
		GameContext.getGameStage().addAction(new DelayedAction(0.3f, () -> requireSwitchState(HandheldState.FIXED)));
	}

	protected Pose getFirePose() {
		return new Pose(user.getHeading().cpy().setLength(user.getLeadComponent().getWidth() / 2 + size.x / 2)
				.add(user.getBody().getPosition()), user.getBody().getAngle());
	}

	protected Pose getArrowPose() {
		return new Pose(
				user.getHeading().cpy().setLength(user.getLeadComponent().getWidth() / 2f + Arrow.size.x / 2f + 0.02f)
						.add(user.getBody().getPosition()),
				user.getBody().getAngle());
	}

	@Override
	protected Vector2 getSize() {
		return size;
	}

	class BowStateMachine<T extends Bow> extends HandheldStateMachine<T> {

		protected BowStateMachine(T subject) {
			super(subject);

			List<Transition> transitionz = new ArrayList<>();

			transitionz.add(new Transition(HandheldState.FIXED, HandheldState.FIRE,
					() -> checkPosition(getFirePose().getPos()) && checkPosition(getArrowPose().getPos()),
					args -> getSubject().startFire()));
			transitionz.add(new Transition(HandheldState.FIRE, HandheldState.FIXED,
					() -> checkPosition(getFixedPose().getPos()), args -> getSubject().stopFire()));

			this.addTransitions(transitionz);
		}

	}
}
