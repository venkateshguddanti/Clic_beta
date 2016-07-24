package com.clic.org.serve.data;

import java.util.ArrayList;

public class DocTypeResponse {

	private ArrayList<DocsType> docstype;
	private String serviceType;
	private String status;
	private String responseCode;
	private String customerID;


	public ArrayList<DocsType> getDocstype() {
		return docstype;
	}

	public void setDocstype(ArrayList<DocsType> docstype) {
		this.docstype = docstype;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	

}
