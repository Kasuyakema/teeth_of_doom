package com.kumascave.games.firstgame.screens;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kumascave.games.firstgame.AppContext;
import com.kumascave.games.firstgame.atest.Ball;
import com.kumascave.games.firstgame.core.GameContext;
import com.kumascave.games.firstgame.core.entity.Mother;
import com.kumascave.games.firstgame.core.entity.item.Stone;
import com.kumascave.games.firstgame.core.entity.item.weapon.Bow;
import com.kumascave.games.firstgame.core.entity.item.weapon.PegLeg;
import com.kumascave.games.firstgame.core.entity.item.weapon.Spear;
import com.kumascave.games.firstgame.core.entity.mobs.creeps.Creep;
import com.kumascave.games.firstgame.core.mechanics.spawn.Spawnpoint;
import com.kumascave.games.firstgame.core.physics.Pose;
import com.kumascave.games.firstgame.core.world.LayeredStage;

public class GameScreen implements Screen {
	private LayeredStage gameStage;
	Stage hudStage;
	private World world;
	private Box2DDebugRenderer debugRenderer;

	public GameScreen() {

		String dirName = "/home/kuma/workspace/main/firstgame/assets";

		try {
			Files.list(new File(dirName).toPath()).forEach(path -> {
				AppContext.inst().getAssetManager().load(StringUtils.substringAfterLast(path.toString(), "/"),
						Texture.class);
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// AppContext.inst().getAssetManager().load("cog.png", Texture.class);
		AppContext.inst().getAssetManager().load("gear2.png", Pixmap.class);
		AppContext.inst().getAssetManager().load("stone_wall.png", Pixmap.class);
		// AppContext.inst().getAssetManager().load("stone_wall.jpg", Texture.class);
		// AppContext.inst().getAssetManager().load("naturalstone.jpg", Texture.class);
		AppContext.inst().getAssetManager().finishLoading();

		GameContext.inst().init();
		GameContext.inst().setGameScreen(this);

		gameStage = GameContext.inst().getGameStage();
		hudStage = GameContext.inst().getHudStage();
		debugRenderer = new Box2DDebugRenderer();

		world = GameContext.inst().getWorld();
		// gameStage.addActor(new Background(), 0);
		// Foreground walls = new Foreground();
		// gameStage.addActor(walls, 1);
		// Floor floor = new Floor(world, 0, 0, 1.00f, 0.10f, 0);
		// gameStage.addActor(floor);
		new Ball(new Vector2(1.20f, 1.20f)).addToWorld();

		new PegLeg(new Pose(1, 1, 1)).addToWorld();

		new Bow(new Pose(-1, -1, 1)).addToWorld();
		new Spear(new Pose(-1, 1, 0)).addToWorld();

		new Stone(new Pose(3, 3, 1)).addToWorld();
		new Stone(new Pose(2.5f, 3, 1)).addToWorld();
		new Stone(new Pose(2f, 3, 1)).addToWorld();
		new Stone(new Pose(1.5f, 3, 1)).addToWorld();
		new Stone(new Pose(1f, 3, 1)).addToWorld();
		new Stone(new Pose(0.5f, 3, 1)).addToWorld();
		gameStage.addAction(new Spawnpoint<Creep>(10.0f, () -> new Creep(new Pose(-5.0f, 5.0f, 0.0f), 0.6f)));

		new Mother(new Pose(0f, 0f, 0)).addToWorld();

		Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}

	@Override
	public void show() {
		Gdx.app.log("GameScreen", "show");
		Gdx.input.setInputProcessor(new InputMultiplexer(GameContext.inst().getHudStage(), gameStage));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameStage.act();
		hudStage.act();
		gameStage.draw();
		debugRenderer.render(world, gameStage.getCamera().combined);
		hudStage.draw();
		world.step(Gdx.graphics.getDeltaTime(), 500, 500);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		gameStage = null;
		world = null;
		debugRenderer = null;
	}

}
