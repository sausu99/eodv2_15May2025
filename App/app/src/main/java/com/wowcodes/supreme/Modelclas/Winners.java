package com.wowcodes.supreme.Modelclas;

import java.util.List;

public class Winners {
    private List<winners_inner> JSON_DATA;

    // Getters and setters
    public List<winners_inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(List<winners_inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString() {
        return "Winners{" +
                "JSON_DATA=" + JSON_DATA +
                '}';
    }

    public class winners_inner {
        private String o_id;
        private String o_name;
        private String prize_pool;

        public String getPrize_pool() {
            return prize_pool;
        }

        public void setPrize_pool(String prize_pool) {
            this.prize_pool = prize_pool;
        }

        private String o_image;
        private String o_edate;
        private String o_type;
        private String winner_name;
        private String winning_value;
        private String is_winner;
        private String multiple_winner;
        private List<WinnerMultiple> winners;

        // Getters and setters
        public String getO_id() {
            return o_id;
        }

        public void setO_id(String o_id) {
            this.o_id = o_id;
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

        public String getIs_winner() {
            return is_winner;
        }

        public void setIs_winner(String is_winner) {
            this.is_winner = is_winner;
        }

        public String getMultiple_winner() {
            return multiple_winner;
        }

        public void setMultiple_winner(String multiple_winner) {
            this.multiple_winner = multiple_winner;
        }

        public List<WinnerMultiple> getWinners() {
            return winners;
        }

        public void setWinners(List<WinnerMultiple> winners) {
            this.winners = winners;
        }
    }

}
