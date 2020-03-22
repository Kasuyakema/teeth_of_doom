package com.kumascave.games.teeth_of_doom.core.ui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.mechanics.HuntDirector;
import com.kumascave.games.teeth_of_doom.util.DynamicVariables;

public class HuntDisplay extends Label implements HudElement {

	private float timer;

	public HuntDisplay() {
		super("", new LabelStyle(new BitmapFont(), Color.GREEN));
		this.setWrap(false);
		this.setText("CALM 000.00");
		this.setWidth(getPrefWidth());
		this.setHeight(getPrefHeight());
		GameContext.getHuntOngoing().addPropertyChangeListener(evt -> timer = 0f);
	}

	@Override
	public void act(float delta) {
		timer += delta;
		String text = "Calm " + (DynamicVariables.calmTime - timer);
		if (GameContext.getHuntOngoing().getValue()) {
			text = "Hunt " + HuntDirector.getHuntCounter();
		}
		this.setText(text);
		super.act(delta);
	}

	@Override
	public void updateGeometry() {
		this.setPosition(0, getParent().getHeight() - getHeight() * 2.2f);
	}
}
