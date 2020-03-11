package com.kumascave.games.teeth_of_doom.core.ui.actors.windows;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.kumascave.games.teeth_of_doom.FirstGame;
import com.kumascave.games.teeth_of_doom.core.ui.GuiFactory;
import com.kumascave.games.teeth_of_doom.core.ui.GuiUtil;
import com.kumascave.games.teeth_of_doom.util.DynamicVariables;
import com.kumascave.games.teeth_of_doom.util.jgoodies.VHolder;

public class DynamicVariableWindow extends TitledWindow {

	private List<Field> fields;

	public DynamicVariableWindow(float height) {
		super("Keybindings", height);

		setModal(true);

		fields = Arrays.asList(DynamicVariables.class.getDeclaredFields());

		getContentTable().setSkin(FirstGame.gameSkin);

		// getContentTable().debugCell();

		for (Field field : fields) {
			try {
				if (field.canAccess(null)) {
					if (field.get(null).getClass().equals(Boolean.class)) {
						CheckBox checkbox = GuiFactory.checkBox(field.getName(), field.getBoolean(null));
						contentTable.add(checkbox).left().expandX();
						GuiUtil.row(contentTable);
						continue;
					}
					if (field.get(null).getClass().equals(VHolder.class)) {
						@SuppressWarnings("rawtypes")
						Class<? extends Object> clazz = ((VHolder) field.get(null)).getValue().getClass();
						if (clazz.equals(Boolean.class)) {
							@SuppressWarnings("unchecked")
							VHolder<Boolean> vHolder = (VHolder<Boolean>) field.get(null);
							CheckBox checkbox = GuiFactory.checkBox(field.getName(), vHolder);
							contentTable.add(checkbox).left().expandX();
							GuiUtil.row(contentTable);
							continue;
						}
					}
				}
				Gdx.app.error(this.getClass().getCanonicalName(), "unable to create gui element for " + field.getName()
						+ ":" + field.get(null).getClass().getCanonicalName());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}

}
