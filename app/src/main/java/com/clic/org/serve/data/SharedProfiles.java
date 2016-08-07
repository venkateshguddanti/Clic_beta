package com.clic.org.serve.data;

import java.util.ArrayList;

/**
 * Created by Venkatesh on 06-08-2016.
 */
public class SharedProfiles {

    private ArrayList<Profile> sahredProfilesList;
    private String customerId;
    private String responsecode;

    public ArrayList<Profile> getSahredProfilesList() {
        return sahredProfilesList;
    }

    public void setSahredProfilesList(ArrayList<Profile> sahredProfilesList) {
        this.sahredProfilesList = sahredProfilesList;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }
}
