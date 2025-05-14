/**
 * The above class is a Java model class that represents the structure of a JSON response for
 * retrieving redeemable items.
 */
package com.wowcodes.supreme.Modelclas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetSellerDetails {

    @SerializedName("JSON_DATA")
    @Expose
    private List<JSONDATum> jsonData = null;

    public List<JSONDATum> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JSONDATum> jsonData) {
        this.jsonData = jsonData;
    }

    public class JSONDATum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("about")
        @Expose
        private String about;
        @SerializedName("join_date")
        @Expose
        private String join_date;
        @SerializedName("link")
        @Expose
        private String link;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("ratting")
        @Expose
        private String ratting;
        public String getRating() {return ratting;}
        public void setRating(String ratting) {this.ratting = ratting;}
        public String getId() {
            return id;
        }
        public void setId(String cId) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.id = name;
        }
        public String getAbout() {
            return about;
        }
        public void setAbout(String about) {
            this.about = about;
        }

        public String getLink() {
            return link;
        }
        public void setLink(String link) {
            this.link = link;
        }

        public String getImage() {
            return image;
        }
        public void setImage(String image) {
            this.image = image;
        }

        public String getJoin_date() {
            return join_date;
        }

        public void setJoin_date(String join_date) {
            this.join_date = join_date;
        }
    }
}
