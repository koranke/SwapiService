package org.example.api;

import com.google.gson.reflect.TypeToken;
import org.example.domain.Person;
import org.example.domain.Result;

import java.util.List;

public class PersonApi extends SwapiApi<Person> {

	public PersonApi() {
		super("people",
				new TypeToken<Result<Person>>(){}.getType(),
				new TypeToken<List<Result<Person>>>(){}.getType());
	}
}
