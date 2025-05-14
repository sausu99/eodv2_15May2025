package com.wowcodes.supreme.Activity;

public class referralCard {
    String name,email,userid,bonus;
    public referralCard(String name,String email,String userid,String bonus){
        this.name=name;
        this.email=email;
        this.userid=userid;
        this.bonus=bonus;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }
}
