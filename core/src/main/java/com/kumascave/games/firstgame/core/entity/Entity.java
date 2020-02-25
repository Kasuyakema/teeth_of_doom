package com.kumascave.games.firstgame.core.entity;

import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.kumascave.games.firstgame.core.GameContext;
import com.kumascave.games.firstgame.core.mechanics.damage.Damage;
import com.kumascave.games.firstgame.core.mechanics.damage.DmgResolving;
import com.kumascave.games.firstgame.core.mechanics.damage.HasHitpoints;
import com.kumascave.games.firstgame.core.mechanics.damage.HpHolder;
import com.kumascave.games.firstgame.core.physics.Friction;
import com.kumascave.games.firstgame.core.physics.PhysicalEntity;
import com.kumascave.games.firstgame.core.physics.Pose;
import com.kumascave.games.firstgame.core.world.CollisionFilters;
import com.kumascave.games.firstgame.util.jgoodies.VHolder;

import lombok.Getter;
import lombok.Setter;

public abstract class Entity extends Image implements PhysicalEntity, HasHitpoints, Disposable {

	@Getter
	@Setter
	protected PropertyChangeSupport changeSupport;

	@Getter
	private UUID id = UUID.randomUUID();

	@Getter
	protected Friction friction;

	@Getter
	protected BodyDef bodyDef;
	protected FixtureDef fixtureDef;

	@Getter
	protected Body body;

	private final Integer hpMaxBase = 100;

	@Getter
	private VHolder<Boolean> aliveHolder = new VHolder<>(true);

	@Getter
	private VHolder<Integer> hpMaxHolder = new VHolder<>(hpMaxBase);
	@Getter
	private HpHolder hpHolder = new HpHolder(this);

	@Getter
	private Map<UUID, Long> collisionCooldowns = new HashMap<>();

	@Getter
	@Setter
	private Long lastCollisionTime = null;

	private boolean dispose = false;
	private boolean removeFromWorld = false;

	protected Entity(Vector2 actorSize, Pose startingPose, Shape shape, BodyType bodyType, float density,
			Friction friction, float restitution) {
		super();
		this.setOrigin(Align.center);
		setScaling(Scaling.fit);
		this.friction = friction;
		this.setSize(actorSize.x, actorSize.y);
		this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
		this.rotateBy(startingPose.getAngle());
		this.setPosition(startingPose.getX(), startingPose.getY());

		bodyDef = new BodyDef();
		bodyDef.type = bodyType;
		bodyDef.position.set(startingPose.getPos());
		bodyDef.angle = startingPose.getAngle();

		// Now define the dimensions of the physics shape

		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = density;
		fixtureDef.friction = friction.getEdgeFriction();
		fixtureDef.restitution = restitution;

	}

	@Override
	public void applyFriction(float deltaT) {
		PhysicalEntity.super.applyFriction(deltaT);
	}

	@Override
	public void act(float delta) {
		if (removeFromWorld) {
			_removeFromWorld();
			removeFromWorld = false;
		}
		if (dispose) {
			_dispose();
			return;
		}
		super.act(delta);
		if (body != null && body.isAwake()) {
			applyFriction(delta);
			updateActor();
		}
	}

	public Vector2 getHeading() {
		Vector2 result = new Vector2((float) Math.cos(body.getAngle()), (float) Math.sin(body.getAngle()));
		result.setLength(1.0f);
		return result;
	}

	public void setPositionFull(float x, float y) {
		setPositionFull(x, y, body.getAngle());
	}

	public void setPositionFull(float x, float y, float angle) {
		body.setTransform(new Vector2(x, y), angle);
		updateActor();
	}

	public void setPositionFull(Vector2 pos) {
		setPositionFull(pos.x, pos.y);
	}

	public void setPositionFull(Pose pose) {
		setPositionFull(pose.getX(), pose.getY(), pose.getAngle());
	}

	protected void updateActor() {
		this.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		this.setPosition(body.getPosition().x - this.getWidth() / 2, body.getPosition().y - this.getHeight() / 2);
	}

	@Override
	public void resolveDmg(Damage dmg) {
		getHpHolder().sub(dmg.getDmg());
	}

	@Override
	public Damage calculateDmg(DmgResolving target, WorldManifold worldManifold) {
		return new Damage(this, 0, worldManifold);
	}

	@Override
	public void dispose() {
		this.dispose = true;
	}

	protected void _dispose() {
		firePropertyChange(PROPERTY_DESTROY_BODY, this, null);
		getBody().getWorld().destroyBody(getBody());
		Group parent = this.getParent();
		if (parent != null) {
			parent.removeActor(this);
		}
	}

	public void addToWorld() {
		// Create a body in the world using our definition
		body = GameContext.inst().getWorld().createBody(bodyDef);
		Fixture fix = body.createFixture(fixtureDef);
		fix.setUserData(this);
		body.setLinearDamping(friction.getLinearDampening());
		body.setAngularDamping(friction.getAngularDampening());
		body.setUserData(this);
		GameContext.inst().getGameStage().addActor(this, getWorldLayer());
	}

	public void removeFromWorld() {
		this.removeFromWorld = true;
	}

	public void _removeFromWorld() {
		firePropertyChange(PROPERTY_DESTROY_BODY, this, null);
		remove();
		GameContext.inst().getWorld().destroyBody(body);
		body = null;
	}

	public void setCollisionFilter(short category, short mask, short group) {
		Fixture fix = getBody().getFixtureList().get(0);
		Filter filter = fix.getFilterData();
		filter.categoryBits = category;
		filter.maskBits = mask;
		filter.groupIndex = group;
		fix.setFilterData(filter);
		fix.refilter();
	}

	public void setCollisionFilter(short category, short mask) {
		setCollisionFilter(category, mask, CollisionFilters.DEFAULT_GROUP);
	}

	public abstract int getWorldLayer();
}
