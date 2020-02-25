package com.kumascave.games.firstgame.core.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kumascave.games.firstgame.core.world.Duo;

import lombok.Data;

@Data
public abstract class StateMachine<S> {

	private Map<Duo<String, String>, Transition> transitions = new HashMap<>();
	protected S subject;

	String state;

	public boolean transition(String target, Object... args) {
		Transition transition = transitions.get(new Duo<>(state, target));
		if (transition != null) {
			boolean result = transition.apply(args);
			if (result) {
				// Gdx.app.debug(subject.getClass().toString(),
				// "State change " + subject.getClass().getCanonicalName() + " " + state + "->"
				// + target);
				state = transition.getResultingState();
			}
			return result;
		}
		if (state.equals(target)) {
			// All States are self referential for now
			return true;
		}
		// no such transition
		return false;
	}

	public void requireTransition(String target, Object... args) {
		if (!transition(target, args)) {
			throw new IllegalStateException("Required transition failed:" + subject.getClass().getCanonicalName() + " "
					+ state + "->" + target);
		}
	}

	public void addTransitions(List<Transition> transitions) {
		transitions.forEach(x -> addTransition(x));
	}

	public void addTransition(Transition transitions) {
		this.transitions.put(transitions.getDuo(), transitions);
	}

	public void removeTransition(String a, String b) {
		this.transitions.remove(new Duo<>(a, b));
	}
}
