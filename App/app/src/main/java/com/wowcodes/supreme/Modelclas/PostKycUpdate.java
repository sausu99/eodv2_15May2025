package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class PostKycUpdate {
    private ArrayList<PostKycUpdate.Post_Kyc_Model_Inner> JSON_DATA;

    public ArrayList<PostKycUpdate.Post_Kyc_Model_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<PostKycUpdate.Post_Kyc_Model_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Post_Kyc_Model_Inner {

        private String msg;

        private String kyc_id;

        public String getMsg ()
        {
            return msg;
        }

        public void setMsg (String msg)
        {
            this.msg = msg;
        }

        public String getKyc_id() {
            return kyc_id;
        }

        public void setKyc_id(String kyc_id) {
            this.kyc_id = kyc_id;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [msg = "+msg+", kyc_id = "+kyc_id+"]";
        }
    }
}
