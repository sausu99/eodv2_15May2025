package com.wowcodes.supreme.Modelclas;

import java.util.List;

public class WishlistResponse {
    private List<WishlistMessage> JSON_DATA;

    public List<WishlistMessage> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(List<WishlistMessage> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString() {
        return "WishlistResponse{" +
                "JSON_DATA=" + JSON_DATA +
                '}';
    }

    public static class WishlistMessage {
        private String msg;
        private String success;

        public WishlistMessage(String msg, String success) {
            this.msg = msg;
            this.success = success;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        @Override
        public String toString() {
            return "WishlistMessage{" +
                    "msg='" + msg + '\'' +
                    ", success='" + success + '\'' +
                    '}';
        }
    }
}
