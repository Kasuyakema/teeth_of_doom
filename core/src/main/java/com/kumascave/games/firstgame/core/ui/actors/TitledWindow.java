package com.kumascave.games.firstgame.core.ui.actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.kumascave.games.firstgame.FirstGame;
import com.kumascave.games.firstgame.core.ui.GuiFactory;
import com.kumascave.games.firstgame.core.ui.input.GuiDefaults;

import lombok.Getter;

public class TitledWindow extends Window implements Disposable {

	Title titleActor;
	CloseButton closeButton;
	@Getter
	Table buttonBarTable;
	@Getter
	Table contentTable;

	ScrollPane scrollpane;

	public TitledWindow(String title, float height) {
		super("", FirstGame.gameSkin);

		Cell<Label> titleCell = getTitleTable().getCell(getTitleLabel());
		getTitleTable().right();
		titleActor = new Title(title);
		titleCell.clearActor().expandX().uniform();
		getTitleTable().add(titleActor);
		getTitleTable().add().expandX().uniform();

		contentTable = new Table();
		scrollpane = GuiFactory.scrollPane(contentTable);
		scrollpane.setScrollingDisabled(true, false);
		buttonBarTable = new Table();
		buttonBarTable.right();
		closeButton = new CloseButton();
		float scale = 0.5f;
		Cell<CloseButton> close = buttonBarTable.add(closeButton).align(Align.bottomRight);
		close.prefHeight(close.getPrefHeight() * scale).prefWidth(close.getPrefWidth() * scale);
		close.minWidth(0f).minHeight(0f);

		add(scrollpane).grow();
		row();
		add(buttonBarTable).growX();

		setResizable(false);
		setMovable(true);

		pad(GuiDefaults.WINDOW_BORDERS);
		padTop(titleActor.getHeight());
		// getBackground().setTopHeight(titleActor.getHeight());
		// getBackground().setBottomHeight(GuiDefaults.WINDOW_BORDERS);
		// getBackground().setLeftWidth(GuiDefaults.WINDOW_BORDERS);
		// getBackground().setRightWidth(GuiDefaults.WINDOW_BORDERS);
		setResizeBorder((int) GuiDefaults.WINDOW_BORDERS * 2);

		float ratio = getBackground().getMinWidth() / getBackground().getMinHeight();
		this.setSize(height * ratio, height);
	}

	public void requestFocus() {
		getStage().setKeyboardFocus(contentTable);
		getStage().setScrollFocus(contentTable);
	}

	// @Override
	// protected void sizeChanged() {
	// super.sizeChanged();
	// if (titleActor != null) {
	// titleActor.setPosition(this.getWidth() / 2 - titleActor.getWidth(),
	// this.getHeight() - titleActor.getHeight());
	// }
	// if (closeButton != null) {
	// closeButton.setPosition(this.getWidth() - closeButton.getWidth() + 10f,
	// this.getHeight() - closeButton.getHeight());
	// }
	// }
	//
	// @Override
	// protected void positionChanged() {
	// super.positionChanged();
	// if (titleActor != null) {
	// titleActor.setPosition(this.getWidth() / 2 - titleActor.getWidth(),
	// this.getHeight() - titleActor.getHeight());
	// }
	// if (closeButton != null) {
	// closeButton.setPosition(this.getWidth() - closeButton.getWidth() + 10f,
	// this.getHeight() - closeButton.getHeight());
	// }
	// }

	@Override
	public void dispose() {
		remove();
		closeButton.clear();
		closeButton = null;
		titleActor = null;
		this.clear();
	}

	private class CloseButton extends TextButton {
		public CloseButton() {
			super("close", FirstGame.gameSkin);
			this.addListener(new InputListener() {
				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					TitledWindow.this.dispose();
				}

				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					return true;
				}
			});
		}

		// @Override
		// public float getPrefHeight() {
		// return super.getPrefHeight() / 2;
		// }
		//
		// @Override
		// public float getPrefWidth() {
		// return super.getPrefWidth() / 2;
		// }

	}
}
