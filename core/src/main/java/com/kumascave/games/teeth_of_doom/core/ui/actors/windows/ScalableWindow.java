package com.kumascave.games.teeth_of_doom.core.ui.actors.windows;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import lombok.Setter;

public class ScalableWindow extends Window {

	@Setter
	private Float minHeigth = null;
	@Setter
	private Float minWidth = null;
	@Setter
	private Float maxHeigth = null;
	@Setter
	private Float maxWidth = null;
	@Setter
	private Float prefHeigth = null;
	@Setter
	private Float prefWidth = null;

	public ScalableWindow(String title, Skin skin, String styleName) {
		super(title, skin, styleName);
	}

	public ScalableWindow(String title, Skin skin) {
		super(title, skin);
	}

	public ScalableWindow(String title, WindowStyle style) {
		super(title, style);
	}

	@Override
	public float getPrefHeight() {
		if (prefHeigth == null) {
			return super.getPrefHeight();
		}
		return prefHeigth;
	}

	@Override
	public float getPrefWidth() {
		if (prefWidth == null) {
			return super.getPrefWidth();
		}
		return prefWidth;
	}

	@Override
	public float getMinHeight() {
		if (minHeigth == null) {
			return super.getMinHeight();
		}
		return minHeigth;
	}

	@Override
	public float getMinWidth() {
		if (minWidth == null) {
			return super.getMinWidth();
		}
		return minWidth;
	}

	@Override
	public float getMaxHeight() {
		if (maxHeigth == null) {
			return super.getMaxHeight();
		}
		return maxHeigth;
	}

	@Override
	public float getMaxWidth() {
		if (maxWidth == null) {
			return super.getMaxWidth();
		}
		return maxWidth;
	}
}
