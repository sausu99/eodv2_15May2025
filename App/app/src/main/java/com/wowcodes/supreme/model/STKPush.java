/**
 * The `STKPush` class is a model class that represents a request for a Safaricom STK push transaction,
 * including various parameters and a nested class for the response.
 */
package com.wowcodes.supreme.model;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class STKPush {
    @SerializedName("BusinessShortCode")
    private String businessShortCode;
    @SerializedName("Password")
    private String password;
    @SerializedName("Timestamp")
    private String timestamp;
    @SerializedName("TransactionType")
    private String transactionType;
    @SerializedName("Amount")
    private String amount;
    @SerializedName("PartyA")
    private String partyA;
    @SerializedName("PartyB")
    private String partyB;
    @SerializedName("PhoneNumber")
    private String phoneNumber;
    @SerializedName("CallBackURL")
    private String callBackURL;
    @SerializedName("AccountReference")
    private String accountReference;
    @SerializedName("TransactionDesc")
    private String transactionDesc;

    public STKPush(String businessShortCode, String password, String timestamp, String transactionType,
                   String amount, String partyA, String partyB, String phoneNumber, String callBackURL,
                   String accountReference, String transactionDesc) {
        this.businessShortCode = businessShortCode;
        this.password = password;
        this.timestamp = timestamp;
        this.transactionType = transactionType;
        this.amount = amount;
        this.partyA = partyA;
        this.partyB = partyB;
        this.phoneNumber = phoneNumber;
        this.callBackURL = callBackURL;
        this.accountReference = accountReference;
        this.transactionDesc = transactionDesc;
    }

    public String getAccountReference() {
        return accountReference;
    }
    private ArrayList<Mpesa_Push_Response> JSON_DATA;


    public ArrayList<Mpesa_Push_Response> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<Mpesa_Push_Response> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }
    public class Mpesa_Push_Response {
        private String MerchantRequestID;

        private String CheckoutRequestID;

        private String ResponseCode;

        private String ResponseDescription;

        private String CustomerMessage;
        public String getMerchantRequestID() {
            return MerchantRequestID;
        }

        public void setMerchantRequestID(String ban) {
            this.MerchantRequestID = MerchantRequestID;
        }

        public String getCheckoutRequestID() {
            return CheckoutRequestID;
        }

        public void setCheckoutRequestID(String CheckoutRequestID) {
            this.CheckoutRequestID = CheckoutRequestID;
        }

        public String getResponseCode() {
            return ResponseCode;
        }

        public void setResponseCode(String ResponseCode) {
            this.ResponseCode = ResponseCode;
        }

        public String getResponseDescription() {
            return ResponseDescription;
        }

        public void setResponseDescription(String ResponseDescription) {
            this.ResponseDescription = ResponseDescription;
        }

        public String getCustomerMessage() {
            return CustomerMessage;
        }

        public void setCustomerMessage(String CustomerMessage) {
            this.CustomerMessage = CustomerMessage;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [MerchantRequestID = "+MerchantRequestID+", CheckoutRequestID = "+CheckoutRequestID+", ResponseCode = "+ResponseCode+" , ResponseDescription = "+ResponseDescription+", CustomerMessage = "+CustomerMessage+"]";
        }

    }
}