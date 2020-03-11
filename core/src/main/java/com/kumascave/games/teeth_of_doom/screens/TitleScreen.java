package com.kumascave.games.teeth_of_doom.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kumascave.games.teeth_of_doom.AppContext;
import com.kumascave.games.teeth_of_doom.core.ui.GuiFactory;
import com.kumascave.games.teeth_of_doom.core.ui.actors.ScalableTextButton;
import com.kumascave.games.teeth_of_doom.core.ui.actors.TitledTable;

public class TitleScreen implements Screen {

	private Stage stage;

	public TitleScreen() {
		super();
		stage = new Stage(new ScreenViewport());

		TitledTable table = new TitledTable("Title Screen");

		stage.addActor(table);

		Table buttonTable = new Table();

		ScalableTextButton playButton = GuiFactory.textButton("Play!",
				() -> AppContext.inst().getGame().setScreen(new GameScreen()));

		buttonTable.add(playButton);
		buttonTable.row();

		TextButton optionsButton = GuiFactory.textButton("Options",
				() -> AppContext.inst().getGame().setScreen(new OptionScreen(TitleScreen.this)));

		buttonTable.add(optionsButton).padTop(optionsButton.getHeight() / 2);
		buttonTable.row();

		TextButton quitButton = GuiFactory.textButton("Quit", () -> Gdx.app.exit());

		buttonTable.add(quitButton).padTop(optionsButton.getHeight() / 2);

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
		// TODO Auto-generated method stub

	}

}
