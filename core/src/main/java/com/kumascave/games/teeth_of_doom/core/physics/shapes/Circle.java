package com.kumascave.games.teeth_of_doom.core.physics.shapes;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.utils.Disposable;
import com.kumascave.games.teeth_of_doom.core.DisposeMeAction;

public class Circle extends CircleShape implements Disposable {

	public Circle() {
		super();
		DisposeMeAction.disposeMe(this);
	}

	public Circle(float diam) {
		this();
		this.setRadius(diam / 2);
	}

	public static float densityFromWeight(float weight, float diam) {
		return weight / ((float) Math.PI * (diam / 2.0f) * (diam / 2.0f));
	}

}
