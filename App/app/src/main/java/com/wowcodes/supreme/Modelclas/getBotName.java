package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class getBotName {

    private ArrayList<getBotName.Get_names_Inner> JSON_DATA;

    public ArrayList<getBotName.Get_names_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<getBotName.Get_names_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Get_names_Inner {
        public String bot_name,bot_image;

        public String getBot_name() {
            return bot_name;
        }

        public void setBot_name(String bot_name) {
            this.bot_name = bot_name;
        }

        public String getBot_image() {
            return bot_image;
        }

        public void setBot_image(String bot_image) {
            this.bot_image = bot_image;
        }

        @Override
        public String toString() {
            return "Get_names_Inner{" +
                    "bot_name='" + bot_name + '\'' +
                    ", bot_image='" + bot_image + '\'' +
                    '}';
        }
    }
}
