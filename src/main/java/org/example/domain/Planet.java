package org.example.domain;

import com.google.gson.annotations.SerializedName;

@lombok.Data
public class Planet {
	@SerializedName("orbital_period")
	private String orbitalPeriod;
	@SerializedName("surface_water")
	private String surfaceWater;
	private String diameter;
	private String edited;
	private String gravity;
	private String created;
	private String name;
	private String climate;
	@SerializedName("rotation_period")
	private String rotationPeriod;
	private String terrain;
	private String url;
	private String population;
}
