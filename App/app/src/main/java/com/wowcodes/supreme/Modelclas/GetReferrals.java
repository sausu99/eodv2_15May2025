package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class GetReferrals {

    private ArrayList<Get_Referrals_Inner> JSON_DATA;

    public ArrayList<Get_Referrals_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<Get_Referrals_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
        {
            return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
        }

    public class Get_Referrals_Inner {
        String user_id,referral_bonus,name,email;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getReferral_bonus() {
            return referral_bonus;
        }

        public void setReferral_bonus(String referral_bonus) {
            this.referral_bonus = referral_bonus;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public String toString() {
            return "ClassPojo [user_id = " + user_id + ", " +
                    "name = " + name + ", " +
                    "email = " + email + ", " +
                    "referral_bonus = " + referral_bonus + "]";
        }

    }
}
