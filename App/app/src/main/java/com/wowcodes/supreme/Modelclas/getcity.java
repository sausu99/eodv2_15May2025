/**
 * The `getcity` class is a model class that represents a list of cities with their respective IDs,
 * names, and images.
 */

package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class getcity
{
    private ArrayList<get_city_Inner> JSON_DATA;

    public ArrayList<get_city_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<get_city_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public static class get_city_Inner{


        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
        private String city_id;

        public String getCity_id ()
        {
            return city_id;
        }

        public void setCity_id (String city_id)
        {
            this.city_id = city_id;
        }

        private String city_name;

        public String getCity_name ()
        {
            return city_name;
        }

        public void setCity_name (String city_name)
        {
            this.city_name = city_name;
        }

        private String city_image;

        public String getCity_image ()
        {
            return city_image;
        }

        public void setCity_image (String city_image)
        {
            this.city_image = city_image;
        }


        private String city_bw_image;

        public String getCity_bw_image ()
        {
            return city_bw_image;
        }

        public void setCity_bw_image (String city_bw_image)
        {
            this.city_bw_image = city_bw_image;
        }
        @Override
        public String toString()
        {
            return "ClassPojo [city_id = "+city_id+", city_name = "+city_name+", city_image = "+city_image+"]";
        }
    }
}
