package com.kumascave.games.teeth_of_doom.core.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.physics.Friction;
import com.kumascave.games.teeth_of_doom.core.physics.Pose;
import com.kumascave.games.teeth_of_doom.core.physics.WorldUtil;
import com.kumascave.games.teeth_of_doom.core.world.CollisionFilters;

import lombok.Getter;

public class EntityComponent extends Image implements Disposable {

	@Getter
	private Entity owner;

	@Getter
	protected Friction friction;

	@Getter
	protected BodyDef bodyDef;
	protected FixtureDef fixtureDef;

	@Getter
	Vector2 actorSize;

	@Getter
	protected Body body;

	private boolean dispose = false;
	private boolean removeFromWorld = false;

	protected EntityComponent(Vector2 actorSize, Pose startingPose, Shape shape, BodyType bodyType, float density,
			Friction friction, float restitution, Entity owner) {
		super();

		this.owner = owner;

		this.setOrigin(Align.center);
		setAlign(Align.center);
		setScaling(Scaling.fit);
		this.friction = friction;
		this.setSize(actorSize.x, actorSize.y);
		this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
		this.rotateBy(startingPose.getAngle());
		this.setPosition(startingPose.getX(), startingPose.getY());
		this.actorSize = actorSize.cpy();

		bodyDef = new BodyDef();
		bodyDef.type = bodyType;
		bodyDef.position.set(startingPose.getPos());
		bodyDef.angle = startingPose.getAngle();

		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = density;
		fixtureDef.friction = friction.getEdgeFriction();
		fixtureDef.restitution = restitution;
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
		if (body != null && body.isAwake()) {
			updateActor();
		}
		super.act(delta);
	}

	public Vector2 getHeading() {
		Vector2 result = new Vector2((float) Math.cos(body.getAngle()), (float) Math.sin(body.getAngle()));
		result.setLength(1.0f);
		return result;
	}

	public void setPositionFull(float x, float y) {
		if (body != null) {
			setPositionFull(x, y, body.getAngle());
		} else {
			setPositionFull(x, y, bodyDef.angle);
		}
	}

	public void setPositionFull(float x, float y, float angle) {
		if (body != null) {
			body.setTransform(new Vector2(x, y), angle);
			updateActor();
		}
		bodyDef.position.set(x, y);
		bodyDef.angle = angle;
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
	public void dispose() {
		this.dispose = true;
	}

	protected void _dispose() {
		WorldUtil.destroyBody(getBody());
		remove();
		clear();
	}

	public void addToWorld() {
		// Create a body in the world using our definition
		if (getBody() == null) {
			body = WorldUtil.createBody(bodyDef);
			Fixture fix = body.createFixture(fixtureDef);
			fix.setUserData(owner);
			body.setLinearDamping(friction.getLinearDampening());
			body.setAngularDamping(friction.getAngularDampening());
			body.setUserData(this);
		}
		if (getStage() == null) {
			setSize(actorSize.x, actorSize.y);
			updateActor();
			GameContext.getGameStage().addActor(this, owner.getWorldLayer());
		}
	}

	public void removeFromWorld() {
		this.removeFromWorld = true;
	}

	public void _removeFromWorld() {
		remove();
		WorldUtil.destroyBody(body);
		body = null;
	}

	public void setCollisionFilter(short category, short mask, short group) {
		if (getBody() != null) {
			Fixture fix = getBody().getFixtureList().get(0);
			Filter filter = fix.getFilterData();
			filter.categoryBits = category;
			filter.maskBits = mask;
			filter.groupIndex = group;
			fix.setFilterData(filter);
			fix.refilter();
		}
		fixtureDef.filter.categoryBits = category;
		fixtureDef.filter.maskBits = mask;
		fixtureDef.filter.groupIndex = group;
	}

	public void setCollisionFilter(short category, short mask) {
		setCollisionFilter(category, mask, CollisionFilters.DEFAULT_GROUP);
	}

}
