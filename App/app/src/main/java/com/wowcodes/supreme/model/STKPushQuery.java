/**
 * The `STKPushQuery` class is a model class that represents the response data for an STK push query in
 * a Java application.
 */
package com.wowcodes.supreme.model;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class STKPushQuery {
    @SerializedName("BusinessShortCode")
    private String businessShortCode;
    @SerializedName("Password")
    private String password;
    @SerializedName("Timestamp")
    private String timestamp;
    @SerializedName("CheckoutRequestID")
    private String checkoutRequestID;

    public STKPushQuery(String businessShortCode, String password, String timestamp, String checkoutRequestID) {
        this.businessShortCode = businessShortCode;
        this.password = password;
        this.timestamp = timestamp;
        this.checkoutRequestID = checkoutRequestID;
    }

    private ArrayList<Mpesa_Push_Response> JSON_DATA;
    public ArrayList<Mpesa_Push_Response> getJSON_DATA() {
        return JSON_DATA;
    }
    public void setJSON_DATA(ArrayList<Mpesa_Push_Response> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }
    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Mpesa_Push_Response {
        private String MerchantRequestID;
        private String CheckoutRequestID;
        private String ResultCode;
        private String ResultDesc;
        private String CallbackMetadata;
        public String getMerchantRequestID() {
            return MerchantRequestID;
        }

        public void setMerchantRequestID(String MerchantRequestId) {
            this.MerchantRequestID = MerchantRequestID;
        }

        public String getCheckoutRequestID() {
            return CheckoutRequestID;
        }

        public void setCheckoutRequestID(String CheckoutRequestID) {
            this.CheckoutRequestID = CheckoutRequestID;
        }

        public String getResultCode() {
            return ResultCode;
        }

        public void setResultCode(String ResultCode) {
            this.ResultCode = ResultCode;
        }

        public String getResultDesc() {
            return ResultDesc;
        }

        public void setResultDesc(String ResultDesc) {
            this.ResultDesc = ResultDesc;
        }

        public String getCallbackMetadata() {
            return CallbackMetadata;
        }

        public void setCallbackMetadata(String CallbackMetadata) {
            this.CallbackMetadata = CallbackMetadata;
        }
        @Override
        public String toString()
        {
            return "ClassPojo [MerchantRequestID = "+MerchantRequestID+", CheckoutRequestID = "+CheckoutRequestID+", ResultCode = "+ResultCode+", ResultDesc = "+ResultDesc+", CallbackMetadata = "+CallbackMetadata+"]";
        }

    }
}
