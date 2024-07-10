package org.example.api;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.restassured.response.Response;
import org.example.core.Constants;
import org.example.domain.ItemLink;
import org.example.domain.PaginatedResponse;
import org.example.domain.Person;
import org.example.domain.Result;

import java.lang.reflect.Type;
import java.util.List;

public class PersonApi extends ApiBase<PersonApi> {
	private final Type resultType = new TypeToken<Result<Person>>(){}.getType();

	public PersonApi() {
		baseUrl = Constants.BASE_SWAPI_URL + "people/";
	}

	public Response tryGetPeople() {
		return get("");
	}

	public List<ItemLink> getPeople() {
		return tryGetPeople()
			.then()
			.statusCode(200)
			.extract()
			.as(PaginatedResponse.class)
			.getResults();
	}

	public List<Result<Person>> getPeople(String searchName) {
		Response response = withQueryParameter("name", searchName).tryGetPeople();
		String json = JsonParser.parseString(response.body().prettyPrint()).getAsJsonObject().get("result").toString();
		return new Gson().fromJson(json, new TypeToken<List<Result<Person>>>(){}.getType());
	}


	public Response tryGetPerson(String id) {
		return get(id);
	}

	public Result<Person> getPerson(String id) {
		Response response = tryGetPerson(id)
			.then()
			.statusCode(200)
			.extract()
			.response();
		String json = JsonParser.parseString(response.body().prettyPrint()).getAsJsonObject().get("result").toString();
		return new Gson().fromJson(json, resultType);
	}
}
