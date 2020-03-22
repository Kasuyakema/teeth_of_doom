package com.kumascave.games.teeth_of_doom.core.physics;

import com.badlogic.gdx.math.Vector2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Pose {

	public Pose(float x, float y, float angle) {
		this(new Vector2(x, y), angle);
	}

	@Setter
	@Getter
	private Vector2 pos;
	@Setter
	@Getter
	private float angle;

	public float getX() {
		return pos.x;
	}

	public float getY() {
		return pos.y;
	}

}
