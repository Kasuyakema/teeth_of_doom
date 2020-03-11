package com.kumascave.games.firstgame.atest;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.kumascave.games.firstgame.core.entity.item.Item;
import com.kumascave.games.firstgame.core.physics.Friction;
import com.kumascave.games.firstgame.core.physics.Pose;
import com.kumascave.games.firstgame.core.physics.shapes.Rectangle;
import com.kumascave.games.firstgame.core.world.CollisionFilters;

public class Target extends Item {

	private static Friction friction = new Friction(5.5f, 0.5f);
	private static Vector2 size = new Vector2(0.75f, 0.25f);
	private static float weight = 20.0f;
	private static float density = Rectangle.densityFromWeight(weight, size);

	public Target(Pose pose) {
		super(new Vector2(size.x, size.y), pose, new Rectangle(size.x, size.y), BodyType.DynamicBody, density, friction,
				0.2f);
		fixtureDef.filter.categoryBits = CollisionFilters.BIG_CATEGORY;
	}

}
