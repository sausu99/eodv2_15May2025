/**
 * The `AllBid` class is a model class that represents a bid in a prize auction, containing various
 * properties such as image, user ID, bid amount, name, order ID, bid ID, bid date, and bid value.
 */
package com.wowcodes.supreme.Modelclas;

public class AllBid {

    private String image;

    private String u_id;

    private String bd_amount;
    private String Total_amount;

    private String name;

    private String o_id;

    private String bd_id;

    private String bd_date;

    private String bd_value;

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    public String getU_id ()
    {
        return u_id;
    }

    public void setU_id (String u_id)
    {
        this.u_id = u_id;
    }

    public String getBd_amount ()
    {
        return bd_amount;
    }

    public void setBd_amount (String bd_amount)
    {
        this.bd_amount = bd_amount;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getO_id ()
    {
        return o_id;
    }

    public void setO_id (String o_id)
    {
        this.o_id = o_id;
    }

    public String getBd_id ()
    {
        return bd_id;
    }

    public void setBd_id (String bd_id)
    {
        this.bd_id = bd_id;
    }


    public String getTotal_amount() {
        return Total_amount;
    }

    public void setTotal_amount(String total_amount) {
        Total_amount = total_amount;
    }

    public String getBd_date ()
    {
        return bd_date;
    }

    public void setBd_date (String bd_date)
    {
        this.bd_date = bd_date;
    }

    public String getBd_value ()
    {
        return bd_value;
    }

    public void setBd_value (String bd_value)
    {
        this.bd_value = bd_value;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [image = "+image+", u_id = "+u_id+", bd_amount = "+bd_amount+", name = "+name+", o_id = "+o_id+", bd_id = "+bd_id+", bd_date = "+bd_date+", bd_value = "+bd_value+"]";
    }

}
