package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class GetSellerItems {

    private ArrayList<GetSellerItems.Get_items_Inner> JSON_DATA;

    public ArrayList<GetSellerItems.Get_items_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<GetSellerItems.Get_items_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Get_items_Inner {
        public String o_id,o_name,o_image,o_image1,o_image2,o_image3,o_image4,o_desc,o_color,o_link,o_date,o_edate,o_stime,o_etime,o_amount,o_type,o_min,o_max,o_qty,bid_increment,time_increment,o_price,o_buy,o_status,total_bids,c_color,c_desc;

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

        public String getTotal_bids() {
            return total_bids;
        }

        public void setTotal_bids(String total_bids) {
            this.total_bids = total_bids;
        }

        public String getTotalbids() {
            return total_bids;
        }

        public void setTotalbids(String total_bids) {
            this.total_bids = total_bids;
        }

        public String getO_color() {
            return o_color;
        }

        public void setO_color(String o_color) {
            this.c_color = o_color;
        }

        public String getC_color() {
            return c_color;
        }

        public void setC_color(String c_color) {
            this.c_color = c_color;
        }

        public String getC_desc() {
            return c_desc;
        }

        public void setC_desc(String c_desc) {
            this.c_desc = c_desc;
        }

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

        public String getO_desc() {
            return o_desc;
        }

        public void setO_desc(String o_desc) {
            this.o_desc = o_desc;
        }

        public String getO_link() {
            return o_link;
        }

        public void setO_link(String o_link) {
            this.o_link = o_link;
        }

        public String getO_date() {
            return o_date;
        }

        public void setO_date(String o_date) {
            this.o_date = o_date;
        }

        public String getO_edate() {
            return o_edate;
        }

        public void setO_edate(String o_edate) {
            this.o_edate = o_edate;
        }

        public String getO_stime() {
            return o_stime;
        }

        public void setO_stime(String o_stime) {
            this.o_stime = o_stime;
        }

        public String getO_etime() {
            return o_etime;
        }

        public void setO_etime(String o_etime) {
            this.o_etime = o_etime;
        }

        public String getO_amount() {
            return o_amount;
        }

        public void setO_amount(String o_amount) {
            this.o_amount = o_amount;
        }

        public String getO_type() {
            return o_type;
        }

        public void setO_type(String o_type) {
            this.o_type = o_type;
        }

        public String getO_min() {
            return o_min;
        }

        public void setO_min(String o_min) {
            this.o_min = o_min;
        }

        public String getO_max() {
            return o_max;
        }

        public void setO_max(String o_max) {
            this.o_max = o_max;
        }

        public String getO_qty() {
            return o_qty;
        }

        public void setO_qty(String o_qty) {
            this.o_qty = o_qty;
        }

        public String getBid_increment() {
            return bid_increment;
        }

        public void setBid_increment(String bid_increment) {
            this.bid_increment = bid_increment;
        }

        public String getTime_increment() {
            return time_increment;
        }

        public void setTime_increment(String time_increment) {
            this.time_increment = time_increment;
        }

        public String getO_price() {
            return o_price;
        }

        public void setO_price(String o_price) {
            this.o_price = o_price;
        }

        public String getO_buy() {
            return o_buy;
        }

        public void setO_buy(String o_buy) {
            this.o_buy = o_buy;
        }

        public String getO_status() {
            return o_status;
        }

        public void setO_status(String o_status) {
            this.o_status = o_status;
        }

        @Override
        public String toString() {
            return "Get_items_Inner{" +
                    "o_id='" + o_id + '\'' +
                    ", o_name='" + o_name + '\'' +
                    ", o_image='" + o_image + '\'' +
                    ", o_desc='" + o_desc + '\'' +
                    ", o_link='" + o_link + '\'' +
                    ", o_date='" + o_date + '\'' +
                    ", o_edate='" + o_edate + '\'' +
                    ", o_stime='" + o_stime + '\'' +
                    ", o_etime='" + o_etime + '\'' +
                    ", o_amount='" + o_amount + '\'' +
                    ", o_type='" + o_type + '\'' +
                    ", o_min='" + o_min + '\'' +
                    ", o_max='" + o_max + '\'' +
                    ", o_qty='" + o_qty + '\'' +
                    ", bid_increment='" + bid_increment + '\'' +
                    ", time_increment='" + time_increment + '\'' +
                    ", o_price='" + o_price + '\'' +
                    ", o_buy='" + o_buy + '\'' +
                    ", o_status='" + o_status + '\'' +
                    '}';
        }
    }
}
