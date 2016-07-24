package com.clic.org.serve.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class UserItemsResponse implements Parcelable{

	private String brandID;
	private String brandName;
	private String productID;
	private String productName;
	private String description;
	private String categoryName;
	private String modelNumber;
	private String categoryID;
	private String subcategoryID;
	private String subcategoryName;
	private String yearop;
	private String itemID;
	private String customerID;
	private String warrentyMonths;
	private String specification;
	private String itemImageUrl;
	private String status;
	private ArrayList<ItemDocs> itemDocs;
	public String getBrandID() {
		return brandID;
	}
	public void setBrandID(String brandID) {
		this.brandID = brandID;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getModelNumber() {
		return modelNumber;
	}
	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}
	public String getCategoryID() {
		return categoryID;
	}
	public String getItemImageUrl() {
		return itemImageUrl;
	}
	public void setItemImageUrl(String itemImageUrl) {
		this.itemImageUrl = itemImageUrl;
	}
	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}
	public String getSubcategoryID() {
		return subcategoryID;
	}
	public void setSubcategoryID(String subcategoryID) {
		this.subcategoryID = subcategoryID;
	}
	public String getSubcategoryName() {
		return subcategoryName;
	}
	public void setSubcategoryName(String subcategoryName) {
		this.subcategoryName = subcategoryName;
	}
	public String getYearop() {
		return yearop;
	}
	public void setYearop(String yearop) {
		this.yearop = yearop;
	}
	public String getItemID() {
		return itemID;
	}
	public void setItemID(String itemID) {
		this.itemID = itemID;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public String getWarrentyMonths() {
		return warrentyMonths;
	}
	public void setWarrentyMonths(String warrentyMonths) {
		this.warrentyMonths = warrentyMonths;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ArrayList<ItemDocs> getItemDocs() {
		return itemDocs;
	}
	public void setItemDocs(ArrayList<ItemDocs> itemDocs) {
		this.itemDocs = itemDocs;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.brandID);
		dest.writeString(this.brandName);
		dest.writeString(this.productID);
		dest.writeString(this.productName);
		dest.writeString(this.description);
		dest.writeString(this.categoryName);
		dest.writeString(this.modelNumber);
		dest.writeString(this.categoryID);
		dest.writeString(this.subcategoryID);
		dest.writeString(this.subcategoryName);
		dest.writeString(this.yearop);
		dest.writeString(this.itemID);
		dest.writeString(this.customerID);
		dest.writeString(this.warrentyMonths);
		dest.writeString(this.specification);
		dest.writeString(this.itemImageUrl);
		dest.writeString(this.status);
		dest.writeTypedList(this.itemDocs);
	}

	public UserItemsResponse() {
	}

	protected UserItemsResponse(Parcel in) {
		this.brandID = in.readString();
		this.brandName = in.readString();
		this.productID = in.readString();
		this.productName = in.readString();
		this.description = in.readString();
		this.categoryName = in.readString();
		this.modelNumber = in.readString();
		this.categoryID = in.readString();
		this.subcategoryID = in.readString();
		this.subcategoryName = in.readString();
		this.yearop = in.readString();
		this.itemID = in.readString();
		this.customerID = in.readString();
		this.warrentyMonths = in.readString();
		this.specification = in.readString();
		this.itemImageUrl = in.readString();
		this.status = in.readString();
		this.itemDocs = in.createTypedArrayList(ItemDocs.CREATOR);
	}

	public static final Creator<UserItemsResponse> CREATOR = new Creator<UserItemsResponse>() {
		@Override
		public UserItemsResponse createFromParcel(Parcel source) {
			return new UserItemsResponse(source);
		}

		@Override
		public UserItemsResponse[] newArray(int size) {
			return new UserItemsResponse[size];
		}
	};
}
