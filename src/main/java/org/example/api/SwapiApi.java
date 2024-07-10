package org.example.api;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import io.restassured.response.Response;
import org.example.core.Constants;
import org.example.domain.ItemLink;
import org.example.domain.PaginatedResponse;
import org.example.domain.Result;

import java.lang.reflect.Type;
import java.util.List;

public class SwapiApi<T> extends ApiBase<SwapiApi<T>> {
	private final Type resultType;
	private final Type resultListType;

	/*
	For the sake of Gson deserialization, we need to pass the type of the object we want to deserialize,
	both for single objects and for lists of objects.

	Here's an example of how to do this from the extending class.

			super("planets",
				new TypeToken<Result<Planet>>(){}.getType(),
				new TypeToken<List<Result<Planet>>>(){}.getType());

	 */
	public SwapiApi(String endpoint, Type type, Type listType) {
		baseUrl = Constants.BASE_SWAPI_URL + endpoint + "/";
		resultType = type;
		resultListType = listType;
	}

	public Response tryGetAll() {
		return get("");
	}

	public List<ItemLink> getAll() {
		return tryGetAll()
			.then()
			.statusCode(200)
			.extract()
			.as(PaginatedResponse.class)
			.getResults();
	}

	public List<Result<T>> getFiltered(String filterName, String filterValue) {
		Response response = withQueryParameter(filterName, filterValue).tryGetAll();
		String json = JsonParser.parseString(response.body().prettyPrint()).getAsJsonObject().get("result").toString();
		return new Gson().fromJson(json, resultListType);
	}

	public Response tryGetById(String id) {
		return get(id);
	}

	public Result<T> getById(String id) {
		Response response = tryGetById(id)
			.then()
			.statusCode(200)
			.extract()
			.response();
		String json = JsonParser.parseString(response.body().prettyPrint()).getAsJsonObject().get("result").toString();
		return new Gson().fromJson(json, resultType);
	}
}
