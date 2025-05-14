/**
 * The AllBidderInner class is a model class in Java that represents a bid item with various properties
 * such as image, status, name, amount, start time, minimum bid, user bids, date, type, description,
 * price,buy, end time, ID, all bids, total bids, maximum bid, bid increment, and time increment.
 */
package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class AllBidderInner {


    private String o_image;

    public String getO_qty() {
        return o_qty;
    }

    public void setO_qty(String o_qty) {
        this.o_qty = o_qty;
    }

    private String o_qty;



    private String lbd_amount,rbd_amount,hbd_amount,dbd_amount;
    private String o_image1;
    private String o_image2;
    private String o_image3;
    private String o_image4;

    private String o_status;

    private String o_name;
    private String seller_name;
    private String seller_about;
    private String seller_ratting;
    private String seller_image;

    private String seller_link;
    private String seller_join_date;

    public String getSeller_join_date() {
        return seller_join_date;
    }

    public void setSeller_join_date(String seller_join_date) {
        this.seller_join_date = seller_join_date;
    }

    public String getSeller_link() {
        return seller_link;
    }

    public void setSeller_link(String seller_link) {
        this.seller_link = seller_link;
    }

    public String getSeller_about() {
        return seller_about;
    }

    public void setSeller_about(String seller_about) {
        this.seller_about = seller_about;
    }

    public String getSeller_ratting() {
        return seller_ratting;
    }

    public void setSeller_ratting(String seller_ratting) {
        this.seller_ratting = seller_ratting;
    }

    public String getSeller_image() {
        return seller_image;
    }

    public void setSeller_image(String seller_image) {
        this.seller_image = seller_image;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    private String o_amount;
    private String total_users;

    private String o_stime;

    private String o_min;

    private ArrayList<UserBid> user_bid;

    private String o_date;
    private String o_edate;
    private String won_id;

    private String o_type;

    private String o_desc;

    private String o_price;

    private String o_buy;

    private String o_etime;
    private ArrayList<UserTicket> user_ticket;
    private ArrayList<GetCategories.JSONDATum> similar_items;

    public ArrayList<UserTicket> getUser_ticket() {
        return user_ticket;
    }

    public void setUser_ticket(ArrayList<UserTicket> user_ticket) {
        this.user_ticket = user_ticket;
    }

    private String won_name;

    private String o_id;

    private ArrayList<AllBid> all_bid;

    private String total_bids;

    private String o_max;

    private String bid_increment;

    private String time_increment;

    public String getWinning_bid() {
        return winning_bid;
    }

    public void setWinning_bid(String winning_bid) {
        this.winning_bid = winning_bid;
    }

    private String wishlist_status;
    private String seller_id;
    private String item_id;
    private String winning_bid;


    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getWishlist_status() {
        return wishlist_status;
    }

    public void setWishlist_status(String wishlist_status) {
        this.wishlist_status = wishlist_status;
    }

    public String getBid_increment() {
        return bid_increment;
    }

    public void setBid_increment(String bid_increment) {
        this.bid_increment = bid_increment;
    }

    public String getTime_increment() {
        return time_increment;
    }

    public void setTime_increment(String time_increment) {
        this.o_edate = time_increment;
    }

    public String getO_edate() {
        return o_edate;
    }

    public void setO_edate(String o_edate) {
        this.o_edate = o_edate;
    }

    public String getO_image ()
    {
        return o_image;
    }

    public void setO_image (String o_image)
    {
        this.o_image = o_image;
    }

    public String getO_status ()
    {
        return o_status;
    }

    public void setO_status (String o_status)
    {
        this.o_status = o_status;
    }

    public String getO_name ()
    {
        return o_name;
    }

    public void setO_name (String o_name)
    {
        this.o_name = o_name;
    }


    public String getWon_id() {
        return won_id;
    }

    public void setWon_id(String won_id) {
        this.won_id = won_id;
    }
    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }


    public String getO_amount ()
    {
        return o_amount;
    }


    public String getWon_name() {
        return won_name;
    }

    public void setWon_name(String won_name) {
        this.won_name = won_name;
    }

    public void setO_amount (String o_amount)
    {
        this.o_amount = o_amount;
    }

    public String getO_stime ()
    {
        return o_stime;
    }

    public void setO_stime (String o_stime)
    {
        this.o_stime = o_stime;
    }

    public String getO_min ()
    {
        return o_min;
    }

    public ArrayList<UserBid> getUser_bid() {
        return user_bid;
    }

    public void setUser_bid(ArrayList<UserBid> user_bid) {
        this.user_bid = user_bid;
    }

    public ArrayList<AllBid> getAll_bid() {
        return all_bid;
    }

    public void setAll_bid(ArrayList<AllBid> all_bid) {
        this.all_bid = all_bid;
    }

    public void setO_min (String o_min)
    {
        this.o_min = o_min;
    }


    public String getTotal_users() {
        return total_users;
    }

    public void setTotal_users(String total_users) {
        this.total_users = total_users;
    }

    public String getO_date ()
    {
        return o_date;
    }

    public void setO_date (String o_date)
    {
        this.o_date = o_date;
    }

    public String getO_type ()
    {
        return o_type;
    }

    public void setO_type (String o_type)
    {
        this.o_type = o_type;
    }

    public String getO_desc ()
    {
        return o_desc;
    }

    public void setO_desc (String o_desc)
    {
        this.o_desc = o_desc;
    }

    public String getO_price ()
    {
        return o_price;
    }

    public void setO_price (String o_price)
    {
        this.o_price = o_price;
    }

    public String getO_buy ()
    {
        return o_buy;
    }

    public void setO_buy (String o_buy)
    {
        this.o_buy = o_buy;
    }

    public String getO_etime ()
    {
        return o_etime;
    }

    public void setO_etime (String o_etime)
    {
        this.o_etime = o_etime;
    }

    public String getO_id ()
    {
        return o_id;
    }

    public void setO_id (String o_id)
    {
        this.o_id = o_id;
    }


    public String getTotal_bids ()
    {
        return total_bids;
    }

    public void setTotal_bids (String total_bids)
    {
        this.total_bids = total_bids;
    }

    public String getO_max ()
    {
        return o_max;
    }

    public void setO_max (String o_max)
    {
        this.o_max = o_max;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [o_image = "+o_image+", o_status = "+o_status+", o_name = "+o_name+", o_amount = "+o_amount+", o_stime = "+o_stime+", o_min = "+o_min+", user_bid = "+user_bid+", o_date = "+o_date+", o_type = "+o_type+", o_desc = "+o_desc+", o_price = "+o_price+", o_buy = "+o_buy+", o_etime = "+o_etime+", o_id = "+o_id+", seller_id = "+seller_id+", all_bid = "+all_bid+", total_bids = "+total_bids+", o_max = "+o_max+", bid_increment = "+bid_increment+", time_increment = "+time_increment+"]";
    }

    public String getO_image1() {
        return o_image1;
    }

    public void setO_image1(String o_image1) {
        this.o_image1 = o_image1;
    }

    public String getO_image2() {
        return o_image2;
    }

    public void setO_image2(String o_image2) {
        this.o_image2 = o_image2;
    }

    public String getO_image3() {
        return o_image3;
    }

    public void setO_image3(String o_image3) {
        this.o_image3 = o_image3;
    }

    public String getO_image4() {
        return o_image4;
    }

    public void setO_image4(String o_image4) {
        this.o_image4 = o_image4;
    }

    public ArrayList<GetCategories.JSONDATum> getSimiliar_items() {
        return similar_items;
    }

    public void setSimiliar_items(ArrayList<GetCategories.JSONDATum> similiar_items) {
        this.similar_items = similiar_items;
    }
}
