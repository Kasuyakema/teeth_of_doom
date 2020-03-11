package com.kumascave.games.teeth_of_doom.core;

import com.badlogic.gdx.utils.Disposable;

public class DisposeMeAction extends DelayedAction {

	public DisposeMeAction(float delay, Disposable target) {
		super(delay, () -> target.dispose());
	}

	public static void disposeMe(Disposable target) {
		GameContext.getGameStage().addAction(new DisposeMeAction(Constants.DISPOSE_TIME, target));
	}

}
