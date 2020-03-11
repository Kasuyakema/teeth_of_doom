package com.kumascave.games.teeth_of_doom.core.world;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Duo<T1, T2> {
	T1 a;
	T2 b;

	@Override
	public String toString() {
		return "(" + a + "," + b + ")";
	}
}