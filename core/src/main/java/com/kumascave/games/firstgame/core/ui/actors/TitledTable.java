package com.kumascave.games.firstgame.core.ui.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kumascave.games.firstgame.FirstGame;

public class TitledTable extends Table {
	Title titleActor;

	public TitledTable(String title) {
		setFillParent(true);
		setSkin(FirstGame.gameSkin);
		setBackground("screen");
		titleActor = new Title(title);
		addActor(titleActor);
		top();
	}

	@Override
	protected void sizeChanged() {
		super.sizeChanged();
		titleActor.setPosition(this.getWidth() / 2 - titleActor.getWidth() / 2,
				this.getHeight() - titleActor.getHeight());
	}

	@Override
	protected void positionChanged() {
		super.positionChanged();
		titleActor.setPosition(this.getWidth() / 2 - titleActor.getWidth() / 2,
				this.getHeight() - titleActor.getHeight());
	}
}
