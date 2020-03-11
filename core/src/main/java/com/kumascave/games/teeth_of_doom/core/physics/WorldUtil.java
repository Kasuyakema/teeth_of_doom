package com.kumascave.games.teeth_of_doom.core.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.World;
import com.kumascave.games.teeth_of_doom.core.GameContext;

import lombok.Setter;

public class WorldUtil {
	private static WorldUtil instance;

	@Setter
	World world;

	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

	boolean stepping = false;

	public static Body createBody(BodyDef bodyDef) {
		return inst()._createBody(bodyDef);
	}

	private Body _createBody(BodyDef bodyDef) {
		requireOutsideStep();
		return world.createBody(bodyDef);
	}

	public static void destroyBody(Body body) {
		inst()._destroyBody(body);
	}

	private void _destroyBody(Body body) {
		requireOutsideStep();
		world.destroyBody(body);
	}

	public static Joint createJoint(JointDef jointDef) {
		return inst()._createJoint(jointDef);
	}

	private Joint _createJoint(JointDef jointDef) {
		requireOutsideStep();
		return world.createJoint(jointDef);
	}

	public static void destroyJoint(Joint joint) {
		inst()._destroyJoint(joint);
	}

	private void _destroyJoint(Joint joint) {
		requireOutsideStep();
		world.destroyJoint(joint);
	}

	public static void worldStep() {
		inst()._worldStep();
	}

	private void _worldStep() {
		stepping = true;
		world.step(Gdx.graphics.getDeltaTime(), 500, 500);
		stepping = false;
	}

	protected void requireOutsideStep() {
		if (stepping) {
			throw new IllegalAccessError("Modification of World during World step is not safe");
		}
	}

	public static WorldUtil inst() {
		if (instance == null) {
			instance = new WorldUtil();
		}
		return instance;
	}

	public static void render() {
		inst()._render();
	}

	private void _render() {
		debugRenderer.render(world, GameContext.getGameStage().getCamera().combined);
	}
}
