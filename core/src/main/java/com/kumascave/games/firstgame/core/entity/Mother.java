package com.kumascave.games.firstgame.core.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kumascave.games.firstgame.AppContext;
import com.kumascave.games.firstgame.core.entity.mobs.creeps.Creep;
import com.kumascave.games.firstgame.core.mechanics.damage.Damage;
import com.kumascave.games.firstgame.core.physics.Friction;
import com.kumascave.games.firstgame.core.physics.Pose;
import com.kumascave.games.firstgame.core.physics.shapes.Circle;
import com.kumascave.games.firstgame.core.world.LayeredStage;

public class Mother extends Entity {

	private static float diam = 1.5f;
	private static Friction friction = new Friction(0.5f, 0.5f);
	private static float weight = 200f;

	public Mother(Pose pose) {
		super(new Vector2(diam, diam), pose, new Circle(diam), BodyType.StaticBody, weight, friction, 0.0f);
		setDrawable(new TextureRegionDrawable(
				new TextureRegion(AppContext.inst().getAssetManager().get("mother.png", Texture.class))));
	}

	@Override
	public void resolveDmg(Damage dmg) {
		if (dmg.getSource() instanceof Creep) {
			System.out.println("Ouch!");
			((Creep) dmg.getSource()).dispose();
		}
	}

	@Override
	public int getWorldLayer() {
		return LayeredStage.OBJECT_LAYER;
	}

	@Override
	public void addToWorld() {
		super.addToWorld();
	}

}
