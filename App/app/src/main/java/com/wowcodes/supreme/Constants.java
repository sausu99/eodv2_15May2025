// The code snippet provided is defining an interface called `Constants`. This interface contains
// various constant variables that are used throughout the application.
package com.wowcodes.supreme;

public interface Constants {
    // TODO: Change the backend urls below ;)
    // TODO: Change (prizex.wowcodes.in) to yourdomain ;)
    String baseurl = "http://z/seller/api.php?/";
    String main_url="http://prizex.wowcodes.in/";
    String imageurl = "http://prizex.wowcodes.in/seller/images/";
    String retrobaseurl = "http://prizex.wowcodes.in/seller/";
     int CONNECT_TIMEOUT = 60 * 1000;

    int READ_TIMEOUT = 60 * 1000;

    int WRITE_TIMEOUT = 60 * 1000;
//MPESA STARTs
     String BASE_URL = "https://safaricom.co.ke/";
    //"https://safaricom.co.ke";
    // live https://safaricom.co.ke
     String BUSINESS_SHORT_CODE = "4114595";
     String PASSKEY = "dacec101824c46f67ea28f80ab68b9a7e4a3443c0fe9b915da8aecf5ccdbd92d";
     String TRANSACTION_TYPE = "CustomerPayBillOnline";
     String PARTYB = "4114595"; //same as business shortcode above
     String CALLBACKURL = "https://demo.wowcodes.in/supreme/seller/api.php?add_bid";
//MPESA DONE

//PAYPAL

    String PAYPAL_CURRENCY = "USD";
    String CLIENT_ID = "AZ1WslU_9hTHf-VfwaS26gG78HQbD7lGcUyl0d-vWm8zt1blz4D389tIx7MT6PuswvTxzSQr7zgnYiMe";
    String CLIENT_SECRET = "ECTXDcIodoam0ZIwaSt2Ds580NGGGKHMBe1lVFKzRTNptz_VWrwbRtEfRmGBCBiCoE6gB-B621Pd6O_g";
    String PAYPAL_BASE_URL =  "https://api-m.paypal.com"; // FOR PRODUCTION
    //"https://api-m.sandbox.paypal.com"; //For sandbox testing


//PAYPAL

//current timezone
    //Look for availanle time zones from below url
    //https://www.timeapi.io/api/TimeZone/AvailableTimeZones
    String CURRENT_TIMEZONE = "Asia/Calcutta";
}