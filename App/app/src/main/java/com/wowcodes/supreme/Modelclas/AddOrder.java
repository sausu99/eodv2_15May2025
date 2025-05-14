/**
 * The AddOrder class is a model class that represents the data structure for adding an order,
 * including various attributes such as message, order status, user ID, total amount, payment amount,
 * order address, order ID, discount amount, order date, and offer ID.
 */
package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class AddOrder
{
    private ArrayList<Add_Order_Inner> JSON_DATA;

    public ArrayList<Add_Order_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<Add_Order_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Add_Order_Inner {

        private String msg;

        private String o_status;

        private String u_id;

        private String total_amount;

        private String pay_amount;

        private String o_address;

        private String o_id;

        private String dis_amount;

        private String o_date;

        private String offer_id;

        public String getMsg ()
        {
            return msg;
        }

        public void setMsg (String msg)
        {
            this.msg = msg;
        }

        public String getO_status ()
        {
            return o_status;
        }

        public void setO_status (String o_status)
        {
            this.o_status = o_status;
        }

        public String getU_id ()
        {
            return u_id;
        }

        public void setU_id (String u_id)
        {
            this.u_id = u_id;
        }

        public String getTotal_amount ()
        {
            return total_amount;
        }

        public void setTotal_amount (String total_amount)
        {
            this.total_amount = total_amount;
        }

        public String getPay_amount ()
        {
            return pay_amount;
        }

        public void setPay_amount (String pay_amount)
        {
            this.pay_amount = pay_amount;
        }

        public String getO_address ()
        {
            return o_address;
        }

        public void setO_address (String o_address)
        {
            this.o_address = o_address;
        }

        public String getO_id ()
        {
            return o_id;
        }

        public void setO_id (String o_id)
        {
            this.o_id = o_id;
        }

        public String getDis_amount ()
        {
            return dis_amount;
        }

        public void setDis_amount (String dis_amount)
        {
            this.dis_amount = dis_amount;
        }

        public String getO_date ()
        {
            return o_date;
        }

        public void setO_date (String o_date)
        {
            this.o_date = o_date;
        }

        public String getOffer_id ()
        {
            return offer_id;
        }

        public void setOffer_id (String offer_id)
        {
            this.offer_id = offer_id;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [msg = "+msg+", o_status = "+o_status+", u_id = "+u_id+", total_amount = "+total_amount+", pay_amount = "+pay_amount+", o_address = "+o_address+", o_id = "+o_id+", dis_amount = "+dis_amount+", o_date = "+o_date+", offer_id = "+offer_id+"]";
        }

    }
}
	