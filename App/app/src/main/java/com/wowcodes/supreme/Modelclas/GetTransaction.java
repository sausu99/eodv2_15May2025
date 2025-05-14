/**
 * The GetTransaction class is a model class that represents a list of transaction objects, each
 * containing information such as date, type name, money, id, type details, and type image.
 */

package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class GetTransaction
{
    private ArrayList<Get_transaction_Inner> JSON_DATA;

    public ArrayList<Get_transaction_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<Get_transaction_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Get_transaction_Inner {
        private String date;

        private String type_name;

        private String money;

        private String id;

        private String type_details;
        private String type_image;

        public String getDate ()
        {
            return date;
        }

        public void setDate (String date)
        {
            this.date = date;
        }

        public String getType_name ()
        {
            return type_name;
        }

        public void setType_name (String type_name)
        {
            this.type_name = type_name;
        }

        public String getMoney ()
        {
            return money;
        }

        public void setMoney (String money)
        {
            this.money = money;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getType_details ()
        {
            return type_details;
        }

        public void setType_details (String type_details)
        {
            this.type_details = type_details;
        }


        public String getType_image() {
            return type_image;
        }

        public void setType_image(String type_image) {
            this.type_image = type_image;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [date = "+date+", type_name = "+type_name+", money = "+money+", id = "+id+", type_details = "+type_details+"]";
        }

    }
}
			
	