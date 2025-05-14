package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class GetConsolation {

    private ArrayList<GetConsolation.Get_consolation_Inner> JSON_DATA;

    public ArrayList<GetConsolation.Get_consolation_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<GetConsolation.Get_consolation_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Get_consolation_Inner {
        public String s_id,s_name,s_colour,s_type,s_code,s_status,s_expired,s_desc,s_link;

        public String getS_desc() {
            return s_desc;
        }

        public void setS_desc(String s_desc) {
            this.s_desc = s_desc;
        }

        public String getS_link() {
            return s_link;
        }

        public void setS_link(String s_link) {
            this.s_link = s_link;
        }

        public String getS_expired() {
            return s_expired;
        }

        public void setS_expired(String s_expired) {
            this.s_expired = s_expired;
        }

        public String getS_id() {
            return s_id;
        }

        public void setS_id(String s_id) {
            this.s_id = s_id;
        }

        public String getS_name() {
            return s_name;
        }

        public void setS_name(String s_name) {
            this.s_name = s_name;
        }

        public String getS_colour() {
            return s_colour;
        }

        public void setS_colour(String s_colour) {
            this.s_colour = s_colour;
        }

        public String getS_type() {
            return s_type;
        }

        public void setS_type(String s_type) {
            this.s_type = s_type;
        }

        public String getS_code() {
            return s_code;
        }

        public void setS_code(String s_code) {
            this.s_code = s_code;
        }

        public String getS_status() {
            return s_status;
        }

        public void setS_status(String s_status) {
            this.s_status = s_status;
        }

        @Override
        public String toString() {
            return "Get_consolation_Inner{" +
                    "s_id='" + s_id + '\'' +
                    ", s_name='" + s_name + '\'' +
                    ", s_colour='" + s_colour + '\'' +
                    ", s_type='" + s_type + '\'' +
                    ", s_code='" + s_code + '\'' +
                    ", s_status='" + s_status + '\'' +
                    '}';
        }
    }
}
