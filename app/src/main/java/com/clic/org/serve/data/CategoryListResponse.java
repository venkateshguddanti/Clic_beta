package com.clic.org.serve.data;

import java.util.ArrayList;

public class CategoryListResponse {

	private String responseCode;
	private String status;
	private ArrayList<Category> categorylist;
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ArrayList<Category> getCategorylist() {
		return categorylist;
	}
	public void setCategorylist(ArrayList<Category> categorylist) {
		this.categorylist = categorylist;
	}

	
}