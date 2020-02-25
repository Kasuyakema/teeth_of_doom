package com.kumascave.games.firstgame.core.ui.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.kumascave.games.firstgame.AppContext;
import com.kumascave.games.firstgame.ConfigKey;
import com.kumascave.games.firstgame.core.GameContext;

public class MoveCameraAction extends Action {

	private Vector2 translation;
	private final OrthographicCamera cam;
	private boolean done = false;
	private float cameraSpeed;

	public MoveCameraAction(Vector2 aTranslation) {
		super();
		this.cam = GameContext.inst().getCameraController().getCamera();
		translation = aTranslation.cpy();
		translation.nor();
		cameraSpeed = AppContext.inst().getConfigManager().getFloat(ConfigKey.CAMERA_SPEED);
	}

	public void stop() {
		this.done = true;
	}

	@Override
	public boolean act(float delta) {
		cam.translate(translation.cpy().scl(cameraSpeed * cam.zoom).scl(delta));
		return done;
	}

	/**
	 * @param translation
	 *            the translation to set
	 */
	public void setTranslation(Vector2 translation) {
		Vector2 newTranslation = translation.cpy();
		newTranslation.nor();
		this.translation = newTranslation;
	}

}
