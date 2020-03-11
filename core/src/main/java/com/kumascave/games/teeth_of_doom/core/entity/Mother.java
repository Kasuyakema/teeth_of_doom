package com.kumascave.games.teeth_of_doom.core.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kumascave.games.teeth_of_doom.AppContext;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.Mob;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.creeps.Creep;
import com.kumascave.games.teeth_of_doom.core.mechanics.alignment.Alignment;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.Damage;
import com.kumascave.games.teeth_of_doom.core.physics.Friction;
import com.kumascave.games.teeth_of_doom.core.physics.Pose;
import com.kumascave.games.teeth_of_doom.core.physics.shapes.Circle;
import com.kumascave.games.teeth_of_doom.core.world.CollisionFilters;
import com.kumascave.games.teeth_of_doom.core.world.LayeredStage;
import com.kumascave.games.teeth_of_doom.screens.TitleScreen;

public class Mother extends Mob {

	private static float diam = 1.5f;
	private static Friction friction = new Friction(0.5f, 0.5f);
	private static float weight = 300f;

	public Mother(Pose pose) {
		super(new Vector2(diam, diam), pose, new Circle(diam), weight, friction, 0.0f);
		setDrawable(new TextureRegionDrawable(
				new TextureRegion(AppContext.inst().getAssetManager().get("mother.png", Texture.class))));
		fixtureDef.filter.categoryBits = CollisionFilters.BIG_CATEGORY;
		setAlignment(Alignment.ALLY);
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

	@Override
	protected void onDeath() {
		// ToDo: Death message
		AppContext.inst().getGame().setScreen(new TitleScreen());
		GameContext.inst().dispose();
		super.onDeath();
	}

}