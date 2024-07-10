package org.example.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthType {
	BEARER("Bearer"), BASIC("Basic");

	private final String value;

}
