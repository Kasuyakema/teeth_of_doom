package com.kumascave.games.firstgame.core.physics;

import com.badlogic.gdx.math.Vector2;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Pose {

	public Pose(float x, float y, float angle) {
		this(new Vector2(x, y), angle);
	}

	@Getter
	private Vector2 pos;
	@Getter
	private float angle;

	public float getX() {
		return pos.x;
	}

	public float getY() {
		return pos.y;
	}
}
