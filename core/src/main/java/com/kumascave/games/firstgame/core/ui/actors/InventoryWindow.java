package com.kumascave.games.firstgame.core.ui.actors;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListDataListener;

import com.kumascave.games.firstgame.core.GameContext;
import com.kumascave.games.firstgame.core.entity.mobs.antropomorph.Inventory;
import com.kumascave.games.firstgame.core.ui.input.GuiDefaults;

public class InventoryWindow extends TitledWindow {

	private static InventoryWindow instance;
	private static final int itemsPerRow = 10;
	Inventory inventory;
	ListDataListener listener;

	List<InventoryCell> cells = new ArrayList<>();

	boolean update = false;

	public InventoryWindow() {
		super("Inventory", GameContext.inst().getGameStage().getHeight());
		setMovable(true);
		contentTable.top().left();
		inventory = GameContext.inst().getPlayer().getInventory();
		listener = inventory.addListDataListener(evt -> update());
		update();
		GameContext.inst().getHudController().getHudRootActor().addActor(this);
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
		// Util.callXTimes(10, () -> contentTable.add(new
		// InventoryCell()).prefHeight(prefSize).prefWidth(prefSize));
		// contentTable.row();
		// Util.callXTimes(3, () -> contentTable.add(new
		// InventoryCell()).prefHeight(prefSize).prefWidth(prefSize));
		inventory.getList()
				.forEach(x -> contentTable.add(new InventoryCell(x)).prefHeight(prefSize).prefWidth(prefSize));
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
