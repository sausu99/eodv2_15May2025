/**
 * The ItemCategory class represents a category of items and contains information such as category ID,
 * title, color, description, image URL, and a list of items belonging to that category.
 */
package com.wowcodes.supreme.Modelclas;

import java.util.List;

public class ItemRedeem {
    private String catId;
    private String title;

    private String color;
    private String description;
    private String imageUrl;
    private List<GetRedeem.JSONDATum> items;

    public ItemRedeem(String catId, String title, String color, String description, String imageUrl, List<GetRedeem.JSONDATum> items) {
        this.catId = catId;
        this.title = title;
        this.color = color;
        this.description = description;
        this.imageUrl = imageUrl;
        this.items = items;
    }
    public String getCatId() {
        return catId;
    }
    public String getTitle() {
        return title;
    }
    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<GetRedeem.JSONDATum> getItems() {
        return items;
    }
}
