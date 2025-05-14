package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class GetPaymentGateway {

    private ArrayList<GetPaymentGateway.Get_PG_Inner> JSON_DATA;

    public ArrayList<GetPaymentGateway.Get_PG_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<GetPaymentGateway.Get_PG_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Get_PG_Inner {
        public String pg_id,pg_name,pg_type,pg_link,pg_image;
        public boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getPg_id() {
            return pg_id;
        }

        public void setPg_id(String pg_id) {
            this.pg_id = pg_id;
        }

        public String getPg_name() {
            return pg_name;
        }

        public void setPg_name(String pg_name) {
            this.pg_name = pg_name;
        }

        public String getPg_type() {
            return pg_type;
        }

        public void setPg_type(String pg_type) {
            this.pg_type = pg_type;
        }

        public String getPg_link() {
            return pg_link;
        }

        public void setPg_link(String pg_link) {
            this.pg_link = pg_link;
        }

        public String getPg_image() {
            return pg_image;
        }

        public void setPg_image(String pg_image) {
            this.pg_image = pg_image;
        }

        @Override
        public String toString() {
            return "Get_PG_Inner{" +
                    "pg_id='" + pg_id + '\'' +
                    ", pg_name='" + pg_name + '\'' +
                    ", pg_type='" + pg_type + '\'' +
                    ", pg_link='" + pg_link + '\'' +
                    ", pg_image='" + pg_image + '\'' +
                    '}';
        }
    }
}
