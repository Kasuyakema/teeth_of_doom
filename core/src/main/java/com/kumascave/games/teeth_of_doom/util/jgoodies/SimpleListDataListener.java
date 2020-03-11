package com.kumascave.games.teeth_of_doom.util.jgoodies;

import java.util.function.Consumer;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public interface SimpleListDataListener extends ListDataListener, Consumer<ListDataEvent> {

	@Override
	public default void intervalAdded(ListDataEvent e) {
		action(e);
	}

	@Override
	public default void intervalRemoved(ListDataEvent e) {
		action(e);
	}

	@Override
	public default void contentsChanged(ListDataEvent e) {
		action(e);
	}

	public void action(ListDataEvent e);

}
