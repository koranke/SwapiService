package org.example.swapi.planets;

import org.example.api.Swapi;
import org.example.domain.Planet;
import org.example.domain.Result;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetPlanetTests {

	@Test
	public void testGetSingleById() {
		Result<Planet> result = Swapi.planets().getById("1");
		Assert.assertEquals(result.getUid(), "1");
		Assert.assertEquals(result.getDescription(), "A planet.");
		Planet planet = result.getProperties();
		Assert.assertEquals(planet.getName(), "Tatooine");
		Assert.assertEquals(planet.getDiameter(), "10465");
	}

}
