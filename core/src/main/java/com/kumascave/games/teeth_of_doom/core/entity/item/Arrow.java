package com.kumascave.games.teeth_of_doom.core.entity.item;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kumascave.games.teeth_of_doom.AppContext;
import com.kumascave.games.teeth_of_doom.core.DelayedAction;
import com.kumascave.games.teeth_of_doom.core.entity.Transition;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.Damage;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.DmgResolving;
import com.kumascave.games.teeth_of_doom.core.physics.Friction;
import com.kumascave.games.teeth_of_doom.core.physics.PhysicalEntity;
import com.kumascave.games.teeth_of_doom.core.physics.Pose;
import com.kumascave.games.teeth_of_doom.core.physics.WorldUtil;
import com.kumascave.games.teeth_of_doom.core.physics.shapes.Rectangle;
import com.kumascave.games.teeth_of_doom.core.world.CollisionFilters;

public class Arrow extends Item {

	private static Friction friction = new Friction(0.1f, 10f, 0.5f, 0.8f, 0.5f);

	public static Vector2 size = new Vector2(0.3f, 0.02f);
	private static Vector2 actorSize = new Vector2(size.x, size.y * 2);

	private static float density = Rectangle.densityFromWeight(0.03f, size);
	private static float restitution = 0.3f;

	private int dmg;

	Runnable stickRunnable;

	public Arrow(Pose odom) {
		super(actorSize, odom, new Rectangle(size.x, size.y), BodyType.DynamicBody, density, friction, restitution);
		getLeadComponent().setDrawable(new TextureRegionDrawable(
				new TextureRegion(AppContext.inst().getAssetManager().get("arrow.png", Texture.class))));
		setCollisionFilter(CollisionFilters.GROUND_CATEGORY, CollisionFilters.SMALL_ITEM_MASK);
		stateMachine = new ArrowStateMachine<Arrow>(this);
	}

	public void shoot(int dmg, Vector2 impulse) {
		// ToDo: make dmg more interesting
		this.dmg = dmg;
		setCollisionFilter(CollisionFilters.ALL_CATEGORY, CollisionFilters.WEAPON_MASK,
				CollisionFilters.PROJECTILE_GROUP);
		getBody().applyLinearImpulse(impulse, getBody().getPosition(), true);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (getState().equals(ProjectileState.FIRED)) {
			if (stickRunnable != null) {
				requireSwitchState(ProjectileState.STICKING);
			}
			switchState(ProjectileState.FREE);
		}
		if (getState().equals(ProjectileState.STICKING)) {
			switchState(ProjectileState.FREE);
		}
	}

	protected void stopShot() {
		setCollisionFilter(CollisionFilters.GROUND_CATEGORY, CollisionFilters.SMALL_ITEM_MASK);
		actorGroup.addAction(new DelayedAction(5f, () -> this.dispose()));
	}

	@Override
	public Damage calculateDmg(DmgResolving target, WorldManifold worldManifold) {
		if (getState().equals(ProjectileState.FIRED)) {
			int oldDmg = dmg;
			dmg = 0;
			if (target instanceof PhysicalEntity && oldDmg > 0) {
				stickRunnable = () -> stick((PhysicalEntity) target, worldManifold);
			}
			return new Damage(this, oldDmg, worldManifold);
		}
		return super.calculateDmg(target, worldManifold);
	}

	private void stick(PhysicalEntity target, WorldManifold worldManifold) {
		setCollisionFilter(CollisionFilters.BASE_CATEGORY, CollisionFilters.COLLISION_DEACTIVATED_MASK);
		Vector2 contactPoint = worldManifold.getPoints()[0];
		Pose pose = new Pose(contactPoint.cpy().sub(getHeading().cpy().setLength(size.x * 0.3f)),
				getHeading().angleRad());
		setPositionFull(pose);
		WeldJointDef weld = new WeldJointDef();
		weld.initialize(target.getBody(), getBody(), target.getBody().getPosition());
		weld.collideConnected = false;
		weld.frequencyHz = 0;
		weld.dampingRatio = 1;
		WorldUtil.createJoint(weld);
		this.getLeadComponent().setZIndex(Math.max(0, target.getZIndex() - 1));
	}

	class ArrowStateMachine<T extends Arrow> extends ItemStateMachine<T> {

		ArrowStateMachine(T subject) {
			super(subject);

			List<Transition> transitionz = new ArrayList<>();

			transitionz.add(new Transition(ProjectileState.FREE, ProjectileState.FIRED,
					args -> getSubject().shoot((Integer) args[0], (Vector2) args[1])));
			transitionz.add(new Transition(ProjectileState.FIRED, ProjectileState.FREE,
					() -> !isMoving() && stickRunnable == null, args -> getSubject().stopShot()));

			transitionz.add(new Transition(ProjectileState.FIRED, ProjectileState.STICKING, () -> stickRunnable != null,
					args -> {
						stickRunnable.run();
						stickRunnable = null;
					}));
			transitionz.add(new Transition(ProjectileState.STICKING, ProjectileState.FREE,
					() -> getBody().getJointList().isEmpty(), args -> getSubject().stopShot()));

			transitionz.add(new Transition(ProjectileState.STICKING, ProjectileState.INVENTORY, args -> {
				getSubject().stopShot();
				prepareForInventory();
			}));
			this.addTransitions(transitionz);
		}

		private boolean isMoving() {
			if (!getBody().isAwake()) {
				return false;
			}
			boolean moving = false;
			moving = getBody().getLinearVelocity().len2() > 0.5f;
			moving = moving || getBody().getAngularVelocity() > 0.5f;
			// TODO: return moving;
			return true;
		}

	}
}
