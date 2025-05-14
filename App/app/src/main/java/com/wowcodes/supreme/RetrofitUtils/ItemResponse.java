/**
 * The ItemResponse class is a Java class that represents the response from an API call, containing
 * information about an item.
 */
package com.wowcodes.supreme.RetrofitUtils;



public class ItemResponse {

    private Item item;

    public Item getItem() {
        return item ;
    }

    public static class Item{

        private  String totalItem;
        private String maxItem;

        public String getTotalItem() {
            return totalItem;
        }

        public String getMaxItem() {
            return maxItem;
        }
    }


}
