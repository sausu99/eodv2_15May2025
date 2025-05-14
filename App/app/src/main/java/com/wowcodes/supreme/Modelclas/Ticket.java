package com.wowcodes.supreme.Modelclas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Ticket {

    @SerializedName("JSON_DATA")
    private List<Ticket_inner> jsonData;

    public List<Ticket_inner> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<Ticket_inner> jsonData) {
        this.jsonData = jsonData;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+jsonData+"]";
    }

    public class Ticket_inner {
        private String ball_1;
        private String ball_2;
        private String ball_3;
        private String ball_4;
        private String ball_5;
        private String ball_6;
        private String ball_7;
        private String ball_8;
        private String purchase_date;
        private String unique_ticket_id;
        private String ticket_price;
        private String normal_ball_limit;
        private String premium_ball_limit;
        private String draw_date;

        public String getDraw_date() {
            return draw_date;
        }

        public void setDraw_date(String draw_date) {
            this.draw_date = draw_date;
        }

        // Getters and setters
        public String getBall_1() {
            return ball_1;
        }

        public void setBall_1(String ball_1) {
            this.ball_1 = ball_1;
        }

        public String getBall_2() {
            return ball_2;
        }

        public void setBall_2(String ball_2) {
            this.ball_2 = ball_2;
        }

        public String getBall_3() {
            return ball_3;
        }

        public void setBall_3(String ball_3) {
            this.ball_3 = ball_3;
        }

        public String getBall_4() {
            return ball_4;
        }

        public void setBall_4(String ball_4) {
            this.ball_4 = ball_4;
        }

        public String getBall_5() {
            return ball_5;
        }

        public void setBall_5(String ball_5) {
            this.ball_5 = ball_5;
        }

        public String getBall_6() {
            return ball_6;
        }

        public void setBall_6(String ball_6) {
            this.ball_6 = ball_6;
        }

        public String getBall_7() {
            return ball_7;
        }

        public void setBall_7(String ball_7) {
            this.ball_7 = ball_7;
        }

        public String getBall_8() {
            return ball_8;
        }

        public void setBall_8(String ball_8) {
            this.ball_8 = ball_8;
        }

        public String getPurchase_date() {
            return purchase_date;
        }

        public void setPurchase_date(String purchase_date) {
            this.purchase_date = purchase_date;
        }

        public String getUnique_ticket_id() {
            return unique_ticket_id;
        }

        public void setUnique_ticket_id(String unique_ticket_id) {
            this.unique_ticket_id = unique_ticket_id;
        }

        public String getTicket_price() {
            return ticket_price;
        }

        public void setTicket_price(String ticket_price) {
            this.ticket_price = ticket_price;
        }

        public String getNormal_ball_limit() {
            return normal_ball_limit;
        }

        public void setNormal_ball_limit(String normal_ball_limit) {
            this.normal_ball_limit = normal_ball_limit;
        }

        public String getPremium_ball_limit() {
            return premium_ball_limit;
        }

        public void setPremium_ball_limit(String premium_ball_limit) {
            this.premium_ball_limit = premium_ball_limit;
        }
    }

}
