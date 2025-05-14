/**
 * The CategoryHorizontal class represents a category with a name and an image.
 */
package com.wowcodes.supreme.Modelclas;

public class CategoryHorizontal {
    private String name;
    private String image;
    private String id;
    private String type;
    private String otype;

    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
