package com.kumascave.games.teeth_of_doom.core.entity.mobs.antropomorph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.jgoodies.binding.list.IndirectListModel;
import com.kumascave.games.teeth_of_doom.core.entity.item.Item;
import com.kumascave.games.teeth_of_doom.core.mechanics.crafting.Recipe;

import lombok.Getter;

public class Inventory extends IndirectListModel<Item> {
	private static final long serialVersionUID = -2999387125084285101L;

	@Getter
	private Human owner;

	public Inventory(Human owner) {
		super();
		this.owner = owner;
	}

	public void addItem(Item item) {
		getList().add(item);
		int lastIndex = getList().size() - 1;
		fireContentsChanged(lastIndex, lastIndex);

		// if (CraftingTree.canCraft(PegLeg.class, this)) {
		// Entity leg = CraftingTree.craft(PegLeg.class, this);
		// leg.getBodyDef().position.set(new Vector2(5, 5));
		// RunnableAction action = new RunnableAction();
		// action.setRunnable(() -> leg.addToWorld());
		// GameContext.inst().getGameStage().addAction(action);
		// }
	}

	public void consumeItems(Recipe recipe) {
		List<Item> toRemove = new ArrayList<>();
		for (Entry<Class<? extends Item>, Integer> e : recipe.entrySet()) {
			getList().stream().filter(x -> x.getClass().equals(e.getKey())).limit(e.getValue())
					.forEach(x -> toRemove.add(x));
		}
		getList().removeAll(toRemove);
		toRemove.forEach(x -> x.dispose());
		fireContentsChanged(0, getSize() - 1);
	}

	public ListDataListener addListDataListener(Consumer<ListDataEvent> consumer) {
		ListDataListener listener = new ListDataListener() {

			@Override
			public void intervalRemoved(ListDataEvent e) {
				consumer.accept(e);
			}

			@Override
			public void intervalAdded(ListDataEvent e) {
				consumer.accept(e);
			}

			@Override
			public void contentsChanged(ListDataEvent e) {
				consumer.accept(e);
			}
		};

		super.addListDataListener(listener);
		return listener;
	}

	public void removeItem(Item item) {
		int index = getList().indexOf(item);
		getList().remove(index);
		fireContentsChanged(index, index);
	}

}
