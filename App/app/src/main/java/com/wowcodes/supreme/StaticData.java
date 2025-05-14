package com.wowcodes.supreme;

import com.wowcodes.supreme.Modelclas.GetCategories;
import com.wowcodes.supreme.Modelclas.GetCoin;
import com.wowcodes.supreme.Modelclas.GetOffersWinner;
import com.wowcodes.supreme.Modelclas.GetRedeem;

import java.util.ArrayList;
import java.util.List;

public class StaticData {
    public static List<GetCategories.JSONDATum> livefragmentList = new ArrayList<>();
    public static List<GetCategories.JSONDATum> upcomingfragmentList = new ArrayList<>();
    public static ArrayList<GetOffersWinner.Get_offers_winner_Inner> winnerfragmentList=new ArrayList<>();
    public static ArrayList<GetCoin.Get_Coin_Inner> coinfragmentList=new ArrayList<>();
    public static List<GetCategories.JSONDATum> shopfragmentList = new ArrayList<>();
    public static ArrayList<String> userProfileList = new ArrayList<>();
    public static List<GetRedeem.JSONDATum> redeemlist = new ArrayList<>();

}
