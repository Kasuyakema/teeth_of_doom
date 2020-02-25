package com.kumascave.games.firstgame.core.physics.shapes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class DShape extends PolygonShape {

	public DShape(float width, float height) {
		super();
		float hw = width / 2.0f;
		float hh = height / 2.0f;
		Vector2[] verticles = new Vector2[4];
		verticles[0] = v(-hw, hh);
		verticles[1] = v(hw, hh / 2.0f);
		verticles[2] = v(hw, -hh / 2.0f);
		verticles[3] = v(-hw, -hh);

		this.set(verticles);
	}

	private Vector2 v(float x, float y) {
		return new Vector2(x, y);
	}

	public static float densityFromWeight(float weight, float width, float height) {
		return weight / (height * width * 0.75f);
	}

	public static float densityFromWeight(float weight, Vector2 size) {
		return densityFromWeight(weight, size.x, size.y);
	}
}
