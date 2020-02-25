package com.kumascave.games.firstgame.core.ui.actors;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.kumascave.games.firstgame.ConfigKey;
import com.kumascave.games.firstgame.FirstGame;
import com.kumascave.games.firstgame.core.ui.GuiFactory;
import com.kumascave.games.firstgame.core.ui.GuiUtil;
import com.kumascave.games.firstgame.core.ui.input.Keybindings;

public class KeybindingWindow extends TitledWindow {

	private List<Entry<Integer, ConfigKey>> bindings;

	public KeybindingWindow(float height) {
		super("Keybindings", height);

		setModal(true);

		bindings = Keybindings.instance().getBindings().entrySet().stream()
				.sorted((x, y) -> x.getValue().compareTo(y.getValue())).collect(Collectors.toList());

		getContentTable().setSkin(FirstGame.gameSkin);

		// getContentTable().debugCell();

		for (Entry<Integer, ConfigKey> e : bindings) {
			TextField textField = GuiFactory.textField(e.getKey().toString());
			textField.setText(Keys.toString(e.getKey()));
			setSkin(FirstGame.gameSkin);
			contentTable.add(e.getValue().name(), "small").left().expandX();
			contentTable.add(textField).right().growX().minHeight(0f).prefHeight(0f).growY();
			// contentTable.add(new KeybindWidget(e.getValue(), e.getKey())).growX();
			GuiUtil.row(contentTable);
		}

	}

}
