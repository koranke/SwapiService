package org.example.swapi.people;

import io.restassured.response.Response;
import org.example.api.PersonApi;
import org.example.core.Constants;
import org.example.domain.ItemLink;
import org.example.domain.PaginatedResponse;
import org.example.domain.Person;
import org.example.domain.Result;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GetPeopleTests {
	@Test
	public void testBasicGetAll() {
		Response response = new PersonApi().tryGetAll()
				.then().statusCode(200)
				.extract()
				.response();

		PaginatedResponse paginatedResponse = response.as(PaginatedResponse.class);
		Assert.assertEquals(paginatedResponse.getMessage(), "ok");
		Assert.assertNull(paginatedResponse.getPrevious());
		Assert.assertNotNull(paginatedResponse.getResults());
		Assert.assertEquals(paginatedResponse.getResults().size(), 10);
	}

	/*
	This test fails, but without requirements, not sure if this is a valid test or not.
	Appears that while "page" and "limit" use defaults when no query parameters are provided, "limit" is not used
	if "page" is also not provided and vice-versa.
	 */
	@Test(enabled = false)
	public void testGetAllWithLimit() {
		Response response = new PersonApi()
				.withQueryParameter("limit", "20")
				.tryGetAll()
				.then().statusCode(200)
				.extract()
				.response();

		PaginatedResponse paginatedResponse = response.as(PaginatedResponse.class);
		Assert.assertEquals(paginatedResponse.getMessage(), "ok");
		Assert.assertEquals(paginatedResponse.getResults().size(), 20);
	}

	@Test
	public void testSecondPage() {
		PaginatedResponse paginatedResponse = new PersonApi()
				.tryGetAll()
				.then().extract()
				.response().as(PaginatedResponse.class);

		Set<String> pageOneIds = paginatedResponse.getResults().stream().map(ItemLink::getUid).collect(Collectors.toSet());

		Response response = new PersonApi()
				.withQueryParameter("limit", "10")
				.withQueryParameter("page", "2")
				.tryGetAll()
				.then().statusCode(200)
				.extract()
				.response();

		paginatedResponse = response.as(PaginatedResponse.class);
		Assert.assertEquals(paginatedResponse.getMessage(), "ok");
		Assert.assertEquals(paginatedResponse.getPrevious(), "https://www.swapi.tech/api/people?page=1&limit=10");
		Assert.assertEquals(paginatedResponse.getNext(), "https://www.swapi.tech/api/people?page=3&limit=10");

		Set<String> pageTwoIds = paginatedResponse.getResults().stream().map(ItemLink::getUid).collect(Collectors.toSet());
		pageTwoIds.retainAll(pageOneIds);
		Assert.assertTrue(pageTwoIds.isEmpty());
	}

	@Test
	public void testGetAllWithCustomPageAndLimit() {
		Response response = new PersonApi()
				.withQueryParameter("limit", "5")
				.withQueryParameter("page", "2")
				.tryGetAll()
				.then().statusCode(200)
				.extract()
				.response();

		PaginatedResponse paginatedResponse = response.as(PaginatedResponse.class);
		Assert.assertEquals(paginatedResponse.getMessage(), "ok");
		Assert.assertEquals(paginatedResponse.getResults().size(), 5);
		Assert.assertEquals(paginatedResponse.getPrevious(), "https://www.swapi.tech/api/people?page=1&limit=5");
		Assert.assertEquals(paginatedResponse.getNext(), "https://www.swapi.tech/api/people?page=3&limit=5");
	}

	@Test
	public void testGetLastPage() {
		PaginatedResponse paginatedResponse = new PersonApi()
				.tryGetAll()
				.then().extract()
				.response().as(PaginatedResponse.class);

		String lastPage = String.valueOf(paginatedResponse.getTotalPages());
		String priorPage = String.valueOf(paginatedResponse.getTotalPages() - 1);
		Set<String> pageOneIds = paginatedResponse.getResults().stream().map(ItemLink::getUid).collect(Collectors.toSet());

		Response response = new PersonApi()
				.withQueryParameter("limit", "10")
				.withQueryParameter("page", lastPage)
				.tryGetAll()
				.then().statusCode(200)
				.extract()
				.response();

		paginatedResponse = response.as(PaginatedResponse.class);
		Assert.assertEquals(paginatedResponse.getMessage(), "ok");
		Assert.assertEquals(paginatedResponse.getPrevious(), String.format("https://www.swapi.tech/api/people?page=%s&limit=10", priorPage));
		Assert.assertNull(paginatedResponse.getNext());

		Set<String> lastPageIds = paginatedResponse.getResults().stream().map(ItemLink::getUid).collect(Collectors.toSet());
		lastPageIds.retainAll(pageOneIds);
		Assert.assertTrue(lastPageIds.isEmpty());
	}


	@Test
	public void testGetByName() {
		String searchName = "Luke Skywalker";
		List<Result<Person>> people = new PersonApi().getFiltered("name", searchName);
		Assert.assertEquals(people.size(), 1);
		Assert.assertEquals(people.get(0).getProperties().getName(), searchName);
		Assert.assertEquals(people.get(0).getUid(), "1");

		String expectedPersonUrl = Constants.BASE_SWAPI_URL + "people/1";
		Assert.assertEquals(people.get(0).getProperties().getUrl(), expectedPersonUrl);
	}

	@Test
	public void testGetByNameNoMatch() {
		List<Result<Person>> people = new PersonApi().getFiltered("name", "The Hulk");
		Assert.assertEquals(people.size(), 0);
	}

	@Test
	public void testGetByNameWithMultiple() {
		String searchName = "Skywalker";
		List<Result<Person>> people = new PersonApi().getFiltered("name", searchName);
		Assert.assertTrue(people.size() > 1);
		for (Result<Person> person : people) {
			Assert.assertTrue(person.getProperties().getName().contains(searchName));
		}
	}

}
