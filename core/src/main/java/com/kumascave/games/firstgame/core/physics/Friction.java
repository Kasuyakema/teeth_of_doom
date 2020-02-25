package com.kumascave.games.firstgame.core.physics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Friction {

	public Friction(float friction, float dampening) {
		this.linearDampening = dampening;
		this.angularDampening = dampening;

		this.edgeFriction = friction;

		this.linearFriction = friction;
		this.angularFriction = friction;
	}

	// friction between body and ground
	private float linearFriction;
	private float angularFriction;

	// can be thought of as "air resistance"
	private float linearDampening;
	private float angularDampening;

	// friction between two bodys rubbing
	private float edgeFriction;
}
