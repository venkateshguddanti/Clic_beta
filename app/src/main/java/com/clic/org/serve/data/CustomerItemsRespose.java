package com.clic.org.serve.data;

import java.util.ArrayList;

public class CustomerItemsRespose {

private ArrayList<ServiceRequestAsRespose> servicerequestslist;
private ArrayList<UserItemsResponse> itemslist;
	

	public ArrayList<ServiceRequestAsRespose> ServiceRequestAsRespose() {
		return servicerequestslist;
	}

	public void setServicerequestslist(ArrayList<ServiceRequestAsRespose> servicerequestslist) {
		this.servicerequestslist = servicerequestslist;
	}

	public ArrayList<UserItemsResponse> getItemslist() {
		return itemslist;
	}

	public void setItemslist(ArrayList<UserItemsResponse> itemslist) {
		this.itemslist = itemslist;
	}

	public ArrayList<ServiceRequestAsRespose> getServicerequestslist() {
		return servicerequestslist;
	}
	

}
