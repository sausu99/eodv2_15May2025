/**
 * The GetOffersWinner class is a model class in Java that represents the data structure for retrieving
 * gamesActivity and Winners.
 */
package com.wowcodes.supreme.Modelclas;

import java.io.Serializable;
import java.util.ArrayList;

public class GetPennyBidders {
    private ArrayList<Get_penny_bidders_Inner> JSON_DATA;

    public ArrayList<Get_penny_bidders_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<Get_penny_bidders_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString() {
        return "ClassPojo [JSON_DATA = " + JSON_DATA + "]";
    }

    public class Get_penny_bidders_Inner implements Serializable {

        public String user_id,user_name,value,played_on,user_image;

        public String getUser_image() {
            return user_image;
        }

        public void setUser_image(String user_image) {
            this.user_image = user_image;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getPlayed_on() {
            return played_on;
        }

        public void setPlayed_on(String played_on) {
            this.played_on = played_on;
        }

        @Override
        public String toString() {
            return "Get_multi_winner_Inner{" +
                    "user_id='" + user_id + '\'' +
                    ", user_name='" + user_name + '\'' +
                    ", value='" + value + '\'' +
                    ", played_on='" + played_on + '\'' +
                    '}';
        }
    }
}
	