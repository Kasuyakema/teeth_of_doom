package com.kumascave.games.firstgame.core.ui.actors;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kumascave.games.firstgame.FirstGame;

public class Title extends Group {

	public Title(String title) {
		Label label = new Label(title, FirstGame.gameSkin);
		Image background = new Image(FirstGame.gameSkin, "title");
		addActor(background);
		addActor(label);
		this.setSize(background.getWidth(), background.getHeight());
		label.setPosition(background.getWidth() / 2 - label.getPrefWidth() / 2f,
				background.getHeight() - label.getStyle().font.getLineHeight());
		// background.setPosition(-background.getPrefWidth() / 2f,
		// -label.getStyle().font.getLineHeight() * 1.5f);
	}

}
