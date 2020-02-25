package com.kumascave.games.firstgame.core.mechanics.alignment;

import com.kumascave.games.firstgame.util.jgoodies.VHolder;

public interface Aligned {

	public Alignment getAlignment();

	public VHolder<Alignment> getAlignmentHolder();

	public void setAlignment(Alignment newAlignment);
}
