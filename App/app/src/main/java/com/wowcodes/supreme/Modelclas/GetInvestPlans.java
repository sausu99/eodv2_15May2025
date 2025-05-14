/**
 * The GetCoin class is a model class in Java that represents a JSON response containing a list of
 * coins.
 */
package com.wowcodes.supreme.Modelclas;

import java.io.Serializable;
import java.util.ArrayList;

public class GetInvestPlans
{
    private ArrayList<Get_Invest_Plans_Inner> JSON_DATA;

    public ArrayList<Get_Invest_Plans_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<Get_Invest_Plans_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public static class Get_Invest_Plans_Inner implements Serializable {
        String admin_email,plan_id,plan_name,plan_short_description,plan_description,investment,plan_minimum,plan_maximum,plan_interest,plan_interest_type,plan_color,plan_duration,compound_interest,plan_penalty,plan_penalty_type,plan_cancelable,plan_lifetime,plan_capital_back,plan_interest_frequency;

        public String getPlan_interest_frequency() {
            return plan_interest_frequency;
        }

        public void setPlan_interest_frequency(String plan_interest_frequency) {
            this.plan_interest_frequency = plan_interest_frequency;
        }

        public String getAdmin_email() {
            return admin_email;
        }

        public void setAdmin_email(String admin_email) {
            this.admin_email = admin_email;
        }

        public String getPlan_id() {
            return plan_id;
        }

        public void setPlan_id(String plan_id) {
            this.plan_id = plan_id;
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

        public String getPlan_description() {
            return plan_description;
        }

        public void setPlan_description(String plan_description) {
            this.plan_description = plan_description;
        }

        public String getInvestment() {
            return investment;
        }

        public void setInvestment(String investment) {
            this.investment = investment;
        }

        public String getPlan_minimum() {
            return plan_minimum;
        }

        public void setPlan_minimum(String plan_minimum) {
            this.plan_minimum = plan_minimum;
        }

        public String getPlan_maximum() {
            return plan_maximum;
        }

        public void setPlan_maximum(String plan_maximum) {
            this.plan_maximum = plan_maximum;
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

        public String getPlan_color() {
            return plan_color;
        }

        public void setPlan_color(String plan_color) {
            this.plan_color = plan_color;
        }

        public String getPlan_duration() {
            return plan_duration;
        }

        public void setPlan_duration(String plan_duration) {
            this.plan_duration = plan_duration;
        }

        public String getCompound_interest() {
            return compound_interest;
        }

        public void setCompound_interest(String compound_interest) {
            this.compound_interest = compound_interest;
        }

        public String getPlan_penalty() {
            return plan_penalty;
        }

        public void setPlan_penalty(String plan_penalty) {
            this.plan_penalty = plan_penalty;
        }

        public String getPlan_penalty_type() {
            return plan_penalty_type;
        }

        public void setPlan_penalty_type(String plan_penalty_type) {
            this.plan_penalty_type = plan_penalty_type;
        }

        public String getPlan_cancelable() {
            return plan_cancelable;
        }

        public void setPlan_cancelable(String plan_cancelable) {
            this.plan_cancelable = plan_cancelable;
        }

        public String getPlan_lifetime() {
            return plan_lifetime;
        }

        public void setPlan_lifetime(String plan_lifetime) {
            this.plan_lifetime = plan_lifetime;
        }

        public String getPlan_capital_back() {
            return plan_capital_back;
        }

        public void setPlan_capital_back(String plan_capital_back) {
            this.plan_capital_back = plan_capital_back;
        }

        @Override
        public String toString() {
            return "Get_Invest_Plans_Inner{" +
                    "admin_email='" + admin_email + '\'' +
                    ", plan_id='" + plan_id + '\'' +
                    ", plan_name='" + plan_name + '\'' +
                    ", plan_short_description='" + plan_short_description + '\'' +
                    ", plan_description='" + plan_description + '\'' +
                    ", investment='" + investment + '\'' +
                    ", plan_minimum='" + plan_minimum + '\'' +
                    ", plan_maximum='" + plan_maximum + '\'' +
                    ", plan_interest='" + plan_interest + '\'' +
                    ", plan_interest_type='" + plan_interest_type + '\'' +
                    ", plan_color='" + plan_color + '\'' +
                    ", plan_duration='" + plan_duration + '\'' +
                    ", compound_interest='" + compound_interest + '\'' +
                    ", plan_penalty='" + plan_penalty + '\'' +
                    ", plan_penalty_type='" + plan_penalty_type + '\'' +
                    ", plan_cancelable='" + plan_cancelable + '\'' +
                    ", plan_lifetime='" + plan_lifetime + '\'' +
                    ", plan_capital_back='" + plan_capital_back + '\'' +
                    '}';
        }
    }
}
	