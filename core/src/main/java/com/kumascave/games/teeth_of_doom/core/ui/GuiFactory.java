package com.kumascave.games.teeth_of_doom.core.ui;

import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Scaling;
import com.kumascave.games.teeth_of_doom.FirstGame;
import com.kumascave.games.teeth_of_doom.core.ui.actors.NumberField;
import com.kumascave.games.teeth_of_doom.core.ui.actors.ScalableTextButton;
import com.kumascave.games.teeth_of_doom.util.jgoodies.VHolder;

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

	public static NumberField numberField(int val) {
		return new NumberField(val, SKIN);
	}

	public static NumberField numberField(int val, Consumer<Integer> func) {
		NumberField numberField = numberField(val);
		numberField.addListener((EventListener) event -> {
			if (event instanceof ChangeEvent) {
				try {
					func.accept(numberField.getValue());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			return false;
		});
		return numberField;
	}

	public static CheckBox checkBox(String name, boolean checked) {
		CheckBox checkbox = new CheckBox(name, FirstGame.gameSkin);
		checkbox.setChecked(checked);
		checkbox.getImage().setScaling(Scaling.fit);
		@SuppressWarnings("unchecked")
		Cell<Image> imageCell = checkbox.getImageCell();
		imageCell.prefHeight(checkbox.getLabel().getPrefHeight()).minHeight(0f);
		imageCell.prefWidth(checkbox.getLabel().getPrefHeight() + GuiDefaults.CHECKBOX_PADDING).minWidth(0f);

		return checkbox;
	}

	public static CheckBox checkBox(String name, boolean checked, Consumer<Boolean> func) {
		CheckBox checkbox = checkBox(name, checked);
		checkbox.addListener((EventListener) event -> {
			if (event instanceof ChangeEvent) {
				try {
					func.accept(checkbox.isChecked());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			return false;
		});
		return checkbox;
	}

	public static CheckBox checkBox(String name, VHolder<Boolean> vHolder) {
		CheckBox checkbox = checkBox(name, vHolder.getValue());

		checkbox.addListener((EventListener) event -> {
			if (event instanceof ChangeEvent) {
				try {
					vHolder.setValue(checkbox.isChecked());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			return false;
		});
		vHolder.addPropertyChangeListener(evt -> checkbox.setChecked((Boolean) evt.getNewValue()));

		return checkbox;
	}

	public static ScalableTextButton textButton(String text, Runnable runnable) {
		ScalableTextButton button = new ScalableTextButton(text, SKIN);
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

	public static ScalableTextButton textButtonSmall(String text, Runnable runnable) {
		ScalableTextButton button = new ScalableTextButton(text, SKIN, "small");

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
		// todo: scale. Check out checkbox
		return button;
	}

	public static ScalableTextButton clearTextButton(String text, Runnable runnable) {
		ScalableTextButton button = new ScalableTextButton(text, SKIN, "clear");
		button.setPrefHeigth(button.getLabel().getPrefHeight() + GuiDefaults.TEXTBUTTON_PADDING * 2f);
		button.setPrefWidth(button.getLabel().getPrefWidth() + GuiDefaults.TEXTBUTTON_PADDING * 2f);
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

}
