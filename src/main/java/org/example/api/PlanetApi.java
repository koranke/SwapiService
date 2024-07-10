package org.example.api;

import com.google.gson.reflect.TypeToken;
import org.example.domain.Planet;
import org.example.domain.Result;
import java.util.List;

public class PlanetApi extends SwapiApi<Planet> {

	public PlanetApi() {
		super("planets",
				new TypeToken<Result<Planet>>(){}.getType(),
				new TypeToken<List<Result<Planet>>>(){}.getType());
	}

}
