package com.kumascave.games.firstgame.core.physics.shapes;

import com.badlogic.gdx.physics.box2d.CircleShape;

public class Circle extends CircleShape {

	public Circle() {
		super();
	}

	public Circle(float diam) {
		super();
		this.setRadius(diam / 2);
	}

	public static float densityFromWeight(float weight, float diam) {
		return weight / ((float) Math.PI * (diam / 2.0f) * (diam / 2.0f));
	}

}
