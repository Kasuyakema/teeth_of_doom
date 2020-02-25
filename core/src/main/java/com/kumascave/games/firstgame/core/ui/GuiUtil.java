package com.kumascave.games.firstgame.core.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kumascave.games.firstgame.core.ui.input.GuiDefaults;

public abstract class GuiUtil {

	// public static Cell<Actor> add(Actor actor, Table table) {
	// return table.add(actor).padTop(GuiDefaults.SMALL_GAP);
	// }

	public static void row(Table table, float gap) {
		table.row();
		table.add().minHeight(gap);
		table.row();
	}

	public static void row(Table table) {
		row(table, GuiDefaults.SMALL_GAP);
	}

}
