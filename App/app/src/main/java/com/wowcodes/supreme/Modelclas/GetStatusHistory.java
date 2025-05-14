/**
 * The GetCoin class is a model class in Java that represents a JSON response containing a list of
 * coins.
 */
package com.wowcodes.supreme.Modelclas;

import java.io.Serializable;
import java.util.ArrayList;

public class GetStatusHistory
{
    private ArrayList<Get_Status_History_Inner> JSON_DATA;

    public ArrayList<Get_Status_History_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<Get_Status_History_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Get_Status_History_Inner implements Serializable {
        String date,status;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Get_Coin_Inner{" +
                    "date='" + date + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }
    }
}
	