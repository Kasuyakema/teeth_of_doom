package com.kumascave.games.teeth_of_doom.core.ai.behavior;

public interface Attacking {

	public void setAttackCooldown(float cooldown);

	public default void updateAttackCooldown(float deltaT) {
		if (attackOnCooldown()) {
			setAttackCooldown(Math.max(getAttackCooldown() - deltaT, 0f));
		}
	}

	public float getAttackCooldown();

	public float getAttackCooldownBase();

	public float getAttackRange();

	public default boolean attackOnCooldown() {
		return getAttackCooldown() > 0f;
	}

	public default void attack() {
		setAttackCooldown(getAttackCooldownBase());
	}

}
