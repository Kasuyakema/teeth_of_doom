package com.kumascave.games.teeth_of_doom.screens;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.kumascave.games.teeth_of_doom.AppContext;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.entity.item.Stone;
import com.kumascave.games.teeth_of_doom.core.entity.item.weapon.Bow;
import com.kumascave.games.teeth_of_doom.core.entity.item.weapon.PegLeg;
import com.kumascave.games.teeth_of_doom.core.entity.item.weapon.Spear;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.creeps.Creep;
import com.kumascave.games.teeth_of_doom.core.mechanics.spawn.Spawnpoint;
import com.kumascave.games.teeth_of_doom.core.physics.Pose;
import com.kumascave.games.teeth_of_doom.core.physics.WorldUtil;
import com.kumascave.games.teeth_of_doom.util.DynamicVariables;

import lombok.Getter;

public class GameScreen implements Screen {

	@Getter
	private boolean disposing = false;

	public GameScreen() {
		String dirName = "/home/kuma/workspace/main/teeth_of_doom/core/src/main/resources/assets";
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
		// AppContext.inst().getAssetManager().load("gear2.png", Pixmap.class);
		// AppContext.inst().getAssetManager().load("stone_wall.png", Pixmap.class);
		// AppContext.inst().getAssetManager().load("stone_wall.jpg", Texture.class);
		// AppContext.inst().getAssetManager().load("naturalstone.jpg", Texture.class);
		AppContext.inst().getAssetManager().finishLoading();

		GameContext.init();
		GameContext.inst().setGameScreen(this);

		// gameStage.addActor(new Background(), 0);
		// Foreground walls = new Foreground();
		// gameStage.addActor(walls, 1);
		// Floor floor = new Floor(world, 0, 0, 1.00f, 0.10f, 0);
		// gameStage.addActor(floor);
//		new Ball(new Vector2(1.20f, 1.20f)).addToWorld();

		PegLeg leg = new PegLeg(new Pose(1, 1, 1));
		leg.addToWorld();

		new Bow(new Pose(-1, -1, 1)).addToWorld();
		new Spear(new Pose(-1, 1, 0)).addToWorld();

//		new Target(new Pose(3, 3, 1)).addToWorld();
		new Stone(new Pose(2.5f, 3, 1)).addToWorld();
		new Stone(new Pose(2f, 3, 1)).addToWorld();
		new Stone(new Pose(1.5f, 3, 1)).addToWorld();
		new Stone(new Pose(1f, 3, 1)).addToWorld();
		new Stone(new Pose(0.5f, 3, 1)).addToWorld();
		Spawnpoint<Creep> spawnpoint = new Spawnpoint<Creep>(6.0f, () -> new Creep(new Pose(-22.0f, 0.0f, 0.0f), 0.6f),
				0.5f);
		GameContext.getGameStage().addAction(spawnpoint);
		// GameContext.getGameStage().addAction(new DelayedAction(3.0f, () ->
		// spawnpoint.setDestroy(true)));
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		DynamicVariables.debugStage
				.addPropertyChangeListener(evt -> GameContext.getGameStage().setDebugAll((Boolean) evt.getNewValue()));
	}

	@Override
	public void show() {
		Gdx.app.log("GameScreen", "show");
		Gdx.input.setInputProcessor(new InputMultiplexer(GameContext.getHudStage(), GameContext.getGameStage()));
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (!DynamicVariables.pause.getValue()) {
			GameContext.getGameStage().act();
			if (disposing) {
				return;
			}
		}
		GameContext.getHudStage().act();
		if (disposing) {
			return;
		}
		GameContext.getGameStage().draw();
		if (DynamicVariables.debugWorld.getValue()) {
			WorldUtil.render();
		}
		if (DynamicVariables.printMouse.getValue()) {
			System.out.println("Mouse at: " + GameContext.getMousePosition());
		}
		GameContext.getHudStage().draw();
		if (!DynamicVariables.pause.getValue()) {
			WorldUtil.worldStep();
		}
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
		disposing = true;
	}

}
