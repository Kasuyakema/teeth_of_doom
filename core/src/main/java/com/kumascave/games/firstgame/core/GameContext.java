package com.kumascave.games.firstgame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kumascave.games.firstgame.core.entity.mobs.antropomorph.Player;
import com.kumascave.games.firstgame.core.mechanics.damage.GlobalContactListener;
import com.kumascave.games.firstgame.core.ui.HudController;
import com.kumascave.games.firstgame.core.ui.actors.HudRootActor;
import com.kumascave.games.firstgame.core.ui.camera.CameraController;
import com.kumascave.games.firstgame.core.ui.input.GameInputListener;
import com.kumascave.games.firstgame.core.ui.input.HudInputListener;
import com.kumascave.games.firstgame.core.world.CPos;
import com.kumascave.games.firstgame.core.world.LayeredStage;
import com.kumascave.games.firstgame.screens.GameScreen;

import lombok.Getter;
import lombok.Setter;

public class GameContext implements Disposable {
	private static GameContext instance;
	@Getter
	private LayeredStage gameStage;
	@Getter
	private Stage hudStage;
	@Getter
	private World world;
	@Getter
	private CameraController cameraController;
	@Getter
	private HudController hudController;
	@Getter
	@Setter
	private GameScreen gameScreen;
	@Getter
	Player player;

	private GameContext() {
		initWorld();
		initHudStage();
		hudController = new HudController();
	}

	private void initGameStage() {
		// TODO load starting pos
		gameStage = new LayeredStage(new ScreenViewport(), new CPos(0, 0));
		OrthographicCamera cam = (OrthographicCamera) gameStage.getCamera();
		float seitenVerh = cam.viewportWidth / cam.viewportHeight;
		cam.viewportHeight = 5;
		cam.viewportWidth = seitenVerh * 5;
		cam.position.x = cam.viewportWidth / 2;
		cam.position.y = cam.viewportHeight / 2;

		gameStage.addListener(new GameInputListener());
		// gameStage.setDebugAll(true);
		cameraController = new CameraController(gameStage);
	}

	private void initHudStage() {
		hudStage = new Stage(new ScreenViewport());
		hudStage.addListener(new HudInputListener());
		// hudStage.setDebugAll(true);
	}

	private void initWorld() {
		World.setVelocityThreshold(1.0f);
		world = new World(new Vector2(0, 0), true);
		world.setContactListener(new GlobalContactListener());
	}

	public static GameContext inst() {
		if (instance == null) {
			instance = new GameContext();
		}
		return instance;
	}

	public Vector2 getMousePosition() {
		OrthographicCamera cam = (OrthographicCamera) gameStage.getCamera();
		Vector3 mouseInWorld3D = new Vector3();
		mouseInWorld3D.x = Gdx.input.getX();
		mouseInWorld3D.y = Gdx.input.getY();
		mouseInWorld3D.z = 0;
		cam.unproject(mouseInWorld3D);
		return new Vector2(mouseInWorld3D.x, mouseInWorld3D.y);
	}

	@Override
	public void dispose() {
		world.dispose();
		world = null;
		if (gameStage != null) {
			gameStage.dispose();
		}
		gameStage = null;
		player = null;
		hudStage.dispose();
		hudStage = null;
		if (gameScreen != null) {
			gameScreen.dispose();
			gameScreen = null;
		}
		instance = null;
		System.out.println("GameContext.dispose()");
	}

	public void init() {
		initGameStage();
		player = new Player();
		player.addToWorld();
		hudController.setHudRootActor(new HudRootActor());
		hudStage.addActor(hudController.getHudRootActor());
	}
}
