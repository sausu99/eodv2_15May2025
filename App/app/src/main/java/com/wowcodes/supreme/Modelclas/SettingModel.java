/**
 * The SettingModel class is a model class that represents the settings data for a prize app, including
 * various attributes such as app name, logo, contact information, ad IDs, and more.
 */
package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class SettingModel
{
    private ArrayList<Setting_model_Inner> JSON_DATA;

    public ArrayList<Setting_model_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<Setting_model_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Setting_model_Inner {



            private String publisher_id;
            private String otp_system;
            public String demo_access;

        public String getDemo_access() {
            return demo_access;
        }

        public void setDemo_access(String demo_access) {
            this.demo_access = demo_access;
        }

        private String support_email;
            private String ads_reward;

            private String app_contact;

            private String how_to_play;

            private String app_version;

            private String app_developed_by;

            private String app_author;

            private String interstital_ad;

            private String interstital_ad_id;

            private String app_name;

            private String app_logo;

            private String app_privacy_policy;

            private String app_email;

            private String about_us;

            private String banner_ad_id;

            private String banner_ad;

            private String app_website;

            private String app_description;

            public String getPublisher_id ()
            {
                return publisher_id;
            }

            public void setPublisher_id (String publisher_id)
            {
                this.publisher_id = publisher_id;
            }

            public String getApp_contact ()
            {
                return app_contact;
            }

            public void setApp_contact (String app_contact)
            {
                this.app_contact = app_contact;
            }

            public String getHow_to_play ()
            {
                return how_to_play;
            }

            public void setHow_to_play (String how_to_play)
            {
                this.how_to_play = how_to_play;
            }

            public String getApp_version ()
            {
                return app_version;
            }

            public void setApp_version (String app_version)
            {
                this.app_version = app_version;
            }

            public String getApp_developed_by ()
            {
                return app_developed_by;
            }

            public void setApp_developed_by (String app_developed_by)
            {
                this.app_developed_by = app_developed_by;
            }

            public String getApp_author ()
            {
                return app_author;
            }

            public void setApp_author (String app_author)
            {
                this.app_author = app_author;
            }

            public String getInterstital_ad ()
            {
                return interstital_ad;
            }

            public void setInterstital_ad (String interstital_ad)
            {
                this.interstital_ad = interstital_ad;
            }


            public String getInterstital_ad_id ()
            {
                return interstital_ad_id;
            }

            public void setInterstital_ad_id (String interstital_ad_id)
            {
                this.interstital_ad_id = interstital_ad_id;
            }

            public String getApp_name ()
            {
                return app_name;
            }

            public void setApp_name (String app_name)
            {
                this.app_name = app_name;
            }

            public String getApp_logo ()
            {
                return app_logo;
            }

            public void setApp_logo (String app_logo)
            {
                this.app_logo = app_logo;
            }

            public String getApp_privacy_policy ()
            {
                return app_privacy_policy;
            }

            public void setApp_privacy_policy (String app_privacy_policy)
            {
                this.app_privacy_policy = app_privacy_policy;
            }

            public String getApp_email ()
            {
                return app_email;
            }

            public void setApp_email (String app_email)
            {
                this.app_email = app_email;
            }

            public String getAbout_us ()
            {
                return about_us;
            }

            public void setAbout_us (String about_us)
            {
                this.about_us = about_us;
            }

            public String getBanner_ad_id ()
            {
                return banner_ad_id;
            }

            public void setBanner_ad_id (String banner_ad_id)
            {
                this.banner_ad_id = banner_ad_id;
            }

            public String getBanner_ad ()
            {
                return banner_ad;
            }

            public void setBanner_ad (String banner_ad)
            {
                this.banner_ad = banner_ad;
            }

            public String getApp_website ()
            {
                return app_website;
            }

            public void setApp_website (String app_website)
            {
                this.app_website = app_website;
            }

            public String getApp_description ()
            {
                return app_description;
            }

            public void setApp_description (String app_description)
            {
                this.app_description = app_description;
            }

        private String name;

        public String getName ()
        {
            return name;
        }

        public void setName (String name)
        {
            this.name = name;
        }

        private String logo;

        public String getLogo ()
        {
            return logo;
        }

        public void setlogo (String logo)
        {
            this.logo = logo;
        }

        private String vungle_app;

        public String getVungle_app ()
        {
            return vungle_app;
        }

        public void setVungle_app (String vungle_app)
        {
            this.vungle_app = vungle_app;
        }

        private String vungle_id;

        public String getVungle_id ()
        {
            return vungle_id;
        }

        public void setVungle_id (String vungle_id)
        {
            this.vungle_id = vungle_id;
        }

        private String adcolony_app;

        public String getAdcolony_app ()
        {
            return adcolony_app;
        }

        public void setAdcolony_app (String adcolony_app)
        {
            this.adcolony_app = adcolony_app;
        }

        private String adcolony_id;

        public String getAdcolony_id ()
        {
            return adcolony_id;
        }

        public void setAdcolony_id (String adcolony_id)
        {
            this.adcolony_id = adcolony_id;
        }

        private String unity_game;

        public String getUnity_game ()
        {
            return unity_game;
        }

        public void setUnity_game (String unity_game)
        {
            this.unity_game = unity_game;
        }

        private String unity_id;

        public String getUnity_id ()
        {
            return unity_id;
        }

        public void setUnity_id (String unity_id)
        {
            this.unity_id = unity_id;
        }

        private String currency;

        public String getCurrency ()
        {
            return currency;
        }

        public void setCurrency (String currency)
        {
            this.currency = currency;
        }


        private String admob_rewarded;

        public String getAdmob_rewarded ()
        {
            return admob_rewarded;
        }

        public void setAdmob_rewarded (String admob_rewarded)
        {
            this.admob_rewarded = admob_rewarded;
        }

        private String facebook_rewarded;

        public String getFacebook_rewarded ()
        {
            return facebook_rewarded;
        }

        public void setFacebook_rewarded (String facebook_rewarded)
        {
            this.facebook_rewarded = facebook_rewarded;
        }

        private String applovin_rewarded;

        public String getApplovin_rewarded ()
        {
            return applovin_rewarded;
        }

        public void setApplovin_rewarded (String applovin_rewarded)
        {
            this.applovin_rewarded = applovin_rewarded;
        }

        private String startio_rewarded;

        public String getStartio_rewarded ()
        {
            return startio_rewarded;
        }

        public void setStartio_rewarded (String startio_rewarded)
        {
            this.startio_rewarded = startio_rewarded;
        }

        private String ironsource_rewarded;

        public String getIronsource_rewarded ()
        {
            return ironsource_rewarded;
        }

        public void setIronsource_rewarded (String ironsource_rewarded)
        {
            this.ironsource_rewarded = ironsource_rewarded;
        }

        private String mpesa_key;

        public String getMpesa_key ()
        {
            return mpesa_key;
        }

        public void setMpesa_key (String mpesa_key)
        {
            this.mpesa_key = mpesa_key;
        }

        private String coinvalue;

        public String getCoinvalue ()
        {
            return coinvalue;
        }
        public void setCoinvalue (String coinvalue)
        {
            this.coinvalue = coinvalue;
        }


        private String mpesa_code;

        public String getMpesa_code ()
        {
            return mpesa_code;
        }

        public void setMpesa_code (String mpesa_code)
        {
            this.mpesa_code = mpesa_code;
        }

        private String paypal_id;

        public String getPaypal_id ()
        {
            return paypal_id;
        }

        public void setPaypal_id (String paypal_id)
        {
            this.paypal_id = paypal_id;
        }

        private String paypal_secret;

        public String getPaypal_secret ()
        {
            return paypal_secret;
        }

        public void setPaypal_secret (String paypal_secret)
        {
            this.paypal_secret = paypal_secret;
        }
        private String flutterwave_public;

        public String getFlutterwave_public ()
        {
            return flutterwave_public;
        }

        public void setFlutterwave_public (String flutterwave_public)
        {
            this.flutterwave_public = flutterwave_public;
        }

        private String flutterwave_encryption;

        public String getFlutterwave_encryption ()
        {
            return flutterwave_encryption;
        }

        public void setFlutterwave_encryption (String flutterwave_encryption)
        {
            this.flutterwave_encryption = flutterwave_encryption;
        }

        private String razorpay;

        public String getRazorpay ()
        {
            return razorpay;
        }

        public void setRazorpay (String razorpay)
        {
            this.razorpay = razorpay;
        }

        private String stripe;

        public String getStripe ()
        {
            return stripe;
        }

        public void setStripe (String stripe)
        {
            this.stripe = stripe;
        }

        private String paypal_currency;

        public String getPaypal_currency ()
        {
            return paypal_currency;
        }

        public void setPaypal_currency (String paypal_currency)
        {
            this.paypal_currency = paypal_currency;
        }

        private String showad;

        public String getShowad ()
        {
            return showad;
        }

        public void setShowad (String showad)
        {
            this.showad = showad;
        }

        private String fb_interstitial;

        public String getFb_interstitial ()
        {
            return fb_interstitial;
        }

        public void setFb_interstitial (String fb_interstitial)
        {
            this.fb_interstitial = fb_interstitial;
        }

        private String fb_banner;

        public String getFb_banner ()
        {
            return fb_banner;
        }

        public void setFb_banner (String fb_banner)
        {
            this.fb_banner = fb_banner;
        }

        private String admob_interstitial;

        public String getAdmob_interstitial ()
        {
            return admob_interstitial;
        }

        public void setAdmob_interstitial (String admob_interstitial)
        {
            this.admob_interstitial = admob_interstitial;
        }



            @Override
            public String toString()
            {
                return "ClassPojo [publisher_id = "+publisher_id+", app_contact = "+app_contact+", how_to_play = "+how_to_play+", app_version = "+app_version+", app_developed_by = "+app_developed_by+", app_author = "+app_author+", interstital_ad = "+interstital_ad+", rewarded_ad_id =  "+interstital_ad_id+", app_name = "+app_name+", app_logo = "+app_logo+", app_privacy_policy = "+app_privacy_policy+", app_email = "+app_email+", about_us = "+about_us+", banner_ad_id = "+banner_ad_id+", banner_ad = "+banner_ad+", app_website = "+app_website+", app_description = "+app_description+",name = "+name+", logo = "+logo+", vungle_app = "+vungle_app+", vungle_id = "+vungle_id+", adcolony_app = "+adcolony_app+", adcolony_id = "+adcolony_id+", unity_game = "+unity_game+", unity_id = "+unity_id+", currency = "+currency+", admob_rewarded = "+admob_rewarded+", facebook_rewarded = "+facebook_rewarded+", applovin_rewarded = "+applovin_rewarded+", startio_rewarded = "+startio_rewarded+", ironsource_rewarded = "+ironsource_rewarded+", mpesa_key = "+mpesa_key+", mpesa_code = "+mpesa_code+", paypal_id = "+paypal_id+", paypal_secret = "+paypal_secret+", flutterwave_public = "+flutterwave_public+", flutterwave_encryption = "+flutterwave_encryption+", razorpay = "+razorpay+", stripe = "+stripe+",showad="+showad+",fb_interstitial="+fb_interstitial+",admob_interstitial="+admob_interstitial+",fb_banner="+fb_banner+",coinvalue="+coinvalue+"]";
            }


        public String getOtp_system() {
            return otp_system;
        }

        public void setOtp_system(String otp_system) {
            this.otp_system = otp_system;
        }

        public String getAds_reward() {
            return ads_reward;
        }

        public void setAds_reward(String ads_reward) {
            this.ads_reward = ads_reward;
        }

        public String getSupport_email() {
            return support_email;
        }

        public void setSupport_email(String support_email) {
            this.support_email = support_email;
        }
    }
}
	