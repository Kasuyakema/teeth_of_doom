package com.kumascave.games.teeth_of_doom.core.physics.shapes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Disposable;
import com.kumascave.games.teeth_of_doom.core.DisposeMeAction;

public class Rectangle extends PolygonShape implements Disposable {

	private Rectangle() {
		DisposeMeAction.disposeMe(this);
	}

	public Rectangle(float width, float height) {
		this();
		this.setAsBox(width / 2, height / 2);
	}

	public Rectangle(float width, float height, Vector2 center, float angle) {
		this();
		this.setAsBox(width / 2, height / 2, center, angle);
	}

	public Rectangle(float square) {
		this();
		this.setAsBox(square / 2, square / 2);
	}

	public static float densityFromWeight(float weight, float width, float height) {
		return weight / (width * height);
	}

	public static float densityFromWeight(float weight, Vector2 size) {
		return densityFromWeight(weight, size.x, size.y);
	}
}
