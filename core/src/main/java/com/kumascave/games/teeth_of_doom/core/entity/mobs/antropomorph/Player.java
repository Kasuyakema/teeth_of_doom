package com.kumascave.games.teeth_of_doom.core.entity.mobs.antropomorph;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kumascave.games.teeth_of_doom.AppContext;
import com.kumascave.games.teeth_of_doom.ConfigKey;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.mechanics.alignment.Alignment;
import com.kumascave.games.teeth_of_doom.core.physics.Friction;
import com.kumascave.games.teeth_of_doom.core.physics.Pose;
import com.kumascave.games.teeth_of_doom.core.physics.shapes.Circle;
import com.kumascave.games.teeth_of_doom.core.ui.input.InputActions;
import com.kumascave.games.teeth_of_doom.screens.TitleScreen;

public class Player extends Human {

	private static float playerDiam = 0.5f;
	private static Pose startingPose = new Pose(-2f, 0f, 0);
	private static Friction friction = new Friction(20f, 0.5f, 0.8f, 0.5f, 0.5f);
	private static float weight = 80f;
	private static float density = Circle.densityFromWeight(weight, playerDiam);

	ItemCollector itemCollector;

	public Player() {
		super(playerDiam, startingPose, density, friction);
		strength = 12;
		trackSpeed = 2;
		maxRotateSpeed = 100;
		setAlignment(Alignment.ALLY);
		components.get(0).setDrawable(new TextureRegionDrawable(
				new TextureRegion(AppContext.inst().getAssetManager().get("player.png", Texture.class))));

		itemCollector = new ItemCollector(this);
	}

	@Override
	public void act(float deltaT) {
		super.act(deltaT);
		lookAt(GameContext.getMousePosition());
	}

	@Override
	protected boolean isAbleToMove() {
		return true;
	}

	public void leftHandAction() {
		grabThrowRelease(getLeftHand());
	}

	public void rightHandAction() {
		grabThrowRelease(getRightHand());
	}

	public void grabThrowRelease(Hand hand) {
		if (hand.isInState(HandState.EMPTY)) {
			GameContext.inst();
			Vector2 pos = GameContext.getMousePosition();
			hand.grab(pos);
			return;
		}
		if (InputActions.isPressed(ConfigKey.KEYBIND_THROW)) {
			hand.switchState(HandState.THROW, getStrength());
			return;
		}
		if (hand.isInState(HandState.EQUIPPED)) {
			hand.getEquippedObject().use();
			return;
		}
		hand.switchState(HandState.EMPTY);

	}

	@Override
	public void addToWorld() {
		super.addToWorld();
		itemCollector.addSensor(getBody());
	}

	@Override
	protected void onDeath() {
		// ToDo: Death message
		super.onDeath();
		AppContext.inst().getGame().setScreen(new TitleScreen());
		GameContext.inst().dispose();
	}

}
