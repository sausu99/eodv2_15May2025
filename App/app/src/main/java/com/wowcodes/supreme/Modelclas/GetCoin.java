/**
 * The GetCoin class is a model class in Java that represents a JSON response containing a list of
 * coins.
 */
package com.wowcodes.supreme.Modelclas;

import java.io.Serializable;
import java.util.ArrayList;

public class GetCoin
{
    private ArrayList<Get_Coin_Inner> JSON_DATA;

    public ArrayList<Get_Coin_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<Get_Coin_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Get_Coin_Inner implements Serializable {
        String c_id,c_name,c_image,c_coin,c_amount,c_full_price,c_discount,c_status;

        public String getC_id() {
            return c_id;
        }

        public void setC_id(String c_id) {
            this.c_id = c_id;
        }

        public String getC_name() {
            return c_name;
        }

        public void setC_name(String c_name) {
            this.c_name = c_name;
        }

        public String getC_image() {
            return c_image;
        }

        public void setC_image(String c_image) {
            this.c_image = c_image;
        }

        public String getC_coin() {
            return c_coin;
        }

        public void setC_coin(String c_coin) {
            this.c_coin = c_coin;
        }

        public String getC_amt() {
            return c_amount;
        }

        public void setC_amt(String c_amt) {
            this.c_amount = c_amount;
        }

        public String getC_full_price() {
            return c_full_price;
        }

        public void setC_full_price(String c_full_price) {
            this.c_full_price = c_full_price;
        }

        public String getC_discount() {
            return c_discount;
        }

        public void setC_discount(String c_discount) {
            this.c_discount = c_discount;
        }

        public String getC_status() {
            return c_status;
        }

        public void setC_status(String c_status) {
            this.c_status = c_status;
        }

        @Override
        public String toString() {
            return "Get_Coin_Inner{" +
                    "c_id='" + c_id + '\'' +
                    ", c_name='" + c_name + '\'' +
                    ", c_image='" + c_image + '\'' +
                    ", c_coin='" + c_coin + '\'' +
                    ", c_amt='" + c_amount + '\'' +
                    ", c_full_price='" + c_full_price + '\'' +
                    ", c_discount='" + c_discount + '\'' +
                    ", c_status='" + c_status + '\'' +
                    '}';
        }
    }
}
	