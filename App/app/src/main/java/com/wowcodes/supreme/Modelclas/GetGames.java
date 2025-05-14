/**
 * The GetOffersLive class is a model class that represents a list of live gamesActivity, with each gamesActivity
 * having various properties such as image, description, status, name, start time, end time, date,
 * amount, total bids, and type.
 */
package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class GetGames
{
    private ArrayList<Get_games_Inner> JSON_DATA;

    public ArrayList<Get_games_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<Get_games_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Get_games_Inner {
        public String rps_status,rps_min,rps_max,rps_win,rps_chance,rps_image,gn_status,gn_min,gn_max,gn_win,gn_chance,gn_image,spin_status,spin_min,spin_max,spin_win,spin_chance,spin_image,ct_status,ct_min,ct_max,ct_win,ct_chance,ct_image,ouc_status,ouc_min,ouc_max,ouc_win_min,ouc_win_max,ouc_bonus1,ouc_bonus2,ouc_bonus3,ouc_amount,cric_status,cric_min,cric_max,cric_win,cric_chance;

        public String getRps_status() {
            return rps_status;
        }

        public void setRps_status(String rps_status) {
            this.rps_status = rps_status;
        }

        public String getGn_status() {
            return gn_status;
        }

        public void setGn_status(String gn_status) {
            this.gn_status = gn_status;
        }

        public String getSpin_status() {
            return spin_status;
        }

        public void setSpin_status(String spin_status) {
            this.spin_status = spin_status;
        }

        public String getCt_status() {
            return ct_status;
        }

        public void setCt_status(String ct_status) {
            this.ct_status = ct_status;
        }

        public String getOuc_status() {
            return ouc_status;
        }

        public void setOuc_status(String ouc_status) {
            this.ouc_status = ouc_status;
        }

        public String getCric_status() {
            return cric_status;
        }

        public void setCric_status(String cric_status) {
            this.cric_status = cric_status;
        }

        public String getCric_min() {
            return cric_min;
        }

        public void setCric_min(String cric_min) {
            this.cric_min = cric_min;
        }

        public String getCric_max() {
            return cric_max;
        }

        public void setCric_max(String cric_max) {
            this.cric_max = cric_max;
        }

        public String getCric_win() {
            return cric_win;
        }

        public void setCric_win(String cric_win) {
            this.cric_win = cric_win;
        }

        public String getCric_chance() {
            return cric_chance;
        }

        public void setCric_chance(String cric_chance) {
            this.cric_chance = cric_chance;
        }

        public String getOuc_min() {
            return ouc_min;
        }

        public void setOuc_min(String ouc_min) {
            this.ouc_min = ouc_min;
        }

        public String getOuc_max() {
            return ouc_max;
        }

        public void setOuc_max(String ouc_max) {
            this.ouc_max = ouc_max;
        }

        public String getOuc_win_min() {
            return ouc_win_min;
        }

        public void setOuc_win_min(String ouc_win_min) {
            this.ouc_win_min = ouc_win_min;
        }

        public String getOuc_win_max() {
            return ouc_win_max;
        }

        public void setOuc_win_max(String ouc_win_max) {
            this.ouc_win_max = ouc_win_max;
        }

        public String getOuc_bonus1() {
            return ouc_bonus1;
        }

        public void setOuc_bonus1(String ouc_bonus1) {
            this.ouc_bonus1 = ouc_bonus1;
        }

        public String getOuc_bonus2() {
            return ouc_bonus2;
        }

        public void setOuc_bonus2(String ouc_bonus2) {
            this.ouc_bonus2 = ouc_bonus2;
        }

        public String getOuc_bonus3() {
            return ouc_bonus3;
        }

        public void setOuc_bonus3(String ouc_bonus3) {
            this.ouc_bonus3 = ouc_bonus3;
        }

        public String getOuc_amount() {
            return ouc_amount;
        }

        public void setOuc_amount(String ouc_amount) {
            this.ouc_amount = ouc_amount;
        }

        public String getCt_min() {
            return ct_min;
        }

        public void setCt_min(String ct_min) {
            this.ct_min = ct_min;
        }

        public String getCt_max() {
            return ct_max;
        }

        public void setCt_max(String ct_max) {
            this.ct_max = ct_max;
        }

        public String getCt_win() {
            return ct_win;
        }

        public void setCt_win(String ct_win) {
            this.ct_win = ct_win;
        }

        public String getCt_chance() {
            return ct_chance;
        }

        public void setCt_chance(String ct_chance) {
            this.ct_chance = ct_chance;
        }

        public String getCt_image() {
            return ct_image;
        }

        public void setCt_image(String ct_image) {
            this.ct_image = ct_image;
        }

        public String getRps_min() {
            return rps_min;
        }

        public void setRps_min(String rps_min) {
            this.rps_min = rps_min;
        }

        public String getRps_max() {
            return rps_max;
        }

        public String getRps_image() {
            return rps_image;
        }

        public void setRps_image(String rps_image) {
            this.rps_image = rps_image;
        }

        public String getGn_image() {
            return gn_image;
        }

        public void setGn_image(String gn_image) {
            this.gn_image = gn_image;
        }

        public String getSpin_image() {
            return spin_image;
        }

        public void setSpin_image(String spin_image) {
            this.spin_image = spin_image;
        }

        public void setRps_max(String rps_max) {
            this.rps_max = rps_max;
        }

        public String getRps_win() {
            return rps_win;
        }

        public void setRps_win(String rps_win) {
            this.rps_win = rps_win;
        }

        public String getRps_chance() {
            return rps_chance;
        }

        public void setRps_chance(String rps_chance) {
            this.rps_chance = rps_chance;
        }

        public String getGn_min() {
            return gn_min;
        }

        public void setGn_min(String gn_min) {
            this.gn_min = gn_min;
        }

        public String getGn_max() {
            return gn_max;
        }

        public void setGn_max(String gn_max) {
            this.gn_max = gn_max;
        }

        public String getGn_win() {
            return gn_win;
        }

        public void setGn_win(String gn_win) {
            this.gn_win = gn_win;
        }

        public String getGn_chance() {
            return gn_chance;
        }

        public void setGn_chance(String gn_chance) {
            this.gn_chance = gn_chance;
        }

        public String getSpin_min() {
            return spin_min;
        }

        public void setSpin_min(String spin_min) {
            this.spin_min = spin_min;
        }

        public String getSpin_max() {
            return spin_max;
        }

        public void setSpin_max(String spin_max) {
            this.spin_max = spin_max;
        }

        public String getSpin_win() {
            return spin_win;
        }

        public void setSpin_win(String spin_win) {
            this.spin_win = spin_win;
        }

        public String getSpin_chance() {
            return spin_chance;
        }

        public void setSpin_chance(String spin_chance) {
            this.spin_chance = spin_chance;
        }

        @Override
        public String toString() {
            return "ClassPojo [JSON_DATA = " + "rps_min='" + rps_min + '\'' + ", rps_max='" + rps_max + '\'' + ", rps_win='" + rps_win + '\'' + ", rps_chance='" + rps_chance + '\'' + ", rps_image='"+rps_image+'\''+", gn_min='" + gn_min + '\'' + ", gn_max='" + gn_max + '\'' + ", gn_win='" + gn_win + '\'' + ", gn_chance='" + gn_chance + '\'' + ", gn_image='" + gn_image + '\'' + ", spin_min='" + spin_min + '\'' + ", spin_max='" + spin_max + '\'' + ", spin_win='" + spin_win + '\'' + ", spin_chance='" + spin_chance + '\'' + ", spin_image='"+spin_image+'\''+']';
        }
    }

}
	