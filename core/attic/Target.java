package com.kumascave.games.teeth_of_doom.core.mechanics;

import com.badlogic.gdx.math.Vector2;
import com.kumascave.games.teeth_of_doom.core.physics.PhysicalEntity;

import lombok.Data;

@Data
public class Target {

	Vector2 position;

	PhysicalEntity entity;

	public void setPosition(Vector2 position) {
		this.position = position.cpy();
	}

	public void setEntity(PhysicalEntity entity) {
		this.entity = entity;
		if (entity == null) {
			this.position = null;
		} else {
			this.position = entity.getPose().getPos();
		}
	}

	public TargetType getType() {
		if (entity != null) {
			return TargetType.ENTITY;
		}
		if (position != null) {
			return TargetType.POSITION;
		}
		return TargetType.EMPTY;
	}

	public void clear() {
		setEntity(null);
	}

	public boolean isEmpty() {
		return getType() == TargetType.EMPTY;
	}

	public enum TargetType {
		POSITION, ENTITY, EMPTY
	}
}
