package com.kumascave.games.teeth_of_doom.core.ui.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.kumascave.games.teeth_of_doom.ConfigKey;
import com.kumascave.games.teeth_of_doom.FirstGame;

public class KeybindWidget extends Table {

	ConfigKey key;
	Integer value;

	TextField textField;

	public KeybindWidget(ConfigKey key, Integer value) {
		super();
		this.key = key;
		this.value = value;
		debugCell();
		textField = new TextField("" + value, FirstGame.gameSkin);

		setSkin(FirstGame.gameSkin);
		this.add(key.name(), "small").left().expandX();
		this.add(textField).right().growX().minHeight(0f).prefHeight(0f).growY();

	}

}
