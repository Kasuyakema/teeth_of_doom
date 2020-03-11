package com.kumascave.games.teeth_of_doom.core.entity;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.kumascave.games.teeth_of_doom.core.world.Duo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transition {

	public Transition(String a, String b, Supplier<Boolean> condition, Consumer<Object[]> transition) {
		super();
		this.a = a;
		this.b = b;
		this.condition = condition;
		this.transition = transition;
	}

	public Transition(String a, String b, Consumer<Object[]> transition, Transition follow) {
		super();
		this.a = a;
		this.b = b;
		this.transition = transition;
		this.follow = follow;
	}

	public Transition(String a, String b, Consumer<Object[]> transition) {
		super();
		this.a = a;
		this.b = b;
		this.transition = transition;
	}

	public Transition(String a, String b, Supplier<Boolean> condition) {
		super();
		this.a = a;
		this.b = b;
		this.condition = condition;
	}

	public Transition(String a, String b) {
		super();
		this.a = a;
		this.b = b;
	}

	private String a;
	private String b;

	Supplier<Boolean> condition = () -> true;

	Consumer<Object[]> transition = (args) -> {
		return;
	};

	Transition follow;

	public Duo<String, String> getDuo() {
		return new Duo<String, String>(a, b);
	}

	public boolean apply(Object... args) {
		if (condition.get()) {
			transition.accept(args);
			if (getFollow() != null) {
				return getFollow().apply(args);
			}
			return true;
		}
		return false;
	}

	public String getResultingState() {
		if (getFollow() != null) {
			return getFollow().getResultingState();
		}
		return b;
	}

}
