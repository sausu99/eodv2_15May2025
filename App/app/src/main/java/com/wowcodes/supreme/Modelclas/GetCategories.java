/**
 * The GetCategories class is a model class in Java that represents the JSON response for retrieving
 * categories.
 */
package com.wowcodes.supreme.Modelclas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCategories {

    @SerializedName("JSON_DATA")
    @Expose
    private List<JSONDATum> jsonData = null;

    public List<JSONDATum> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JSONDATum> jsonData) {
        this.jsonData = jsonData;
    }

    public static class JSONDATum {

        @SerializedName("c_id")
        @Expose
        private String cId;
        @SerializedName("c_name")
        @Expose
        private String cName;
        @SerializedName("c_image")
        @Expose
        private String cImage;
        @SerializedName("c_view")
        @Expose
        private String cView;
        @SerializedName("o_id")
        @Expose
        private String oId;

        @SerializedName("o_color")
        @Expose
        private String oColor;
        @SerializedName("o_name")
        @Expose
        private String oName;
        @SerializedName("o_image")
        @Expose
        private String oImage;

        @SerializedName("o_image1")
        @Expose
        private String oImage1;

        @SerializedName("o_image2")
        @Expose
        private String oImage2;

        @SerializedName("o_image3")
        @Expose
        private String oImage3;

        @SerializedName("o_image4")
        @Expose
        private String oImage4;
        @SerializedName("o_desc")
        @Expose
        private String oDesc;
        @SerializedName("o_date")
        @Expose
        private String oDate;
        @SerializedName("o_edate")
        @Expose
        private String oEdate;
        @SerializedName("o_stime")
        @Expose
        private String oStime;
        @SerializedName("o_etime")
        @Expose
        private String oEtime;
        @SerializedName("o_amount")
        @Expose
        private String oAmount;
        @SerializedName("o_type")
        @Expose
        private String oType;
        @SerializedName("o_min")
        @Expose
        private String oMin;
        @SerializedName("o_max")
        @Expose
        private String oMax;
        @SerializedName("o_price")
        @Expose
        private String oPrice;


        @SerializedName("o_winners")
        @Expose
        private String oWinners;

        @SerializedName("o_oimage")
        @Expose
        private String oOimage;
        @SerializedName("o_currency")
        @Expose
        private String oCurrency;
        @SerializedName("o_stars")
        @Expose
        private String oStars;
        @SerializedName("o_membership")
        @Expose
        private String oMembership;
        @SerializedName("o_qty")
        @Expose
        private String oQty;
        @SerializedName("o_cat")
        @Expose
        private Object oCat;
        @SerializedName("o_umax")
        @Expose
        private String oUmax;
        @SerializedName("o_ulimit")
        @Expose
        private String oUlimit;
        @SerializedName("o_status")
        @Expose
        private String oStatus;
        @SerializedName("c_status")
        @Expose
        private String cStatus;
        @SerializedName("o_link")
        @Expose
        private String oLink;
        @SerializedName("c_desc")
        @Expose
        private String cDesc;
        @SerializedName("c_color")
        @Expose
        private String cColor;

        @SerializedName("total_bids")
        @Expose
        private String totalbids;

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("city_id")
        @Expose
        private String city_id;
        @SerializedName("item_id")
        @Expose
        private String item_id;
        @SerializedName("wishlist_status")
        @Expose
        private String wishlist_status;

        public String getWishlist_status() {
            return wishlist_status;
        }

        public void setWishlist_status(String wishlist_status) {
            this.wishlist_status = wishlist_status;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getoImage3() {
            return oImage3;
        }

        public void setoImage3(String oImage3) {
            this.oImage3 = oImage3;
        }

        public String getoImage4() {
            return oImage4;
        }

        public void setoImage4(String oImage4) {
            this.oImage4 = oImage4;
        }

        public void setTotalbids(String totalbids) {
            this.totalbids = totalbids;
        }

        public String getoImage2() {
            return oImage2;
        }

        public void setoImage2(String oImage2) {
            this.oImage2 = oImage2;
        }

        public String getCity_id() {
            return city_id;
        }

        public String getcId() {
            return cId;
        }
        public String getTotalbids() {
            return totalbids;
        }

        public void setcId(String cId) {
            this.cId = cId;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getcName() {
            return cName;
        }

        public void setcName(String cName) {
            this.cName = cName;
        }

        public String getcImage() {
            return cImage;
        }

        public void setcImage(String cImage) {
            this.cImage = cImage;
        }

        public String getcView() {
            return cView;
        }

        public void setcView(String cView) {
            this.cView = cView;
        }

        public String getoId() {
            return oId;
        }

        public void setoId(String oId) {
            this.oId = oId;
        }

        public String getoName() {
            return oName;
        }

        public void setoName(String oName) {
            this.oName = oName;
        }

        public String getoImage() {
            return oImage;
        }

        public void setoImage(String oImage) {
            this.oImage = oImage;
        }

        public String getoDesc() {
            return oDesc;
        }

        public void setoDesc(String oDesc) {
            this.oDesc = oDesc;
        }

        public String getoDate() {
            return oDate;
        }

        public void setoDate(String oDate) {
            this.oDate = oDate;
        }

        public String getoEdate() {
            return oEdate;
        }

        public void setoEdate(String oEdate) {
            this.oEdate = oEdate;
        }

        public String getoStime() {
            return oStime;
        }

        public void setoStime(String oStime) {
            this.oStime = oStime;
        }

        public String getoEtime() {
            return oEtime;
        }

        public void setoEtime(String oEtime) {
            this.oEtime = oEtime;
        }

        public String getoAmount() {
            return oAmount;
        }

        public void setoAmount(String oAmount) {
            this.oAmount = oAmount;
        }

        public String getoType() {
            return oType;
        }

        public void setoType(String oType) {
            this.oType = oType;
        }

        public String getoMin() {
            return oMin;
        }

        public void setoMin(String oMin) {
            this.oMin = oMin;
        }

        public String getoMax() {
            return oMax;
        }

        public void setoMax(String oMax) {
            this.oMax = oMax;
        }

        public String getoPrice() {
            return oPrice;
        }

        public void setoPrice(String oPrice) {
            this.oPrice = oPrice;
        }

        public String getoOimage() {
            return oOimage;
        }

        public void setoOimage(String oOimage) {
            this.oOimage = oOimage;
        }

        public String getoCurrency() {
            return oCurrency;
        }

        public void setoCurrency(String oCurrency) {
            this.oCurrency = oCurrency;
        }

        public String getoStars() {
            return oStars;
        }

        public void setoStars(String oStars) {
            this.oStars = oStars;
        }

        public String getoMembership() {
            return oMembership;
        }

        public void setoMembership(String oMembership) {
            this.oMembership = oMembership;
        }

        public String getoQty() {
            return oQty;
        }

        public void setoQty(String oQty) {
            this.oQty = oQty;
        }

        public Object getoCat() {
            return oCat;
        }

        public void setoCat(Object oCat) {
            this.oCat = oCat;
        }

        public String getoUmax() {
            return oUmax;
        }

        public void setoUmax(String oUmax) {
            this.oUmax = oUmax;
        }

        public String getoUlimit() {
            return oUlimit;
        }

        public void setoUlimit(String oUlimit) {
            this.oUlimit = oUlimit;
        }

        public String getoStatus() {
            return oStatus;
        }

        public void setoStatus(String oStatus) {
            this.oStatus = oStatus;
        }

        public String getcStatus() {
            return cStatus;
        }

        public void setcStatus(String cStatus) {
            this.cStatus = cStatus;
        }

        public String getoLink() {
            return oLink;
        }

        public void setoLink(String oLink) {
            this.oLink = oLink;
        }

        public String getcDesc() {
            return cDesc;
        }

        public void setcDesc(String cDesc) {
            this.cDesc = cDesc;
        }

        public String getcColor() {
            return cColor;
        }

        public void setcColor(String cColor) {
            this.cColor = cColor;
        }

        public String getId() {
            return id;
        }

        public void setId(String cColor) {
            this.id = id;
        }

        public String getoWinners() {
            return oWinners;
        }

        public void setoWinners(String oWinners) {
            this.oWinners = oWinners;
        }

        public String getoImage1() {
            return oImage1;
        }

        public void setoImage1(String oImage1) {
            this.oImage1 = oImage1;
        }

        public String getoColor() {
            return oColor;
        }

        public void setoColor(String oColor) {
            this.oColor = oColor;
        }
    }
}
