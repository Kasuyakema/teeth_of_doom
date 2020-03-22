package com.kumascave.games.teeth_of_doom.core.ui.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class NumberField extends TextField {

	public NumberField(int val, Skin skin, String styleName) {
		super("" + val, skin, styleName);
		setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
	}

	public NumberField(int val, Skin skin) {
		super("" + val, skin);
		setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
	}

	public NumberField(int val, TextFieldStyle style) {
		super("" + val, style);
		setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
	}

	public int getValue() {
		if (org.apache.commons.lang3.StringUtils.isBlank(getText())) {
			return 0;
		}
		return Integer.parseInt(getText());
	}
}
