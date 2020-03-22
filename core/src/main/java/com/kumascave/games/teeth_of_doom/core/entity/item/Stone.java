package com.kumascave.games.teeth_of_doom.core.entity.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kumascave.games.teeth_of_doom.AppContext;
import com.kumascave.games.teeth_of_doom.core.physics.Friction;
import com.kumascave.games.teeth_of_doom.core.physics.Pose;
import com.kumascave.games.teeth_of_doom.core.physics.shapes.Circle;
import com.kumascave.games.teeth_of_doom.core.world.CollisionFilters;

public class Stone extends Item {

	private static Friction friction = new Friction(0.7f, 0.7f);
	private static float weight = 1f;
	private static float diam = 0.1f;
	private static float actorScale = 2f;
	private static float density = Circle.densityFromWeight(weight, diam);
	private static float restitution = 0.1f;

	public Stone(Pose startingPose) {
		super(new Vector2(diam * actorScale, diam * actorScale), startingPose, new Circle(diam), BodyType.DynamicBody,
				density, friction, restitution);
		setCollisionFilter(CollisionFilters.GROUND_CATEGORY, CollisionFilters.SMALL_ITEM_MASK);
		getLeadComponent().setDrawable(new TextureRegionDrawable(
				new TextureRegion(AppContext.inst().getAssetManager().get("stone.png", Texture.class))));
		// float toPx = GameContext.inst().getCameraController().getCamera().project(new
		// Vector3(1, 0, 0)).x;
		// System.out.println(toPx);
		// float prefSize = diam * actorScale * toPx;
		// System.out.println(prefSize);
		// getDrawable().setMinHeight(0);
		// getDrawable().setMinWidth(0);
	}

}
