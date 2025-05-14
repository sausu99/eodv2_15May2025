/**
 * The GetOffersLive class is a model class that represents a list of live gamesActivity, with each gamesActivity
 * having various properties such as image, description, status, name, start time, end time, date,
 * amount, total bids, and type.
 */
package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class GetOffersLive
{
    private ArrayList<Get_offers_live_Inner> JSON_DATA;

    public ArrayList<Get_offers_live_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<Get_offers_live_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Get_offers_live_Inner {
        private String o_image;

        private String o_desc;

        private String o_status;

        private String o_name;

        private String o_etime;

        private String o_id;
        private String o_type;

        private String o_stime;

        private String o_date;
        private String o_amount;
        private String total_bids;
        private String o_edate;

        public String getO_image ()
        {
            return o_image;
        }

        public void setO_image (String o_image)
        {
            this.o_image = o_image;
        }

        public String getO_desc ()
        {
            return o_desc;
        }

        public void setO_desc (String o_desc)
        {
            this.o_desc = o_desc;
        }


        public String getO_edate() {
            return o_edate;
        }

        public void setO_edate(String o_edate) {
            this.o_edate = o_edate;
        }

        public String getO_status ()
        {
            return o_status;
        }

        public void setO_status (String o_status)
        {
            this.o_status = o_status;
        }

        public String getO_name ()
        {
            return o_name;
        }

        public void setO_name (String o_name)
        {
            this.o_name = o_name;
        }

        public String getO_etime ()
        {
            return o_etime;
        }

        public void setO_etime (String o_etime)
        {
            this.o_etime = o_etime;
        }

        public String getO_id ()
        {
            return o_id;
        }

        public void setO_id (String o_id)
        {
            this.o_id = o_id;
        }

        public String getO_stime ()
        {
            return o_stime;
        }

        public void setO_stime (String o_stime)
        {
            this.o_stime = o_stime;
        }


        public String getO_amount() {
            return o_amount;
        }

        public void setO_amount(String o_amount) {
            this.o_amount = o_amount;
        }

        public String getTotal_bids() {
            return total_bids;
        }

        public void setTotal_bids(String total_bids) {
            this.total_bids = total_bids;
        }

        public String getO_date ()
        {
            return o_date;
        }

        public void setO_date (String o_date)
        {
            this.o_date = o_date;
        }


        public String getO_type() {
            return o_type;
        }

        public void setO_type(String o_type) {
            this.o_type = o_type;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [o_image = "+o_image+"," +
                    " o_desc = "+o_desc+", " +
                    "o_status = "+o_status+", o_name = "+o_name+", o_etime = "+o_etime+", o_id = "+o_id+", o_stime = "+o_stime+", o_date = "+o_date+"]";
        }

    }

}
	