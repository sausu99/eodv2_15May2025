/**
 /**
 * The GetBidUser class is a model class that represents the JSON data for a bid user, including their
 * ID, order name, bid amount, name, order ID, bid status, bid ID, bid date, order image, and bid
 * value.
 */
package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class GetBidUser
{
    private ArrayList<Get_biduser_Inner> JSON_DATA;


    public ArrayList<Get_biduser_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<Get_biduser_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Get_biduser_Inner {
        private String u_id;
        private String total_bids;
        private String status;
        private String o_edate;
        private String o_etime;
        private String o_type;

        private String o_name;

        private String bd_amount;

        private String name;

        private String o_id;

        private String bd_status;

        private String bd_id;

        private String bd_date;
        private String o_image;

        private String bd_value;

        public String getU_id ()
        {
            return u_id;
        }

        public void setU_id (String u_id)
        {
            this.u_id = u_id;
        }

        public String getO_name ()
        {
            return o_name;
        }

        public void setO_name (String o_name)
        {
            this.o_name = o_name;
        }

        public String getBd_amount ()
        {
            return bd_amount;
        }

        public String getO_image() {
            return o_image;
        }

        public void setO_image(String o_image) {
            this.o_image = o_image;
        }

        public void setBd_amount (String bd_amount)
        {
            this.bd_amount = bd_amount;
        }

        public String getName ()
        {
            return name;
        }

        public void setName (String name)
        {
            this.name = name;
        }

        public String getO_id ()
        {
            return o_id;
        }

        public void setO_id (String o_id)
        {
            this.o_id = o_id;
        }

        public String getBd_status ()
        {
            return bd_status;
        }

        public void setBd_status (String bd_status)
        {
            this.bd_status = bd_status;
        }

        public String getBd_id ()
        {
            return bd_id;
        }

        public void setBd_id (String bd_id)
        {
            this.bd_id = bd_id;
        }

        public String getBd_date ()
        {
            return bd_date;
        }

        public void setBd_date (String bd_date)
        {
            this.bd_date = bd_date;
        }

        public String getBd_value ()
        {
            return bd_value;
        }

        public void setBd_value (String bd_value)
        {
            this.bd_value = bd_value;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [u_id = "+u_id+", o_name = "+o_name+", bd_amount = "+bd_amount+", name = "+name+", o_id = "+o_id+", bd_status = "+bd_status+", bd_id = "+bd_id+", bd_date = "+bd_date+", bd_value = "+bd_value+"]";
        }

        public String getO_type() {
            return o_type;
        }

        public void setO_type(String o_type) {
            this.o_type = o_type;
        }

        public String getTotal_bids() {
            return total_bids;
        }

        public void setTotal_bids(String total_bids) {
            this.total_bids = total_bids;
        }

        public String getO_etime() {
            return o_etime;
        }

        public void setO_etime(String o_etime) {
            this.o_etime = o_etime;
        }

        public String getO_edate() {
            return o_edate;
        }

        public void setO_edate(String o_edate) {
            this.o_edate = o_edate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}
			
