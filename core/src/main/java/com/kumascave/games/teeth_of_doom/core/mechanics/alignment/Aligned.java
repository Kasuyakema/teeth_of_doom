package com.kumascave.games.teeth_of_doom.core.mechanics.alignment;

import com.kumascave.games.teeth_of_doom.util.jgoodies.VHolder;

public interface Aligned {

	public Alignment getAlignment();

	public VHolder<Alignment> getAlignmentHolder();

	public void setAlignment(Alignment newAlignment);
}
