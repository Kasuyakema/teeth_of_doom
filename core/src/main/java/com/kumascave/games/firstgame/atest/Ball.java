package com.kumascave.games.firstgame.atest;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.kumascave.games.firstgame.core.entity.item.Item;
import com.kumascave.games.firstgame.core.physics.Friction;
import com.kumascave.games.firstgame.core.physics.Pose;
import com.kumascave.games.firstgame.core.physics.shapes.Circle;
import com.kumascave.games.firstgame.core.world.CollisionFilters;

public class Ball extends Item {

	private static Friction friction = new Friction(0.5f, 0.5f);
	private static float diam = 0.1f;
	private static float weight = 0.3f;
	private static float density = Circle.densityFromWeight(weight, diam);

	public Ball(Vector2 pos) {
		super(new Vector2(diam, diam), new Pose(pos, 0f), new Circle(diam), BodyType.DynamicBody, density, friction,
				0.7f);
		fixtureDef.filter.groupIndex = CollisionFilters.BALLGAME_GROUP;
	}

}
