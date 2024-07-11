package org.example.swapi.people;

import io.restassured.response.Response;
import org.example.api.Swapi;
import org.example.domain.Message;
import org.example.domain.Person;
import org.example.domain.Result;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class GetPersonTests {

	@Test
	public void testGetSingleById() {
		Result<Person> result = Swapi.people().getById("1");
		Assert.assertEquals(result.getUid(), "1");
		Assert.assertEquals(result.getDescription(), "A person within the Star Wars universe");
		Person person = result.getProperties();
		Assert.assertEquals(person.getName(), "Luke Skywalker");
		Assert.assertEquals(person.getHeight(), "172");
	}


	@DataProvider(name = "InvalidIdScenarios")
	public Object[][] getInvalidIdScenarios() {
		List<Object[]> data = new ArrayList<>();
		String scenario;

		//------------------------------------------------
		scenario = "99999999999999999999999999999999999999999999999999999";
		data.add(new Object[]{ scenario });
		//------------------------------------------------
		scenario = "X";
		data.add(new Object[]{ scenario });
		//------------------------------------------------

		return data.toArray(new Object[][]{});
	}

	@Test(dataProvider = "InvalidIdScenarios")
	public void testGetUsingInvalidId(String id) {
		Response response = Swapi.people().tryGetById(id)
				.then().statusCode(404)
				.extract()
				.response();

		Message message = response.then().extract().as(Message.class);
		Assert.assertEquals(message.getMessage(), "not found");
	}

}
