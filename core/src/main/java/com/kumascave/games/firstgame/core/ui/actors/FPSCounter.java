package com.kumascave.games.firstgame.core.ui.actors;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class FPSCounter extends Label implements HudElement {
	private static final String FPS_PREFIX = "FPS:";

	float timeSinceUpdate = 0;
	List<Float> deltas = new ArrayList<>();

	public FPSCounter() {
		super("", new LabelStyle(new BitmapFont(), Color.GREEN));
		this.setWrap(false);
		this.setText(FPS_PREFIX + "000");
		this.setWidth(getPrefWidth());
		this.setHeight(getPrefHeight());
	}

	@Override
	public void act(float delta) {
		timeSinceUpdate += delta;
		deltas.add(delta);

		if (timeSinceUpdate > 1.0f) {
			this.setText(FPS_PREFIX + calculateFramerate());
			timeSinceUpdate = 0.0f;
			deltas.clear();
		}
		super.act(delta);
	}

	private String calculateFramerate() {
		return Integer.toString(deltas.size());
	}

	@Override
	public void updateGeometry() {
		this.setPosition(0, getParent().getHeight() - getHeight());
	}
}
