package com.kumascave.games.firstgame.core.physics.shapes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Rectangle extends PolygonShape {

	public Rectangle(float width, float height) {
		super();
		this.setAsBox(width / 2, height / 2);
	}

	public Rectangle(float square) {
		super();
		this.setAsBox(square / 2, square / 2);
	}

	public static float densityFromWeight(float weight, float width, float height) {
		return weight / (width * height);
	}

	public static float densityFromWeight(float weight, Vector2 size) {
		return densityFromWeight(weight, size.x, size.y);
	}
}
