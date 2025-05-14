/**
 * The AllBidder class is a model class that represents a list of AllBidderInner objects.
 */
package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class AllBidder
{
    private ArrayList<AllBidderInner> JSON_DATA;

    public ArrayList<AllBidderInner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<AllBidderInner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }
}
	