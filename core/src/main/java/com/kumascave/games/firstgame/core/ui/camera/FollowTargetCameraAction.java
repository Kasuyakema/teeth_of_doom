package com.kumascave.games.firstgame.core.ui.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kumascave.games.firstgame.AppContext;
import com.kumascave.games.firstgame.ConfigKey;
import com.kumascave.games.firstgame.core.GameContext;

public class FollowTargetCameraAction extends Action {

	private Actor target;
	private final OrthographicCamera cam;
	private boolean done = false;
	private float lerp;

	public FollowTargetCameraAction(Actor aTarget) {
		super();
		this.target = aTarget;
		this.cam = GameContext.inst().getCameraController().getCamera();
		lerp = AppContext.inst().getConfigManager().getFloat(ConfigKey.CAMERA_LERP);
	}

	public void stop() {
		this.done = true;
	}

	@Override
	public boolean act(float delta) {
		cam.position.x = cam.position.x + (target.getX() - cam.position.x) * lerp;
		cam.position.y = cam.position.y + (target.getY() - cam.position.y) * lerp;
		cam.update();
		return done;
	}

}
