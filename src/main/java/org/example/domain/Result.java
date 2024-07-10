package org.example.domain;

import lombok.Data;

@Data
public class Result<T> {
	private T properties;
	private String description;
	private String id;
	private String uid;
	private String version;
}
