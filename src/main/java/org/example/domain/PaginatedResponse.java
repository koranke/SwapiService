package org.example.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class PaginatedResponse {
	private String message;
	private String next;
	private String previous;
	@SerializedName("total_records")
	private int totalRecords;
	@SerializedName("total_pages")
	private int totalPages;
	private List<ItemLink> results;
}
