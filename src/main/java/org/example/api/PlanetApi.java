package org.example.api;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.restassured.response.Response;
import org.example.core.Constants;
import org.example.domain.ItemLink;
import org.example.domain.PaginatedResponse;
import org.example.domain.Person;
import org.example.domain.Planet;
import org.example.domain.Result;

import java.lang.reflect.Type;
import java.util.List;

public class PlanetApi extends ApiBase<PlanetApi> {
	private final Type resultType = new TypeToken<Result<Planet>>(){}.getType();

	public PlanetApi() {
		baseUrl = Constants.BASE_SWAPI_URL + "planets/";
	}

	public Response tryGetPlanets() {
		return get("");
	}

	public List<ItemLink> getPlanets() {
		return tryGetPlanets()
			.then()
			.statusCode(200)
			.extract()
			.as(PaginatedResponse.class)
			.getResults();
	}

	public List<Result<Planet>> getPlanets(String searchName) {
		Response response = withQueryParameter("name", searchName).tryGetPlanets();
		String json = JsonParser.parseString(response.body().prettyPrint()).getAsJsonObject().get("result").toString();
		return new Gson().fromJson(json, new TypeToken<List<Result<Person>>>(){}.getType());
	}


	public Response tryGetPlanet(String id) {
		return get(id);
	}

	public Result<Planet> getPlanet(String id) {
		Response response = tryGetPlanet(id)
			.then()
			.statusCode(200)
			.extract()
			.response();
		String json = JsonParser.parseString(response.body().prettyPrint()).getAsJsonObject().get("result").toString();
		return new Gson().fromJson(json, resultType);
	}
}
