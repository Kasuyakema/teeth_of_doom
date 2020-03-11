package com.kumascave.games.teeth_of_doom.core.ui.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import lombok.Setter;

public class ScalableTextButton extends TextButton {

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

	public ScalableTextButton(String text, Skin skin, String styleName) {
		super(text, skin, styleName);
	}

	public ScalableTextButton(String text, Skin skin) {
		super(text, skin);
	}

	public ScalableTextButton(String text, TextButtonStyle style) {
		super(text, style);
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
