package com.kumascave.games.firstgame.screens;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kumascave.games.firstgame.AppContext;
import com.kumascave.games.firstgame.core.GameContext;
import com.kumascave.games.firstgame.core.save.SafeManager;
import com.kumascave.games.firstgame.core.ui.GuiFactory;
import com.kumascave.games.firstgame.core.ui.actors.KeybindingWindow;
import com.kumascave.games.firstgame.core.ui.actors.TitledTable;

public class OptionScreen implements Screen {
	private Stage stage;

	public OptionScreen(Screen previous) {
		super();
		stage = new Stage(new ScreenViewport());

		TitledTable table = new TitledTable("Options Screen");

		stage.addActor(table);

		Table buttonTable = new Table();

		TextButton backButton = GuiFactory.textButton("back", () -> AppContext.inst().getGame().setScreen(previous));

		buttonTable.add(backButton);
		buttonTable.row();

		TextButton controllsButton = GuiFactory.textButton("Keybinding", () -> {
			KeybindingWindow keybinding = new KeybindingWindow(stage.getHeight());
			keybinding.setX(stage.getWidth() / 2 - keybinding.getWidth() / 2);
			stage.addActor(keybinding);
			keybinding.requestFocus();
		});

		buttonTable.add(controllsButton).padTop(controllsButton.getHeight() / 2);
		buttonTable.row();

		if (previous instanceof GameScreen) {
			TextButton saveButton = GuiFactory.textButton("Save", () -> {
				try {
					SafeManager.writeChunks(GameContext.inst().getGameStage().getTileLayer().getMap().getChunks());
				} catch (IOException e) {
					// TODO Save failed dialog
				}
			});

			buttonTable.add(saveButton).padTop(saveButton.getHeight() / 2);
			buttonTable.row();

			TextButton endGameButton = GuiFactory.textButton("End Game", () -> {
				AppContext.inst().getGame().setScreen(new TitleScreen());
				GameContext.inst().dispose();
			});

			buttonTable.add(endGameButton).padTop(endGameButton.getHeight() / 2);
		}
		table.add(buttonTable).expandY();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		Gdx.app.log("OptionsScreen", "show");
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		this.stage = null;
	}

}
