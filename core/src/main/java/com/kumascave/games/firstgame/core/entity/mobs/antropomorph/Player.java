package com.kumascave.games.firstgame.core.entity.mobs.antropomorph;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kumascave.games.firstgame.AppContext;
import com.kumascave.games.firstgame.ConfigKey;
import com.kumascave.games.firstgame.core.GameContext;
import com.kumascave.games.firstgame.core.mechanics.alignment.Alignment;
import com.kumascave.games.firstgame.core.physics.Friction;
import com.kumascave.games.firstgame.core.physics.Pose;
import com.kumascave.games.firstgame.core.physics.shapes.Circle;
import com.kumascave.games.firstgame.core.ui.input.InputActions;

public class Player extends Human {

	private static float playerDiam = 0.5f;
	private static Pose startingPose = new Pose(0f, 0f, 0);
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
		setDrawable(new TextureRegionDrawable(
				new TextureRegion(AppContext.inst().getAssetManager().get("player.png", Texture.class))));

		itemCollector = new ItemCollector(this);
	}

	@Override
	public void act(float deltaT) {
		setTarget(GameContext.inst().getMousePosition());
		super.act(deltaT);
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
			Vector2 pos = GameContext.inst().getMousePosition();
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
		hand.release();

	}

	@Override
	public void addToWorld() {
		super.addToWorld();
		itemCollector.addSensor(body);
	}

}
