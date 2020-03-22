package com.kumascave.games.teeth_of_doom.core.ui.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kumascave.games.teeth_of_doom.core.GameContext;

import lombok.Getter;

public class CameraController {

	private CameraMode mode = CameraMode.FREE;
	private final Stage stage;
	@Getter
	private Vector2 direction = new Vector2(0, 0);
	@Getter
	private float zoom = 1.00f;
	private boolean moving = false;

	private MoveCameraAction moveCameraAction;
	private FollowTargetCameraAction followCameraAction;

	public CameraController(Stage stage) {
		super();
		this.stage = stage;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Vector2 direction) {
		Vector2 newDirection = direction.cpy();
		this.direction = newDirection;
		if (moving) {
			if (this.direction.len() < 0.1) {
				stopMoving();
			} else {
				moveCameraAction.setTranslation(direction);
			}
		}
	}

	public void stopMoving() {
		if (moving && mode.equals(CameraMode.FREE)) {
			moveCameraAction.stop();
			moveCameraAction = null;
			this.moving = false;
		}
	}

	public void startMoving() {
		if (!moving && direction.len() > 0.1 && mode.equals(CameraMode.FREE)) {
			moveCameraAction = new MoveCameraAction(direction);
			stage.addAction(moveCameraAction);
			this.moving = true;
		}
	}

	public void mergeDirection(Vector2 direction) {
		if (mode.equals(CameraMode.FREE)) {
			Vector2 oldDirection = this.direction.cpy();
			Vector2 newDirection = direction.cpy();
			newDirection.add(oldDirection);
			this.setDirection(newDirection);
			this.startMoving();
		}
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
		applyZoom();
	}

	public void toggleMode() {
		if (this.mode.equals(CameraMode.FREE)) {
			switchMode(CameraMode.LOCKED_TO_ACTOR, GameContext.getPlayer().getLeadComponent());
			return;
		}
		switchMode(CameraMode.FREE, null);
	}

	public void switchMode(CameraMode newMode, Actor target) {
		if (!this.mode.equals(newMode)) {
			if (this.mode.equals(CameraMode.FREE)) {
				direction = new Vector2(0, 0);
				stopMoving();
			}
			if (this.mode.equals(CameraMode.LOCKED_TO_ACTOR)) {
				followCameraAction.stop();
				followCameraAction = null;
			}
			if (newMode.equals(CameraMode.LOCKED_TO_ACTOR)) {
				followCameraAction = new FollowTargetCameraAction(target);
				stage.addAction(followCameraAction);
			}
			this.mode = newMode;
		}

	}

	private void applyZoom() {
		getCamera().zoom = zoom;
	}

	public OrthographicCamera getCamera() {
		return (OrthographicCamera) GameContext.getGameStage().getCamera();
	}

	public enum CameraMode {
		FREE, LOCKED_TO_ACTOR;
	}
}
