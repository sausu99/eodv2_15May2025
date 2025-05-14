/**
 * The SuccessModel class is a Java model class that represents a JSON response containing success and
 * message data.
 */
package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class SuccessModel
{
    private ArrayList<Suc_Model_Inner> JSON_DATA;

    public ArrayList<Suc_Model_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<Suc_Model_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Suc_Model_Inner {

        private String msg;
        private String bid_status;

        public String getBid_status() {
            return bid_status;
        }

        public void setBid_status(String bid_status) {
            this.bid_status = bid_status;
        }

        private String success;

        public String getMsg ()
        {
            return msg;
        }

        public void setMsg (String msg)
        {
            this.msg = msg;
        }

        public String getSuccess ()
        {
            return success;
        }

        public void setSuccess (String success)
        {
            this.success = success;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [msg = "+msg+", success = "+success+"]";
        }


    }
}
	