package com.kumascave.games.firstgame.core.ui.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kumascave.games.firstgame.core.entity.item.Item;
import com.kumascave.games.firstgame.core.ui.GuiFactory;

public class InventoryCell extends Table {

	public InventoryCell(Item item) {
		super(GuiFactory.SKIN);
		setBackground("inventory_cell");
		add(item).grow();

	}

}
