package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class GetNotification {

    private ArrayList<GetNotification.Get_notification_Inner> JSON_DATA;

    public ArrayList<GetNotification.Get_notification_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<GetNotification.Get_notification_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString() { return "ClassPojo [JSON_DATA = "+JSON_DATA+"]"; }

    public static class Get_notification_Inner {
        public String id,tittle,body,image,link,action,status,time;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTittle() {
            return tittle;
        }

        public void setTittle(String tittle) {
            this.tittle = tittle;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Get_notification_Inner{" +
                    "id='" + id + '\'' +
                    ", title='" + tittle + '\'' +
                    ", body='" + body + '\'' +
                    ", image='" + image + '\'' +
                    ", link='" + link + '\'' +
                    ", action='" + action + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }
    }
}
