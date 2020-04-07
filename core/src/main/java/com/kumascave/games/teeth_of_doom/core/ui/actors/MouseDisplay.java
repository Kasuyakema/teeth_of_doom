package com.kumascave.games.teeth_of_doom.core.ui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kumascave.games.teeth_of_doom.core.GameContext;

public class MouseDisplay extends Label implements HudElement {

	public MouseDisplay() {
		super("", new LabelStyle(new BitmapFont(), Color.GREEN));
		this.setWrap(false);
		this.setText("Mouse: (000.00,000.00)");
		this.setWidth(getPrefWidth());
		this.setHeight(getPrefHeight());
	}

	@Override
	public void act(float delta) {
		this.setText("Mouse: " + GameContext.getMousePosition());
		super.act(delta);
	}

	@Override
	public void updateGeometry() {
		this.setPosition(0, getParent().getHeight() - getHeight() * 3.4f);
	}
}
