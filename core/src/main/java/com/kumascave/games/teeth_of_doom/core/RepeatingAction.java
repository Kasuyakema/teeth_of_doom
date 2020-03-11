package com.kumascave.games.teeth_of_doom.core;

import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;

import lombok.Setter;

public class RepeatingAction extends RunnableAction {

	@Setter
	float delay;
	float timePassed = 0.0f;
	@Setter
	private boolean destroy = false;
	@Setter
	private boolean awake = true;

	@Override
	public boolean act(float delta) {
		if (awake) {
			timePassed = timePassed + delta;
			if (delay < timePassed) {
				super.act(delta);
				restart();
			}
		}
		return destroy;
	}

	public RepeatingAction(float delay, Runnable runnable) {
		super();
		this.delay = delay;
		setRunnable(runnable);
	}

	@Override
	public void restart() {
		super.restart();
		timePassed = 0.0f;
	}

}
