package com.kumascave.games.teeth_of_doom.core.ui.actors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kumascave.games.teeth_of_doom.core.Constants;

public class FPSCounter extends Label implements HudElement {
	private static final String FPS_PREFIX = "FPS:";

	private static final float upperThreshold = 1.2f / Constants.TARGET_FPS;
	// private static final float lowerThreshold = 0.8f / Constants.TARGET_FPS;

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
		List<Float> dts = deltas.stream().filter(x -> (x > upperThreshold)).collect(Collectors.toList());
		if (!dts.isEmpty()) {
			Gdx.app.debug(FPSCounter.class.getSimpleName(), dts.toString());
		}
		return Integer.toString(deltas.size());
	}

	@Override
	public void updateGeometry() {
		this.setPosition(0, getParent().getHeight() - getHeight());
	}
}
