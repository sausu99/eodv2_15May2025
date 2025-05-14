package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class GetPrizes {

    private ArrayList<GetPrizes.Get_prizes_Inner> JSON_DATA;

    public ArrayList<GetPrizes.Get_prizes_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<GetPrizes.Get_prizes_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Get_prizes_Inner {
        public String prize_id,prize_name,rank,prize_image;

        public String getPrize_image() {
            return prize_image;
        }

        public void setPrize_image(String prize_image) {
            this.prize_image = prize_image;
        }

        public String getPrize_id() {
            return prize_id;
        }

        public void setPrize_id(String prize_id) {
            this.prize_id = prize_id;
        }

        public void setPrize_name(String prize_name) {
            this.prize_name = prize_name;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getPrize_name() {
            return prize_name;
        }

        public String getRank() {
            return rank;
        }
    }
}
