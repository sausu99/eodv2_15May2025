package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class GetProfile {

    private ArrayList<GetProfile.Get_profile_Inner> JSON_DATA;

    public ArrayList<GetProfile.Get_profile_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<GetProfile.Get_profile_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Get_profile_Inner {
        public String total_plays,played_on,item_image,item_name,coins_earned;

        public String getCoins_earned() {
            return coins_earned;
        }

        public void setCoins_earned(String coins_earned) {
            this.coins_earned = coins_earned;
        }

        public String getItem_image() {
            return item_image;
        }

        public void setItem_image(String item_image) {
            this.item_image = item_image;
        }

        public String getTotal_plays() {
            return total_plays;
        }

        public void setTotal_plays(String total_plays) {
            this.total_plays = total_plays;
        }

        public String getPlayed_on() {
            return played_on;
        }

        public void setPlayed_on(String played_on) {
            this.played_on = played_on;
        }

        public String getItem_name() {
            return item_name;
        }

        public void setItem_name(String item_name) {
            this.item_name = item_name;
        }

        @Override
        public String toString() {
            return "Get_names_Inner{" +
                    "total_plays='" + total_plays + '\'' +
                    ", played_on='" + played_on + '\'' +
                    ", item_image='" + item_image + '\'' +
                    ", item_name='" + item_name + '\'' +
                    '}';
        }
    }
}
