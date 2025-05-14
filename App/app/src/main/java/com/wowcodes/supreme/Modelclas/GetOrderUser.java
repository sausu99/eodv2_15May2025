/**
 * The `GetOrderUser` class is a model class in Java that represents a user's order information,
 * including details such as order status, user ID, order name, total amount, payment amount, address,
 * order ID, name, discount amount, order date, and offer ID.
 */
package com.wowcodes.supreme.Modelclas;

import java.io.Serializable;
import java.util.ArrayList;

public class GetOrderUser
{
    private ArrayList<Get_order_user_Inner> JSON_DATA;

    public ArrayList<Get_order_user_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<Get_order_user_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }


    public class Get_order_user_Inner implements Serializable {
        String o_image,rating,review,o_id,u_id,name,offer_id,o_name,total_amount,dis_amount,pay_amount,o_address,order_date,order_status,o_status,seller_name,o_type;
        ArrayList<GetStatusHistory.Get_Status_History_Inner> status_history;

        public String getO_type() {
            return o_type;
        }

        public void setO_type(String o_type) {
            this.o_type = o_type;
        }

        public String getSeller_name() {
            return seller_name;
        }

        public void setSeller_name(String seller_name) {
            this.seller_name = seller_name;
        }

        public String getO_image() {
            return o_image;
        }

        public void setO_image(String o_image) {
            this.o_image = o_image;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }

        public String getO_id() {
            return o_id;
        }

        public void setO_id(String o_id) {
            this.o_id = o_id;
        }

        public String getU_id() {
            return u_id;
        }

        public void setU_id(String u_id) {
            this.u_id = u_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOffer_id() {
            return offer_id;
        }

        public void setOffer_id(String offer_id) {
            this.offer_id = offer_id;
        }

        public String getO_name() {
            return o_name;
        }

        public void setO_name(String o_name) {
            this.o_name = o_name;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getDis_amount() {
            return dis_amount;
        }

        public void setDis_amount(String dis_amount) {
            this.dis_amount = dis_amount;
        }

        public String getPay_amount() {
            return pay_amount;
        }

        public void setPay_amount(String pay_amount) {
            this.pay_amount = pay_amount;
        }

        public String getO_address() {
            return o_address;
        }

        public void setO_address(String o_address) {
            this.o_address = o_address;
        }

        public String getOrder_date() {
            return order_date;
        }

        public void setOrder_date(String order_date) {
            this.order_date = order_date;
        }

        public String getOrder_status() {
            return order_status;
        }

        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }

        public String getO_status() {
            return o_status;
        }

        public void setO_status(String o_status) {
            this.o_status = o_status;
        }

        public ArrayList<GetStatusHistory.Get_Status_History_Inner> getStatus_history() {
            return status_history;
        }

        public void setStatus_history(ArrayList<GetStatusHistory.Get_Status_History_Inner> status_history) {
            this.status_history = status_history;
        }

        @Override
        public String toString() {
            return "Get_order_user_Inner{" +
                    "o_image='" + o_image + '\'' +
                    ", rating='" + rating + '\'' +
                    ", review='" + review + '\'' +
                    ", o_id='" + o_id + '\'' +
                    ", u_id='" + u_id + '\'' +
                    ", name='" + name + '\'' +
                    ", offer_id='" + offer_id + '\'' +
                    ", o_name='" + o_name + '\'' +
                    ", total_amount='" + total_amount + '\'' +
                    ", dis_amount='" + dis_amount + '\'' +
                    ", pay_amount='" + pay_amount + '\'' +
                    ", o_address='" + o_address + '\'' +
                    ", order_date='" + order_date + '\'' +
                    ", order_status='" + order_status + '\'' +
                    ", o_status='" + o_status + '\'' +
                    ", status_history=" + status_history +
                    '}';
        }
    }
}
