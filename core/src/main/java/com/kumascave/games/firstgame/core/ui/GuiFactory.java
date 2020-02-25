package com.kumascave.games.firstgame.core.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.kumascave.games.firstgame.FirstGame;
import com.kumascave.games.firstgame.core.ui.input.GuiDefaults;

public abstract class GuiFactory {

	public static Skin SKIN = FirstGame.gameSkin;

	public static ScrollPane scrollPane(Actor widget) {
		ScrollPane scrollpane = new ScrollPane(widget, SKIN);
		scrollpane.setVariableSizeKnobs(false);
		scrollpane.setScrollbarsVisible(true);
		scrollpane.setFadeScrollBars(false);
		scrollpane.setScrollBarTouch(true);
		scrollpane.setFlickScroll(false);
		return scrollpane;
	}

	public static TextField textField(String text) {
		TextField textField = new TextField(text, SKIN);
		textField.getStyle().background.setRightWidth(GuiDefaults.TEXTFIELD_PADDING);
		textField.getStyle().background.setLeftWidth(GuiDefaults.TEXTFIELD_PADDING);
		textField.getStyle().background.setTopHeight(GuiDefaults.TEXTFIELD_PADDING);
		textField.getStyle().background.setBottomHeight(GuiDefaults.TEXTFIELD_PADDING);
		return textField;
	}

	public static TextButton textButton(String text, Runnable runnable) {
		TextButton button = new TextButton(text, SKIN);
		button.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				runnable.run();
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		return button;
	}

	public static TextButton textButtonSmall(String text, Runnable runnable) {
		TextButton button = new TextButton(text, SKIN);
		button.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				runnable.run();
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		// todo: scale
		return button;
	}

}
