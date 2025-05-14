/**
 * The RegisterModel class is a model class that represents JSON data for user registration, containing
 * various properties such as name, email, password, phone number, etc.
 */
package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class RegisterModel {
    private ArrayList<Register_model_Inner> JSON_DATA;

    public ArrayList<Register_model_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<Register_model_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString() {
        return "ClassPojo [JSON_DATA = " + JSON_DATA + "]";
    }

    public class Register_model_Inner {

        private String msg;

        private String image;

        private String wallet;

        private String code;

        private String refferal_code;

        private String network;

        private String password;

        private String country_code;
        private String phone;

        private String success;

        private String name;

        private String id;

        private String email;

        private String status;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getImage() {
            return image;
        }

        public String getCountry_code() { return country_code; }

        public void setCountry_code(String country_code) { this.country_code = country_code; }

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

        public String getRefferal_code() {
            return refferal_code;
        }

        public void setRefferal_code(String refferal_code) {
            this.refferal_code = refferal_code;
        }

        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "ClassPojo [msg = " + msg + ", image = " + image + ", wallet = " + wallet + ", code = " + code + ", refferal_code = " + refferal_code + ", network = " + network + ", password = " + password + ", country_code = "+country_code+", phone = " + phone + ", success = " + success + ", name = " + name + ", id = " + id + ", email = " + email + ", status = " + status + "]";
        }
    }
}
	