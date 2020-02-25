package com.kumascave.games.firstgame.core.entity.item;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.kumascave.games.firstgame.core.entity.Entity;
import com.kumascave.games.firstgame.core.physics.Friction;
import com.kumascave.games.firstgame.core.physics.Pose;
import com.kumascave.games.firstgame.core.physics.shapes.Rectangle;
import com.kumascave.games.firstgame.core.world.CollisionFilters;
import com.kumascave.games.firstgame.core.world.LayeredStage;

public class RopeElement extends Entity {

	private static Friction friction = new Friction(0.1f, 0.1f);
	private static float restitution = 0.5f;

	public RopeElement(Pose odom, Vector2 size, float weight) {
		super(size, odom, new Rectangle(size.x, size.y), BodyType.DynamicBody,
				Rectangle.densityFromWeight(weight, size), friction, restitution);
		fixtureDef.filter.categoryBits = CollisionFilters.GROUND_CATEGORY;
		fixtureDef.filter.maskBits = CollisionFilters.SMALL_ITEM_MASK;
	}

	@Override
	public int getWorldLayer() {
		// TODO Auto-generated method stub
		return LayeredStage.OBJECT_LAYER;
	}

	public void instantDispose() {
		_dispose();
	}
}
