package com.kumascave.games.firstgame.core.entity.mobs;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape;
import com.kumascave.games.firstgame.core.entity.Entity;
import com.kumascave.games.firstgame.core.mechanics.alignment.Aligned;
import com.kumascave.games.firstgame.core.mechanics.alignment.Alignment;
import com.kumascave.games.firstgame.core.mechanics.damage.Damage;
import com.kumascave.games.firstgame.core.physics.Friction;
import com.kumascave.games.firstgame.core.physics.Pose;
import com.kumascave.games.firstgame.core.ui.actors.HPBar;
import com.kumascave.games.firstgame.core.world.CollisionFilters;
import com.kumascave.games.firstgame.core.world.LayeredStage;
import com.kumascave.games.firstgame.core.world.tiles.Wall;
import com.kumascave.games.firstgame.util.jgoodies.VHolder;

import lombok.Getter;
import lombok.Setter;

public abstract class Mob extends Entity implements Aligned {
	@Getter
	protected float strength = 3f;

	protected float maxMoveSpeed = 3f;
	protected float maxRotateSpeed = 3f;
	protected float trackSpeed = 8f;

	@Getter
	@Setter
	protected Vector2 target;

	@Getter
	@Setter
	protected Vector2 movementDir = new Vector2(0, 0);

	@Getter
	protected VHolder<Alignment> alignmentHolder = new VHolder<>(Alignment.NEUTRAL);

	private HPBar<Mob> hpBar;

	protected Mob(Vector2 actorSize, Pose startingOdom, Shape shape, float density, Friction friction,
			float restitution) {
		super(actorSize, startingOdom, shape, BodyType.DynamicBody, density, friction, restitution);
		this.hpBar = new HPBar<>(this);
		fixtureDef.filter.categoryBits = CollisionFilters.MOB_CATEGORY;
		// fixtureDef.filter.maskBits = CollisionFilters.MOB_MASK;
	}

	@Override
	public void act(float deltaT) {
		super.act(deltaT);
		if (isAbleToMove()) {

			if (target != null) {
				lookAtTarget();
			}

			if (!movementDir.isZero()) {
				Vector2 curVel = body.getLinearVelocity();
				float velInDir = (float) Math.cos(movementDir.angleRad(curVel)) * curVel.len();
				// accelerate in movement direction up to maxMovementSpeed
				float velInDirMax = 0.5f * maxMoveSpeed * (2 - Math.abs(movementDir.angle(getHeading())) / 180);
				float deltaV = velInDirMax - Math.max(velInDir, 0);
				if (deltaV > 0) {
					float force = deltaV * body.getMass() / 0.1f;
					Vector2 directedForce = new Vector2(movementDir);
					directedForce.setLength(force);
					body.applyForceToCenter(directedForce, true);
				}
			}
		}
	}

	protected boolean isAbleToMove() {
		if (!isAlive()) {
			return false;
		}
		float vAng = body.getAngularVelocity();
		return vAng < maxRotateSpeed;
	}

	protected void lookAtTarget() {
		float dif = target.cpy().sub(body.getPosition()).angle(getHeading());
		if (Math.abs(dif) > 0.01) {
			float newVel = Math.min(maxRotateSpeed, -dif / trackSpeed);
			body.setAngularVelocity(newVel);
			body.setAwake(true);
		}
	}

	@Override
	public void applyFriction(float deltaT) {
		Vector2 curVel = body.getLinearVelocity();
		float curVelLen = curVel.len();
		float velInDir;
		if (movementDir.isZero()) {
			velInDir = 0;
		} else {
			velInDir = (float) Math.cos(movementDir.angleRad(curVel)) * curVelLen;
		}

		float fGleit = friction.getLinearFriction() * body.getMass();
		Vector2 wGleit;
		float fMax;
		if (velInDir <= 0 || velInDir > maxMoveSpeed) {
			// apply friction to full velocity
			wGleit = new Vector2(-curVel.x, -curVel.y);
			fMax = curVelLen * body.getMass() / deltaT;
		} else {
			// apply friction perpendicular to movement direction. Dont apply friction in
			// desired direction
			float velPerDir = (float) Math.sin(movementDir.angleRad(curVel)) * curVelLen;
			wGleit = new Vector2(velPerDir * movementDir.y, -velPerDir * movementDir.x);
			fMax = Math.abs(velPerDir) * body.getMass() / deltaT;
		}
		if (fGleit > fMax) {
			wGleit.setLength(fMax);
		} else {
			wGleit.setLength(fGleit);
		}
		body.applyForceToCenter(wGleit, true);
	}

	@Override
	public boolean remove() {
		hpBar.remove();
		return super.remove();
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
}
