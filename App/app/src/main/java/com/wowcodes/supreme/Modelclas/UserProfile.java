/**
 * The UserProfile class represents a user profile and contains inner class User_profile_Inner which
 * represents the details of the user.
 */
package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class UserProfile {
    private ArrayList<User_profile_Inner> JSON_DATA;


    public ArrayList<User_profile_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<User_profile_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString() {
        return "ClassPojo [JSON_DATA = " + JSON_DATA + "]";
    }

    public class User_profile_Inner {
        private String image;
        private String reward_coins;
        private String kyc_status;

        private String wallet;

        private String code;

        private String device_id;

        private String login_type;

        private String phone;
        private String password;

        private String name;

        private String id;

        private String email;

        private String network;
        private String ban;


        private String status;

        public String getBan() {
            return ban;
        }

        public void setBan(String ban) {
            this.ban = ban;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getWallet() {
            return wallet;
        }

        public void setWallet(String wallet) {
            this.wallet = wallet;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getLogin_type() {
            return login_type;
        }

        public void setLogin_type(String login_type) {
            this.login_type = login_type;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "ClassPojo [image = " + image + ", wallet = " + wallet + ", code = " + code + ", device_id = " + device_id + ", login_type = " + login_type + ", phone = " + phone + ", name = " + name + ", id = " + id + "," +
                    " email = " + email + ", " +
                    "network = " + network + ", " +
                    "ban = " + ban + ", " +
                    "status = " + status + "]";
        }

        public String getReward_coins() {
            return reward_coins;
        }

        public void setReward_coins(String reward_coins) {
            this.reward_coins = reward_coins;
        }

        public String getKyc_status() {
            return kyc_status;
        }

        public void setKyc_status(String kyc_status) {
            this.kyc_status = kyc_status;
        }
    }


}
			
	