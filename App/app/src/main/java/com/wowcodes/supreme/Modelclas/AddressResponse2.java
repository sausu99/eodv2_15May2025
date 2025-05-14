package com.wowcodes.supreme.Modelclas;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class AddressResponse2 {

    @SerializedName("JSON_DATA")
    private List<Address> addresses;

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public static class Address {

        @SerializedName("address_id")
        private String addressId;

        @SerializedName("u_id")
        private String userId;

        @SerializedName("address_line1")
        private String addressLine1;

        @SerializedName("address_line2")
        private String addressLine2;

        @SerializedName("city")
        private String city;

        @SerializedName("state")
        private String state;

        @SerializedName("postal_code")
        private String postalCode;

        @SerializedName("country")
        private String country;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("updated_at")
        private String updatedAt;
        @SerializedName("address_type")
        private String address_type;
        @SerializedName("nickname")
        private String nickname;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getAddress_type() {
            return address_type;
        }

        public void setAddress_type(String address_type) {
            this.address_type = address_type;
        }
// Getters and setters for each field

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAddressLine1() {
            return addressLine1;
        }

        public void setAddressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
        }

        public String getAddressLine2() {
            return addressLine2;
        }

        public void setAddressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getPostalCode() {
            return postalCode;
        }
    }
}




