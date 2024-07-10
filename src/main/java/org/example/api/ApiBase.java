package org.example.api;

import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.enums.AuthType;

import java.util.Map;

public class ApiBase<T> {
	private RequestSpecification requestSpecification;
	protected String baseUrl;
	protected String contentType = "application/json; charset=UTF-8";
	protected String accept;
	protected String authorization;
	protected AuthType authType;
	protected Map<String, String> headers;
	protected Map<String, String> queryParameters;

	public T withQueryParameter(String key, String value) {
		if (queryParameters == null) {
			queryParameters = new java.util.HashMap<>();
		}
		queryParameters.put(key, value);
		return (T) this;
	}

	public T withHeader(String key, String value) {
		if (headers == null) {
			headers = new java.util.HashMap<>();
		}
		headers.put(key, value);
		return (T) this;
	}

	public T withAuthorization(String value) {
		authorization = value;
		return (T) this;
	}

	public T withAuthType(AuthType value) {
		authType = value;
		return (T) this;
	}

	public T withContentType(String value) {
		contentType = value;
		return (T) this;
	}

	public T withAccept(String value) {
		accept = value;
		return (T) this;
	}

	public T withBaseUrl(String value) {
		baseUrl = value;
		return (T) this;
	}

	protected Response get(String endpoint) {
		configureClient();
		return requestSpecification.get(baseUrl + endpoint);
	}

	protected Response post(String endpoint, Object body) {
		String bodyString = new Gson().toJson(body);
		return post(endpoint, bodyString);
	}

	protected Response post(String endpoint, String body) {
		configureClient();
		return requestSpecification.body(body).post(baseUrl + endpoint);
	}

	protected Response put(String endpoint, String body) {
		configureClient();
		return requestSpecification.body(body).put(baseUrl + endpoint);
	}

	protected Response delete(String endpoint) {
		configureClient();
		return requestSpecification.delete(baseUrl + endpoint);
	}



	private void configureClient() {
		requestSpecification = io.restassured.RestAssured.given();
		requestSpecification.filters(new io.restassured.filter.log.RequestLoggingFilter(),
				new io.restassured.filter.log.ResponseLoggingFilter(),
				new io.restassured.filter.log.ErrorLoggingFilter());
		requestSpecification.baseUri(baseUrl);
		if (authorization != null) {
			configureAuthorization();
		}
		if (contentType != null) {
			requestSpecification.contentType(contentType);
		}
		if (accept != null) {
			requestSpecification.accept(accept);
		}
		if (headers != null) {
			requestSpecification.headers(headers);
		}
		if (queryParameters != null) {
			requestSpecification.queryParams(queryParameters);
		}
	}

	private void configureAuthorization() {
		if (authType == AuthType.BEARER) {
			withHeader("Authorization", String.format("%s %s", authType.getValue(), authorization));
		} else if (authType == AuthType.BASIC) {
			requestSpecification.auth().preemptive().basic(authorization, "");
		} else {
			throw new IllegalArgumentException("Unsupported authorization type");
		}
	}


}
