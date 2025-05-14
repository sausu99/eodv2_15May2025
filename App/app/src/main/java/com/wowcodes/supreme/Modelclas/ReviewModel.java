package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class ReviewModel {
    private ArrayList<AllReviewer> JSON_DATA;

    public ArrayList<AllReviewer> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<AllReviewer> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }
     }

