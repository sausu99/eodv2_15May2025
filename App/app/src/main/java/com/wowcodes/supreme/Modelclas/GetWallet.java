/**
 * The GetWallet class is a model class that represents the JSON data for a user's wallet, including
 * information such as wallet ID, user ID, package ID, coin amount, order ID, date, status, name,
 * amount, coins, and image.
 */
package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class GetWallet
{
    private ArrayList<Get_Wallet_Inner> JSON_DATA;

    public ArrayList<Get_Wallet_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<Get_Wallet_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Get_Wallet_Inner {
        private String wp_id;

        private String wp_user;

        private String wp_package_id;

        private String c_coin;

        private String wp_order_id;

        private String wp_date;

        private String wp_status;

        private String c_name;

        private String c_amount;

        private String wp_coins;

        private String wp_amount;

        private String c_image;

        public String getWp_id ()
        {
            return wp_id;
        }

        public void setWp_id (String wp_id)
        {
            this.wp_id = wp_id;
        }

        public String getWp_user ()
        {
            return wp_user;
        }

        public void setWp_user (String wp_user)
        {
            this.wp_user = wp_user;
        }

        public String getWp_package_id ()
        {
            return wp_package_id;
        }

        public void setWp_package_id (String wp_package_id)
        {
            this.wp_package_id = wp_package_id;
        }

        public String getC_coin ()
        {
            return c_coin;
        }

        public void setC_coin (String c_coin)
        {
            this.c_coin = c_coin;
        }

        public String getWp_order_id ()
        {
            return wp_order_id;
        }

        public void setWp_order_id (String wp_order_id)
        {
            this.wp_order_id = wp_order_id;
        }

        public String getWp_date ()
        {
            return wp_date;
        }

        public void setWp_date (String wp_date)
        {
            this.wp_date = wp_date;
        }

        public String getWp_status ()
        {
            return wp_status;
        }

        public void setWp_status (String wp_status)
        {
            this.wp_status = wp_status;
        }

        public String getC_name ()
        {
            return c_name;
        }

        public void setC_name (String c_name)
        {
            this.c_name = c_name;
        }

        public String getC_amount ()
        {
            return c_amount;
        }

        public void setC_amount (String c_amount)
        {
            this.c_amount = c_amount;
        }

        public String getWp_coins ()
        {
            return wp_coins;
        }

        public void setWp_coins (String wp_coins)
        {
            this.wp_coins = wp_coins;
        }

        public String getWp_amount ()
        {
            return wp_amount;
        }

        public void setWp_amount (String wp_amount)
        {
            this.wp_amount = wp_amount;
        }

        public String getC_image ()
        {
            return c_image;
        }

        public void setC_image (String c_image)
        {
            this.c_coin = c_image;
        }
        @Override
        public String toString()
        {
            return "ClassPojo [wp_id = "+wp_id+", wp_user = "+wp_user+", wp_package_id = "+wp_package_id+", c_coin = "+c_coin+", wp_order_id = "+wp_order_id+", wp_date = "+wp_date+", wp_status = "+wp_status+", c_name = "+c_name+", c_amount = "+c_amount+", wp_coins = "+wp_coins+", wp_amount = "+wp_amount+", c_image = "+c_image+"]";
        }
    }
}
	