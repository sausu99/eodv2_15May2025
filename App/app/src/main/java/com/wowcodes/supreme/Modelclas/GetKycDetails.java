package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class GetKycDetails {

    private ArrayList<GetKycDetails.Get_kycstatus_Inner> JSON_DATA;

    public ArrayList<GetKycDetails.Get_kycstatus_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<GetKycDetails.Get_kycstatus_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Get_kycstatus_Inner {
        public String kyc_status,id_firstname,id_lastname,id_number,id_type,id_country,dob,id_front,id_back;

        public String getId_firstname() {
            return id_firstname;
        }

        public void setId_firstname(String id_firstname) {
            this.id_firstname = id_firstname;
        }

        public String getId_lastname() {
            return id_lastname;
        }

        public void setId_lastname(String id_lastname) {
            this.id_lastname = id_lastname;
        }

        public String getId_number() {
            return id_number;
        }

        public void setId_number(String id_number) {
            this.id_number = id_number;
        }

        public String getId_type() {
            return id_type;
        }

        public void setId_type(String id_type) {
            this.id_type = id_type;
        }

        public String getId_country() {
            return id_country;
        }

        public void setId_country(String id_country) {
            this.id_country = id_country;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getId_front() {
            return id_front;
        }

        public void setId_front(String id_front) {
            this.id_front = id_front;
        }

        public String getId_back() {
            return id_back;
        }

        public void setId_back(String id_back) {
            this.id_back = id_back;
        }

        public String getKyc_status() {
            return kyc_status;
        }

        public void setKyc_status(String kyc_status) {
            this.kyc_status = kyc_status;
        }


        @Override
        public String toString() {
            return "Get_kycstatus_Inner{" +
                    "kyc_status='" + kyc_status + '\'' +
                    ", id_firstname='" + id_firstname + '\'' +
                    ", id_lastname='" + id_lastname + '\'' +
                    ", id_number='" + id_number + '\'' +
                    ", id_type='" + id_type + '\'' +
                    ", id_country='" + id_country + '\'' +
                    ", dob='" + dob + '\'' +
                    ", id_front='" + id_front + '\'' +
                    ", id_back='" + id_back + '\'' +
                    '}';
        }
    }
}
