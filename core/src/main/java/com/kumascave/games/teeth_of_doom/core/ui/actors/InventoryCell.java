package com.kumascave.games.teeth_of_doom.core.ui.actors;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kumascave.games.teeth_of_doom.core.entity.item.Item;
import com.kumascave.games.teeth_of_doom.core.entity.item.ItemState;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.antropomorph.Human;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.antropomorph.Inventory;
import com.kumascave.games.teeth_of_doom.core.ui.GuiFactory;
import com.kumascave.games.teeth_of_doom.core.ui.actors.contextMenu.InventoryItemContextMenu;

public class InventoryCell extends Table {

	Item item;
	Inventory inventory;

	public InventoryCell(Item item, Inventory inventory) {
		super(GuiFactory.SKIN);
		this.item = item;
		this.inventory = inventory;

		setBackground("inventory_cell");
		add(item).grow();
		addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (button == Buttons.RIGHT) {

					new InventoryItemContextMenu(InventoryCell.this,
							InventoryCell.this.localToStageCoordinates(new Vector2(x, y)), event);
					return true;
				}
				return super.touchDown(event, x, y, pointer, button);
			}
		});
	}

	public void drop() {
		item.remove();
		inventory.removeItem(item);
		item.requireSwitchState(ItemState.FREE);
		Human owner = inventory.getOwner();
		float dist = Math.max(owner.getWidth(), owner.getHeight()) / 2f
				+ Math.max(item.getActorSize().x, item.getActorSize().y) / 2f + 0.05f;
		Vector2 rand = new Vector2(MathUtils.random(-1.0f, 1.0f), MathUtils.random(-1.0f, 1.0f)).setLength(dist);
		Vector2 pos = owner.getBody().getPosition().cpy().add(rand);
		item.setPositionFull(pos.x, pos.y, MathUtils.random((float) -Math.PI, (float) Math.PI));
		item.addToWorld();
	}

}
