package org.example.swapi;

import org.example.api.PlanetApi;
import org.example.domain.Planet;
import org.example.domain.Result;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetPlanetTests {

	@Test
	public void testGetSingleById() {
		Result<Planet> result = new PlanetApi().getPlanet("1");
		Assert.assertEquals(result.getUid(), "1");
		Assert.assertEquals(result.getDescription(), "A planet.");
		Planet planet = result.getProperties();
		Assert.assertEquals(planet.getName(), "Tatooine");
		Assert.assertEquals(planet.getDiameter(), "10465");
	}

}
