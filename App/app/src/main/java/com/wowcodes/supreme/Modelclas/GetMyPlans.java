/**
 * The GetCoin class is a model class in Java that represents a JSON response containing a list of
 * coins.
 */
package com.wowcodes.supreme.Modelclas;

import java.io.Serializable;
import java.util.ArrayList;

public class GetMyPlans
{
    private ArrayList<Get_My_Plans_Inner> JSON_DATA;

    public ArrayList<Get_My_Plans_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<Get_My_Plans_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public static class Get_My_Plans_Inner implements Serializable {
        String plan_name,plan_short_description,investment_amount,current_value,last_update,next_update,expires_on,order_date,plan_status,plan_interest,plan_interest_type,plan_interest_frequency,plan_color,plan_cancelable,plan_cancel_charge,plan_id;

        public String getPlan_id() {
            return plan_id;
        }

        public void setPlan_id(String plan_id) {
            this.plan_id = plan_id;
        }

        public String getPlan_cancel_charge() {
            return plan_cancel_charge;
        }

        public void setPlan_cancel_charge(String plan_cancel_charge) {
            this.plan_cancel_charge = plan_cancel_charge;
        }

        public String getPlan_cancelable() {
            return plan_cancelable;
        }

        public void setPlan_cancelable(String plan_cancelable) {
            this.plan_cancelable = plan_cancelable;
        }

        public String getPlan_color() {
            return plan_color;
        }

        public void setPlan_color(String plan_color) {
            this.plan_color = plan_color;
        }

        public String getPlan_interest() {
            return plan_interest;
        }

        public void setPlan_interest(String plan_interest) {
            this.plan_interest = plan_interest;
        }

        public String getPlan_interest_type() {
            return plan_interest_type;
        }

        public void setPlan_interest_type(String plan_interest_type) {
            this.plan_interest_type = plan_interest_type;
        }

        public String getPlan_interest_frequency() {
            return plan_interest_frequency;
        }

        public void setPlan_interest_frequency(String plan_interest_frequency) {
            this.plan_interest_frequency = plan_interest_frequency;
        }

        public String getPlan_status() {
            return plan_status;
        }

        public void setPlan_status(String plan_status) {
            this.plan_status = plan_status;
        }

        public String getPlan_name() {
            return plan_name;
        }

        public void setPlan_name(String plan_name) {
            this.plan_name = plan_name;
        }

        public String getPlan_short_description() {
            return plan_short_description;
        }

        public void setPlan_short_description(String plan_short_description) {
            this.plan_short_description = plan_short_description;
        }

        public String getInvestment_amount() {
            return investment_amount;
        }

        public void setInvestment_amount(String investment_amount) {
            this.investment_amount = investment_amount;
        }

        public String getCurrent_value() {
            return current_value;
        }

        public void setCurrent_value(String current_value) {
            this.current_value = current_value;
        }

        public String getLast_update() {
            return last_update;
        }

        public void setLast_update(String last_update) {
            this.last_update = last_update;
        }

        public String getNext_update() {
            return next_update;
        }

        public void setNext_update(String next_update) {
            this.next_update = next_update;
        }

        public String getExpires_on() {
            return expires_on;
        }

        public void setExpires_on(String expires_on) {
            this.expires_on = expires_on;
        }

        public String getOrder_date() {
            return order_date;
        }

        public void setOrder_date(String order_date) {
            this.order_date = order_date;
        }

        @Override
        public String toString() {
            return "Get_My_Plans_Inner{" +
                    "plan_name='" + plan_name + '\'' +
                    ", plan_short_description='" + plan_short_description + '\'' +
                    ", investment_amount='" + investment_amount + '\'' +
                    ", current_value='" + current_value + '\'' +
                    ", last_update='" + last_update + '\'' +
                    ", next_update='" + next_update + '\'' +
                    ", expires_on='" + expires_on + '\'' +
                    ", order_date='" + order_date + '\'' +
                    '}';
        }
    }
}
	