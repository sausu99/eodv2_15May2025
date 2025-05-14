package com.wowcodes.supreme.Modelclas;


import java.util.ArrayList;

public  class AddTktResponse {
    private ArrayList<AddTktResponse.add_tktinner> JSON_DATA;

    public ArrayList<AddTktResponse.add_tktinner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<AddTktResponse.add_tktinner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }


    public class add_tktinner{
        private String msg;
        private String ticket_id;
        private String success;

        public String getTicket_id() {
            return ticket_id;
        }

        public void setTicket_id(String ticket_id) {
            this.ticket_id = ticket_id;
        }

        // Getters and setters
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
    }}
}
