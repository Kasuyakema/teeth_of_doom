package com.kumascave.games.teeth_of_doom.core.ui.actors.contextMenu;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.kumascave.games.teeth_of_doom.core.ui.GuiFactory;
import com.kumascave.games.teeth_of_doom.core.ui.actors.InventoryCell;

public class InventoryItemContextMenu extends ContextMenu {
	public InventoryItemContextMenu(InventoryCell parent, Vector2 pos, InputEvent event) {
		super(parent, pos, event);
		TextButton drop = GuiFactory.clearTextButton("Drop", () -> {
			parent.drop();
			dispose();
		});
		add(drop);
		row();
	}

}