/**
 * The ChargeResponse class is a Java class that represents the response received from a charge API,
 * containing information about the charge, customer, additional information, and tax ID.
 */
package com.wowcodes.supreme.RetrofitUtils;

import java.util.List;

public class ChargeResponse {
    private Charge charge;

    public Charge getCharge() {
        return charge;
    }


    public static class Charge {
        private String status;
        private Customer customer;
        private int value;
        private String comment;
        private String correlationID;
        private String paymentLinkID;
        private String paymentLinkUrl;
        private String qrCodeImage;
        private int expiresIn;
        private String expiresDate;
        private String createdAt;
        private String updatedAt;
        private String brCode;
        private List<AdditionalInfo> additionalInfo;

        public String getStatus() {
            return status;
        }



        public Customer getCustomer() {
            return customer;
        }



        public int getValue() {
            return value;
        }



        public String getComment() {
            return comment;
        }



        public String getCorrelationID() {
            return correlationID;
        }



        public String getPaymentLinkID() {
            return paymentLinkID;
        }



        public String getPaymentLinkUrl() {
            return paymentLinkUrl;
        }



        public String getQrCodeImage() {
            return qrCodeImage;
        }


        public int getExpiresIn() {
            return expiresIn;
        }


        public String getExpiresDate() {
            return expiresDate;
        }



        public String getCreatedAt() {
            return createdAt;
        }


        public String getUpdatedAt() {
            return updatedAt;
        }



        public String getBrCode() {
            return brCode;
        }



        public List<AdditionalInfo> getAdditionalInfo() {
            return additionalInfo;
        }


    }

    public static class Customer {
        private String name;
        private String email;
        private String phone;
        private TaxID taxID;

        public String getName() {
            return name;
        }



        public String getEmail() {
            return email;
        }



        public String getPhone() {
            return phone;
        }



        public TaxID getTaxID() {
            return taxID;
        }


    }

    public static class TaxID {
        private String taxID;
        private String type;

        public String getTaxID() {
            return taxID;
        }


        public String getType() {
            return type;
        }


    }

    public static class AdditionalInfo {
        private String key;
        private String value;

        public String getKey() {
            return key;
        }



        public String getValue() {
            return value;
        }


    }
}

