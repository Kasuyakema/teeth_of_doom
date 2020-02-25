package com.kumascave.games.firstgame.core.entity.mobs.creeps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kumascave.games.firstgame.AppContext;
import com.kumascave.games.firstgame.core.entity.mobs.Mob;
import com.kumascave.games.firstgame.core.mechanics.alignment.Alignment;
import com.kumascave.games.firstgame.core.physics.Friction;
import com.kumascave.games.firstgame.core.physics.Pose;
import com.kumascave.games.firstgame.core.physics.shapes.Circle;

public class Creep extends Mob {

	private static Friction friction = new Friction(0.7f, 0.7f);
	private static float weight = 3f;
	private static float diam = 0.2f;
	private static float density = Circle.densityFromWeight(weight, diam);
	private static float restitution = 0.1f;

	public Creep(Pose pose, float weight) {
		super(new Vector2(diam, diam), pose, new Circle(diam), density, friction, restitution);
		maxMoveSpeed = 1;
		strength = 1;
		getHpMaxHolder().setValue(30);
		target = new Vector2(0, 0);
		setAlignment(Alignment.HOSTILE);
		setScale(1.8f);
		setDrawable(new TextureRegionDrawable(
				new TextureRegion(AppContext.inst().getAssetManager().get("Tex_wood_spider.png", Texture.class))));
	}

	@Override
	public void act(float deltaT) {
		Vector2 position = getBody().getPosition();

		this.setMovementDir(new Vector2(-position.x, -position.y));

		super.act(deltaT);
	}

}
