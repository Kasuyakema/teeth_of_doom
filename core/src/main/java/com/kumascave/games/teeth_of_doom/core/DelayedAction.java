package com.kumascave.games.teeth_of_doom.core;

import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;

import lombok.Setter;

public class DelayedAction extends RunnableAction {

	@Setter
	float delay;
	float timePassed = 0.0f;

	@Override
	public boolean act(float delta) {
		timePassed = timePassed + delta;
		if (delay < timePassed) {
			return super.act(delta);
		}
		return false;
	}

	public DelayedAction(float delay, Runnable runnable) {
		super();
		this.delay = delay;
		setRunnable(runnable);
	}

}
