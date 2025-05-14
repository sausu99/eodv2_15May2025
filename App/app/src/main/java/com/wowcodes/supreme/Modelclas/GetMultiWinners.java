/**
 * The GetOffersWinner class is a model class in Java that represents the data structure for retrieving
 * gamesActivity and Winners.
 */
package com.wowcodes.supreme.Modelclas;

import java.io.Serializable;
import java.util.ArrayList;

public class GetMultiWinners {
    private ArrayList<Get_multi_winner_Inner> JSON_DATA;

    public ArrayList<Get_multi_winner_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<Get_multi_winner_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString() {
        return "ClassPojo [JSON_DATA = " + JSON_DATA + "]";
    }

    public class Get_multi_winner_Inner implements Serializable {

        public String winner_id,winner_name,winner_image, winning_value,winner_join_date;

        public String getWinner_join_date() {
            return winner_join_date;
        }

        public void setWinner_join_date(String winner_join_date) {
            this.winner_join_date = winner_join_date;
        }

        public String getWinner_id() {
            return winner_id;
        }

        public void setWinner_id(String winner_id) {
            this.winner_id = winner_id;
        }

        public String getWinner_name() {
            return winner_name;
        }

        public void setWinner_name(String winner_name) {
            this.winner_name = winner_name;
        }

        public String getWinner_image() {
            return winner_image;
        }

        public void setWinner_image(String winner_image) {
            this.winner_image = winner_image;
        }

        public String getWinning_value() {
            return winning_value;
        }

        public void setWinning_value(String winning_value) {
            this.winning_value = winning_value;
        }

        @Override
        public String toString() {
            return "Get_multi_winner_Inner{" +
                    "winner_id='" + winner_id + '\'' +
                    ", winner_name='" + winner_name + '\'' +
                    ", winner_image='" + winner_image + '\'' +
                    ", winning_value='" + winning_value + '\'' +
                    '}';
        }
    }
}
	