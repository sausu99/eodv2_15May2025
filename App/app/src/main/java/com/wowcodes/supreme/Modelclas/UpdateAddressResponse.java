package com.wowcodes.supreme.Modelclas;

import java.util.List;

public class UpdateAddressResponse {
    private List<JSONData> JSON_DATA;

    public List<JSONData> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(List<JSONData> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    public static class JSONData {
        private String msg;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
