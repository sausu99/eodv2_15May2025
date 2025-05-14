package com.wowcodes.supreme.Modelclas;

import java.util.List;

public class MultipleWinnersId {
    private List<Item> JSON_DATA;

    public List<Item> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(List<Item> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    public static class Item {
        private String o_id;
        private String o_edate;
        private String o_type;
        private String prize_pool;
        private String multiple_winner;
        private String o_image;

        public String getO_image() {
            return o_image;
        }

        public void setO_image(String o_image) {
            this.o_image = o_image;
        }

        private String o_image1;
        private String o_image2;
        private String o_image3;
        private String o_image4;

        public String getO_image1() {
            return o_image1;
        }

        public void setO_image1(String o_image1) {
            this.o_image1 = o_image1;
        }

        public String getO_image2() {
            return o_image2;
        }

        public void setO_image2(String o_image2) {
            this.o_image2 = o_image2;
        }

        public String getO_image3() {
            return o_image3;
        }

        public void setO_image3(String o_image3) {
            this.o_image3 = o_image3;
        }

        public String getO_image4() {
            return o_image4;
        }

        public void setO_image4(String o_image4) {
            this.o_image4 = o_image4;
        }

        private List<Winner> winners;

        // Getters and Setters
        public String getO_id() {
            return o_id;
        }

        public void setO_id(String o_id) {
            this.o_id = o_id;
        }

        public String getO_edate() {
            return o_edate;
        }

        public void setO_edate(String o_edate) {
            this.o_edate = o_edate;
        }

        public String getO_type() {
            return o_type;
        }

        public void setO_type(String o_type) {
            this.o_type = o_type;
        }

        public String getPrize_pool() {
            return prize_pool;
        }

        public void setPrize_pool(String prize_pool) {
            this.prize_pool = prize_pool;
        }

        public String getMultiple_winner() {
            return multiple_winner;
        }

        public void setMultiple_winner(String multiple_winner) {
            this.multiple_winner = multiple_winner;
        }

        public List<Winner> getWinners() {
            return winners;
        }

        public void setWinners(List<Winner> winners) {
            this.winners = winners;
        }
    }

    public static class Winner {
        private String is_winner;
        private String order_placed;
        private String item_id;

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        private String rank; // Can be null
        private String winner_name; // Can be null
        private String winning_value; // Can be null
        private String o_name;
        private String user_image;

        public String getUser_image() {
            return user_image;
        }

        public void setUser_image(String user_image) {
            this.user_image = user_image;
        }

        private String o_image;
        private String item_worth;

        // Getters and Setters
        public String getIs_winner() {
            return is_winner;
        }

        public void setIs_winner(String is_winner) {
            this.is_winner = is_winner;
        }

        public String getOrder_placed() {
            return order_placed;
        }

        public void setOrder_placed(String order_placed) {
            this.order_placed = order_placed;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getWinner_name() {
            return winner_name;
        }

        public void setWinner_name(String winner_name) {
            this.winner_name = winner_name;
        }

        public String getWinning_value() {
            return winning_value;
        }

        public void setWinning_value(String winning_value) {
            this.winning_value = winning_value;
        }

        public String getO_name() {
            return o_name;
        }

        public void setO_name(String o_name) {
            this.o_name = o_name;
        }

        public String getO_image() {
            return o_image;
        }

        public void setO_image(String o_image) {
            this.o_image = o_image;
        }

        public String getItem_worth() {
            return item_worth;
        }

        public void setItem_worth(String item_worth) {
            this.item_worth = item_worth;
        }
    }
}

