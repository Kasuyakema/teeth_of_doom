package com.kumascave.games.teeth_of_doom.core.ui.actors.windows;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListDataListener;

import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.antropomorph.Inventory;
import com.kumascave.games.teeth_of_doom.core.ui.GuiDefaults;
import com.kumascave.games.teeth_of_doom.core.ui.actors.InventoryCell;

public class InventoryWindow extends TitledWindow {

	private static InventoryWindow instance;
	private static final int itemsPerRow = 10;
	Inventory inventory;
	ListDataListener listener;

	List<InventoryCell> cells = new ArrayList<>();

	boolean update = false;

	public InventoryWindow() {
		super("Inventory", GameContext.getGameStage().getHeight());
		setMovable(true);
		contentTable.top().left();
		GameContext.inst();
		inventory = GameContext.getPlayer().getInventory();
		listener = inventory.addListDataListener(evt -> update());
		update();
		GameContext.inst();
		GameContext.getHudController().getHudRootActor().addActor(this);
	}

	@Override
	public void act(float delta) {
		if (update) {
			_update();
			update = false;
		}
		super.act(delta);
	}

	private void update() {
		update = true;
	}

	private void _update() {
		contentTable.clear();
		cells.clear();
		float prefSize = (getWidth() - GuiDefaults.WINDOW_BORDERS * 2 + scrollpane.getScrollBarWidth()) / itemsPerRow;
		for (int i = 0; i < inventory.getList().size(); i++) {
			if (i > 1 && i % 11 == 0) {
				contentTable.row();
			}
			contentTable.add(new InventoryCell(inventory.getList().get(i), inventory)).prefHeight(prefSize)
					.prefWidth(prefSize);
		}
	}

	public static void toggle() {
		if (instance == null) {
			instance = new InventoryWindow();
		} else {
			instance.dispose();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		instance = null;
		inventory.removeListDataListener(listener);
	}

}
