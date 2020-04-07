package com.kumascave.games.teeth_of_doom.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kumascave.games.teeth_of_doom.core.entity.Mother;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.antropomorph.Player;
import com.kumascave.games.teeth_of_doom.core.mechanics.HuntDirector;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.GlobalContactListener;
import com.kumascave.games.teeth_of_doom.core.physics.WorldUtil;
import com.kumascave.games.teeth_of_doom.core.ui.HudController;
import com.kumascave.games.teeth_of_doom.core.ui.actors.HudRootActor;
import com.kumascave.games.teeth_of_doom.core.ui.camera.CameraController;
import com.kumascave.games.teeth_of_doom.core.ui.input.GameInputListener;
import com.kumascave.games.teeth_of_doom.core.ui.input.HudInputListener;
import com.kumascave.games.teeth_of_doom.core.world.CPos;
import com.kumascave.games.teeth_of_doom.core.world.CollisionMap;
import com.kumascave.games.teeth_of_doom.core.world.LayeredStage;
import com.kumascave.games.teeth_of_doom.screens.GameScreen;
import com.kumascave.games.teeth_of_doom.util.gif.FrameMemory;
import com.kumascave.games.teeth_of_doom.util.jgoodies.VHolder;

import lombok.Setter;

public class GameContext implements Disposable {
	private static GameContext instance;

	private LayeredStage gameStage;

	private Stage hudStage;

	private World world;

	private CameraController cameraController;

	private HudController hudController;

	@Setter
	private GameScreen gameScreen;

	private Player player;

	private Mother mother;

	private VHolder<Boolean> huntOngoing;

	private HuntDirector huntDirector;

	public static final RandomXS128 RANDOM = new RandomXS128();

	private GameContext() {
		initWorld();
		initHudStage();
		hudController = new HudController();
		huntOngoing = new VHolder<>(true);
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
		WorldUtil.inst().setWorld(world);
	}

	public static GameContext inst() {
		if (instance == null) {
			instance = new GameContext();
		}
		return instance;
	}

	public static Vector2 getMousePosition() {
		return inst()._getMousePosition();
	}

	private Vector2 _getMousePosition() {
		OrthographicCamera cam = (OrthographicCamera) gameStage.getCamera();
		Vector3 mouseInWorld3D = new Vector3();
		mouseInWorld3D.x = Gdx.input.getX();
		mouseInWorld3D.y = Gdx.input.getY();
		mouseInWorld3D.z = 0;
		cam.unproject(mouseInWorld3D);
		return new Vector2(mouseInWorld3D.x, mouseInWorld3D.y);
	}

	public static CollisionMap getCollisionMap() {
		return inst()._getCollisionMap();
	}

	private CollisionMap _getCollisionMap() {
		return _getGameStage().getTileLayer().getMap().getCollisionMap();
	}

	@Override
	public void dispose() {
		world.dispose();
		world = null;
		if (gameStage != null) {
			gameStage.dispose();
		}
		gameStage = null;
		FrameMemory.dispose();
		player = null;
		mother = null;
		hudStage.dispose();
		hudStage = null;
		if (gameScreen != null) {
			gameScreen.dispose();
			gameScreen = null;
		}
		instance = null;
		huntOngoing = null;
		if (huntDirector != null) {
			huntDirector.dispose();
		}
		huntDirector = null;
		// System.out.println("GameContext.dispose()");
	}

	public static void init() {
		inst()._init();
	}

	private void _init() {
		initGameStage();
		player = new Player();
		player.addToWorld();
		mother = new Mother();
		mother.addToWorld();
		hudController.setHudRootActor(new HudRootActor());
		hudStage.addActor(hudController.getHudRootActor());
		huntDirector = new HuntDirector();
	}

	public static LayeredStage getGameStage() {
		return inst()._getGameStage();
	}

	public static Stage getHudStage() {
		return inst()._getHudStage();
	}

	public static CameraController getCameraController() {
		return inst()._getCameraController();
	}

	public static HudController getHudController() {
		return inst()._getHudController();
	}

	public static GameScreen getGameScreen() {
		return inst()._getGameScreen();
	}

	public static Player getPlayer() {
		return inst()._getPlayer();
	}

	public static Mother getMother() {
		return inst()._getMother();
	}

	private LayeredStage _getGameStage() {
		return gameStage;
	}

	private Stage _getHudStage() {
		return hudStage;
	}

	private CameraController _getCameraController() {
		return cameraController;
	}

	private HudController _getHudController() {
		return hudController;
	}

	private GameScreen _getGameScreen() {
		return gameScreen;
	}

	private Player _getPlayer() {
		return player;
	}

	private Mother _getMother() {
		return mother;
	}

	public static VHolder<Boolean> getHuntOngoing() {
		return inst()._getHuntOngoing();
	}

	private VHolder<Boolean> _getHuntOngoing() {
		return huntOngoing;
	}

}
