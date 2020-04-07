package com.kumascave.games.teeth_of_doom.core.entity.mobs;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape;
import com.kumascave.games.teeth_of_doom.core.ai.behavior.Attacking;
import com.kumascave.games.teeth_of_doom.core.entity.Entity;
import com.kumascave.games.teeth_of_doom.core.mechanics.alignment.Aligned;
import com.kumascave.games.teeth_of_doom.core.mechanics.alignment.Alignment;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.Damage;
import com.kumascave.games.teeth_of_doom.core.physics.Friction;
import com.kumascave.games.teeth_of_doom.core.physics.PhysicalEntity;
import com.kumascave.games.teeth_of_doom.core.physics.Pose;
import com.kumascave.games.teeth_of_doom.core.ui.actors.HPBar;
import com.kumascave.games.teeth_of_doom.core.world.CollisionFilters;
import com.kumascave.games.teeth_of_doom.core.world.LayeredStage;
import com.kumascave.games.teeth_of_doom.core.world.tiles.Wall;
import com.kumascave.games.teeth_of_doom.util.jgoodies.VHolder;

import lombok.Getter;
import lombok.Setter;

public abstract class Mob extends Entity implements Aligned, Attacking {
	@Getter
	protected float strength = 3f;
	protected float maxMoveSpeed = 3f;
	protected float maxRotateSpeed = 3f;
	protected float trackSpeed = 8f;
	@Getter
	@Setter
	private PhysicalEntity targetEntity;
	@Getter
	@Setter
	private Vector2 targetPosition;
	@Getter
	@Setter
	private Vector2 attackVector;
	@Getter
	@Setter
	private List<Vector2> path;
	@Getter
	@Setter
	private CompletableFuture<Void> future;
	@Getter
	@Setter
	private boolean lookAtWaypount;
	@Getter
	@Setter
	protected Vector2 movementDir = new Vector2(0, 0);
	@Getter
	protected VHolder<Alignment> alignmentHolder = new VHolder<>(Alignment.NEUTRAL);
	private HPBar<Mob> hpBar;
	@Getter
	protected float attackRange = 0.5f;
	@Getter
	protected float attackCooldownBase = 3.0f;
	@Setter
	@Getter
	protected float attackCooldown = 0f;
	@Getter
	private Proximity proximity;

	protected Mob(Vector2 actorSize, Pose startingOdom, Shape shape, float density, Friction friction,
			float restitution) {
		super(actorSize, startingOdom, shape, BodyType.DynamicBody, density, friction, restitution);
		this.hpBar = new HPBar<>(this);
		setCollisionFilter(CollisionFilters.MOB_CATEGORY, CollisionFilters.MOB_MASK);
		proximity = new Proximity(this);
	}

	@Override
	public void act(float deltaT) {
		super.act(deltaT);
		updateAttackCooldown(deltaT);

		if (isAbleToMove()) {
			if (lookAtWaypount) {
				lookAt(path.get(0));
			} else if (targetEntity != null) {
				lookAt(targetEntity.getPose().getPos());
			}

			if (!movementDir.isZero()) {
				Vector2 curVel = getBody().getLinearVelocity();
				float velInDir = (float) Math.cos(movementDir.angleRad(curVel)) * curVel.len();
				// accelerate in movement direction up to maxMovementSpeed * desired speed
				float velInDirMax = 0.5f * maxMoveSpeed * (2 - Math.abs(movementDir.angle(getHeading())) / 180)
						* movementDir.len();
				float deltaV = velInDirMax - Math.max(velInDir, 0);
				if (deltaV > 0) {
					float force = deltaV * getBody().getMass() / 0.1f;
					Vector2 directedForce = new Vector2(movementDir);
					directedForce.setLength(force);
					getBody().applyForceToCenter(directedForce, true);
				}
			}
		}
	}

	protected boolean isAbleToMove() {
		if (!isAlive()) {
			return false;
		}
		float vAng = getBody().getAngularVelocity();
		return vAng < maxRotateSpeed;
	}

	protected void lookAt(Vector2 target) {
		float dif = target.cpy().sub(getBody().getPosition()).angle(getHeading());
		if (Math.abs(dif) > 0.01) {
			float newVel = Math.min(maxRotateSpeed, -dif / trackSpeed);
			getBody().setAngularVelocity(newVel);
			getBody().setAwake(true);
		}
	}

	@Override
	public void applyFriction(float deltaT) {

		applyAngularFriction(deltaT);

		Vector2 curVel = getBody().getLinearVelocity();
		float curVelLen = curVel.len();
		float velInDir;
		if (movementDir.isZero()) {
			velInDir = 0;
		} else {
			velInDir = (float) Math.cos(movementDir.angleRad(curVel)) * curVelLen;
		}

		float fGleit = friction.getLinearFriction() * getBody().getMass();
		Vector2 wGleit;
		float fMax;
		if (velInDir <= 0 || velInDir > getDesiredVelocity()) {
			// apply friction to full velocity
			wGleit = new Vector2(-curVel.x, -curVel.y);
			fMax = curVelLen * getBody().getMass() / deltaT;
		} else {
			// apply friction perpendicular to movement direction. Dont apply friction in
			// desired direction
			float velPerDir = (float) Math.sin(movementDir.angleRad(curVel)) * curVelLen;
			wGleit = new Vector2(velPerDir * movementDir.y, -velPerDir * movementDir.x);
			fMax = Math.abs(velPerDir) * getBody().getMass() / deltaT;
		}
		if (fGleit > fMax) {
			wGleit.setLength(fMax);
		} else {
			wGleit.setLength(fGleit);
		}
		getBody().applyForceToCenter(wGleit, true);
	}

	@Override
	public void _removeFromWorld() {
		hpBar.remove();
		super._removeFromWorld();
	}

	@Override
	public Alignment getAlignment() {
		return getAlignmentHolder().getValue();
	}

	@Override
	public void setAlignment(Alignment newAlignment) {
		getAlignmentHolder().setValue(newAlignment);
	}

	@Override
	protected void _dispose() {
		hpBar.dispose();
		super._dispose();
	}

	@Override
	public void resolveDmg(Damage dmg) {
		if (dmg.getSource() instanceof Wall) {
			// hardnesses and stuff
			Vector2 vel = getBody().getLinearVelocity();
			float velInDir = Math.abs((float) Math.cos(dmg.getWorldManifold().getNormal().angleRad(vel)) * vel.len());
			if (velInDir > 0.1f) {
				int impactDmg = (int) (velInDir * (1.0f - getBody().getFixtureList().get(0).getRestitution()));
				System.out.println("Impacted for " + impactDmg + " DMG! " + System.currentTimeMillis() / 1000.0f);
				dmg.addDmg(impactDmg);
			}
		}
		super.resolveDmg(dmg);
	}

	@Override
	public int getWorldLayer() {
		return LayeredStage.OBJECT_LAYER;
	}

	@Override
	public void attack() {
		Attacking.super.attack();
		System.out.println("Attack by " + getClass().getCanonicalName());
	}

	protected float getDesiredVelocity() {
		return movementDir.len() * maxMoveSpeed;
	}

	@Override
	public void addToWorld() {
		super.addToWorld();
		proximity.addSensor(getBody());
	}

	public void resetPath() {
		setPath(null);
		if (getFuture() != null) {
			getFuture().cancel(true);
			setFuture(null);
		}
		lookAtWaypount = false;
	}

	@Override
	protected void onDeath() {
		this.setCollisionFilter(CollisionFilters.GROUND_CATEGORY);
		super.onDeath();
	}

}
