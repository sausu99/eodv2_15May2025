package com.wowcodes.supreme.Modelclas;

 import java.util.List;

public class GetLotteryId {
    private List<BallData> JSON_DATA;

    // Getters and Setters

    public List<BallData> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(List<BallData> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }
    public class BallData {
        private String normal_ball_start;
        private String normal_ball_end;
        private String normal_ball_limit;
        private String premium_ball_start;
        private String premium_ball_end;
        private String premium_ball_limit;
        private String ticket_price;

        // Getters and Setters

        public String getNormal_ball_start() {
            return normal_ball_start;
        }

        public void setNormal_ball_start(String normal_ball_start) {
            this.normal_ball_start = normal_ball_start;
        }

        public String getNormal_ball_end() {
            return normal_ball_end;
        }

        public void setNormal_ball_end(String normal_ball_end) {
            this.normal_ball_end = normal_ball_end;
        }

        public String getNormal_ball_limit() {
            return normal_ball_limit;
        }

        public void setNormal_ball_limit(String normal_ball_limit) {
            this.normal_ball_limit = normal_ball_limit;
        }

        public String getPremium_ball_start() {
            return premium_ball_start;
        }

        public void setPremium_ball_start(String premium_ball_start) {
            this.premium_ball_start = premium_ball_start;
        }

        public String getPremium_ball_end() {
            return premium_ball_end;
        }

        public void setPremium_ball_end(String premium_ball_end) {
            this.premium_ball_end = premium_ball_end;
        }

        public String getPremium_ball_limit() {
            return premium_ball_limit;
        }

        public void setPremium_ball_limit(String premium_ball_limit) {
            this.premium_ball_limit = premium_ball_limit;
        }

        public String getTicket_price() {
            return ticket_price;
        }

        public void setTicket_price(String ticket_price) {
            this.ticket_price = ticket_price;
        }
    }

}
