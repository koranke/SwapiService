package org.example.api;

public class Swapi {

	public static PersonApi people() {
		return new PersonApi();
	}

	public static PlanetApi planets() {
		return new PlanetApi();
	}
}
