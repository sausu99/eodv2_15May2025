package com.wowcodes.supreme.Modelclas;

import java.util.List;

public class WishlistItem {
    private List<Item> JSON_DATA;

    // Getters and Setters
    public List<Item> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(List<Item> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    public static class Item {
        private String item_id;
        private String o_name;
        private String o_image;
        private List<AvailableItem> available_items;

        // Getters and Setters
        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getO_name() {
            return o_name;
        }

        public void setO_name(String o_name) {
            this.o_name = o_name;
        }

        public String getO_image() {
            return o_image;
        }

        public void setO_image(String o_image) {
            this.o_image = o_image;
        }

        public List<AvailableItem> getAvailable_items() {
            return available_items;
        }

        public void setAvailable_items(List<AvailableItem> available_items) {
            this.available_items = available_items;
        }
    }

    public static class AvailableItem {
        private String o_id;
        private String o_type;

        // Getters and Setters
        public String getO_id() {
            return o_id;
        }

        public void setO_id(String o_id) {
            this.o_id = o_id;
        }

        public String getO_type() {
            return o_type;
        }

        public void setO_type(String o_type) {
            this.o_type = o_type;
        }
    }
}
