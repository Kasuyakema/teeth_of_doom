package com.kumascave.games.firstgame.core.entity;

import java.util.Arrays;

public interface WithStateMachine {

	public default boolean switchState(String target, Object... args) {
		return getStateMachine().transition(target, args);
	}

	public default void requireSwitchState(String target, Object... args) {
		getStateMachine().requireTransition(target, args);
	}

	public default String getState() {
		return getStateMachine().getState();
	}

	public default boolean isInState(String state) {
		return getState().equals(state);
	}

	public default boolean isInStates(String... states) {
		return Arrays.stream(states).anyMatch(s -> isInState(s));
	}

	public StateMachine getStateMachine();

}
