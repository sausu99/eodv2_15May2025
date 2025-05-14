
package com.wowcodes.supreme.Modelclas;

public class AllReviewer{



        private String user_image;
        private String user;
        private String reviewed_on;
        private String rating;
        private String review;

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getReviewed_on() {
        return reviewed_on;
    }

    public void setReviewed_on(String reviewed_on) {
        this.reviewed_on = reviewed_on;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public String toString() {
        return "AllReviewer{" +
                "user_image='" + user_image + '\'' +
                ", user='" + user + '\'' +
                ", reviewd_on='" + reviewed_on + '\'' +
                ", rating='" + rating + '\'' +
                ", review='" + review + '\'' +
                '}';
    }}


	