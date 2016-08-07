package com.clic.org.serve.data;

/**
 * Created by Venkatesh on 06-08-2016.
 */
public class ProfileData {

    private String customerId;
    private String phonenumber;
    private String name;

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
