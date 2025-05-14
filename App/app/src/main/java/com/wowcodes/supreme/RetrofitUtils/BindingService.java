package com.wowcodes.supreme.RetrofitUtils;

import com.wowcodes.supreme.Modelclas.AddOrder;
import com.wowcodes.supreme.Modelclas.AddressResponse;
import com.wowcodes.supreme.Modelclas.AddressResponse2;
import com.wowcodes.supreme.Modelclas.AllBidder;
import com.wowcodes.supreme.Modelclas.GetAchievements;
import com.wowcodes.supreme.Modelclas.GetCategories;
import com.wowcodes.supreme.Modelclas.GetCoin;
import com.wowcodes.supreme.Modelclas.GetConsolation;
import com.wowcodes.supreme.Modelclas.GetGames;
import com.wowcodes.supreme.Modelclas.GetInvestPlans;
import com.wowcodes.supreme.Modelclas.GetKycDetails;
import com.wowcodes.supreme.Modelclas.GetMultiWinners;
import com.wowcodes.supreme.Modelclas.GetMyPlans;
import com.wowcodes.supreme.Modelclas.GetNotification;
import com.wowcodes.supreme.Modelclas.GetPaymentGateway;
import com.wowcodes.supreme.Modelclas.GetPennyBidders;
import com.wowcodes.supreme.Modelclas.GetPrizes;
import com.wowcodes.supreme.Modelclas.GetProfile;
import com.wowcodes.supreme.Modelclas.GetRedeem;
import com.wowcodes.supreme.Modelclas.GetReferrals;
import com.wowcodes.supreme.Modelclas.GetSellerDetails;
import com.wowcodes.supreme.Modelclas.GetSellerItems;
import com.wowcodes.supreme.Modelclas.GetWallet;
import com.wowcodes.supreme.Modelclas.GetBidUser;
import com.wowcodes.supreme.Modelclas.GetOffersLive;
import com.wowcodes.supreme.Modelclas.GetOffersWinner;
import com.wowcodes.supreme.Modelclas.GetOrderUser;
import com.wowcodes.supreme.Modelclas.GetTransaction;
import com.wowcodes.supreme.Modelclas.LoginModel;
import com.wowcodes.supreme.Modelclas.PostKycUpdate;
import com.wowcodes.supreme.Modelclas.RegisterModel;
import com.wowcodes.supreme.Modelclas.ReviewModel;
import com.wowcodes.supreme.Modelclas.SettingModel;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.Modelclas.Ticket;
import com.wowcodes.supreme.Modelclas.UpdateAddressResponse;
import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.Modelclas.WishlistItem;
import com.wowcodes.supreme.Modelclas.WishlistResponse;
import com.wowcodes.supreme.Modelclas.AddTktResponse;
import com.wowcodes.supreme.Modelclas.getBotName;
import com.wowcodes.supreme.Modelclas.getcity;
import com.wowcodes.supreme.Modelclas.GetLotteryId;
import com.wowcodes.supreme.Modelclas.MultipleWinnersId;
import com.wowcodes.supreme.Modelclas.SingleWinnersId;
import com.wowcodes.supreme.Modelclas.Winners;
//import com.wowcodes.prizex.Modelclas.gettime;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface BindingService {


    @GET("api.php?get_offers_live")
    Call<GetOffersLive> get_offers_live(
    );

    @FormUrlEncoded
    @POST("api.php?cancel_hyip_plan")
    Call<SuccessModel> cancel_hyip_plan(
            @Field("u_id") String u_id,
            @Field("plan_id") String plan_id
    );


    @FormUrlEncoded
    @POST("api.php?add_hyip_order")
    Call<SuccessModel> add_hyip_order(
            @Field("u_id") String u_id,
            @Field("investment_amount") String amount,
            @Field("plan_id") String plan_id
    );

    @FormUrlEncoded
    @POST("api.php?post_addUserBal")
    Call<SuccessModel> post_addUserBal(
            @Field("user_id") String u_id,
            @Field("wallet") String o_id,
            @Field("type") String type
    );

    @FormUrlEncoded
    @POST("api.php?get_hyip_plan")
    Call<GetMyPlans> get_my_jars(
            @Field("u_id") String u_id
    );

    @FormUrlEncoded
    @POST("api.php?get_hyip")
    Call<GetInvestPlans> get_jars(
            @Field("u_id") String u_id,
            @Field("city_id") String city_id
    );

    @GET("api.php?get_payment_gateway")
    Call<GetPaymentGateway> get_payment_gateway(
    );


    @FormUrlEncoded
    @POST("api.php?get_achievements")
    Call<GetAchievements> get_achievements(
            @Field("user_id") String u_id
    );

    @FormUrlEncoded
    @POST("api.php?update_achievements")
    Call<SuccessModel> update_achievement(
            @Field("a_id") String a_id,
            @Field("user_id") String u_id
    );

    /*@GET("api.php?get_referral")
    Call<GetReferrals> getReferrals(
            @Query("u_id") String userId
    );*/

    @FormUrlEncoded
    @POST("api.php?get_referral")
    Call<GetReferrals> getReferrals(
            @Field("u_id") String userId
    );

    @FormUrlEncoded
    @POST("api.php?checkRegistrationPhone")
    Call<SuccessModel> checkRegistrationPhone(
            @Field("phone") String phone_no,
            @Field("country_code") String code
    );

    @FormUrlEncoded
    @POST("api.php?checkRegistrationEmail")
    Call<SuccessModel> checkRegistrationEmail(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("api.php?get_offers")
    Call<GetCategories> get_offers(
            @Field("city_id") String id
    );

    @FormUrlEncoded
    @POST("api.php?get_featured_item")
    Call<GetCategories> get_top_deals(
            @Field("city_id") String id
    );

    @FormUrlEncoded
    @POST("api.php?get_consolation")
    Call<GetConsolation> get_consolation(
            @Field("u_id") String id
    );

    @FormUrlEncoded
    @POST("api.php?get_notification_history")
    Call<GetNotification> get_notification_history(
            @Field("u_id") String id
    );

    @FormUrlEncoded
    @POST("api.php?delete_notification")
    Call<SuccessModel> delete_notifications(
            @Field("u_id") String id,
            @Field("ids") String ids
    );

    @FormUrlEncoded
    @POST("api.php?delete_notification_all")
    Call<SuccessModel> delete_all_notifications(
            @Field("u_id") String id
    );

    @FormUrlEncoded
    @POST("api.php?read_all_notifications")
    Call<SuccessModel> read_all_notifications(
            @Field("u_id") String id
    );

    @FormUrlEncoded
    @POST("api.php?update_consolation")
    Call<SuccessModel> update_consolation(
            @Field("s_status") String status,
            @Field("s_id") String id
    );

    @FormUrlEncoded
    @POST("api.php?set_notification_token")
    Call<SuccessModel> set_fcm_token(
            @Field("u_id") String user_id,
            @Field("fcm_token") String token
    );

    @FormUrlEncoded
    @POST("api.php?get_product")
    Call<GetCategories> get_product(
            @Field("o_id") String id
    );

    @FormUrlEncoded
    @POST("api.php?get_items")
    Call<GetCategories> get_categories(
            @Field("city_id") String id,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api.php?get_category_item")
    Call<GetCategories> get_category_item(
            @Field("city_id") String city_id,
            @Field("c_id") String c_id,
            @Field("area") String area
    );

    @FormUrlEncoded
    @POST("api.php?get_offers_upcomming")
    Call<GetCategories> get_offers_upcomming(
            @Field("city_id") String id,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api.php?get_shop")
    Call<GetCategories> get_shop(
            @Field("city_id") String id,
            @Field("user_id") String user_id
    );

    /*@GET("api.php?get_offers_upcomming")
    Call<GetCategories> get_offers_upcomming(
    );


    @GET("api.php?get_shop")
    Call<GetCategories> get_shop(
    );

    @GET("api.php?get_items")
    Call<GetCategories> get_categories(
    );*/


    @FormUrlEncoded
    @POST("api.php?get_penny_bid")
    Call<GetPennyBidders> get_penny_bid(
            @Field("o_id") String id
    );

    @FormUrlEncoded
    @POST("api.php?get_profile")
    Call<GetProfile> get_profile(
            @Field("u_id") String id
    );

    @FormUrlEncoded
    @POST("api.php?get_prizes")
    Call<GetPrizes> get_prizes(
            @Field("o_id") String id
    );

    @FormUrlEncoded
    @POST("api.php?get_seller_items")
    Call<GetSellerItems> get_seller_items(
            @Field("id") String id
    );

    @GET("api.php?kyc_details")
    Call<GetKycDetails> get_kyc_status(
            @Query("u_id") String userId
    );


    @Multipart
    @POST("api.php?add_kyc")
    Call<PostKycUpdate> postKycUpdate(
            @Part("u_id") RequestBody u_id,
            @Part("id_type") RequestBody id_type,
            @Part("id_number") RequestBody id_number,
            @Part MultipartBody.Part id_front,
            @Part MultipartBody.Part id_back,
            @Part("id_country") RequestBody id_country,
            @Part("dob") RequestBody dob,
            @Part("id_firstname") RequestBody id_firstname,
            @Part("id_lastname") RequestBody id_lastname
    );

    @GET("api.php?get_games")
    Call<GetGames> get_games(
    );

    @GET("api.php?get_bot_name")
    Call<getBotName> get_bot_name(
    );


    @GET("api.php?get_offers_quiz")
    Call<GetOffersLive> get_offers_quiz(
    );
    @GET("api.php?get_coin_list")
    Call<GetCoin> get_coin_list(
    );

    @GET("api.php?settings")
    Call<SettingModel> settings(
    );


    @FormUrlEncoded
    @POST("api.php?get_offers_winner")
    Call<GetOffersWinner> get_offers_winner(
            @Field("u_id") String name

    );


    @FormUrlEncoded
    @POST("api.php?get_multi_winners")
    Call<GetMultiWinners> get_multiple_winners(
            @Field("o_id") String o_id
    );


    @FormUrlEncoded
    @POST("api.php?add_bid_multi")
    Call<SuccessModel> add_bid_multi(
            @Field("add_bid_multi") String name

    );
    @FormUrlEncoded
    @POST("api.php?get_order_user")
    Call<GetOrderUser> get_order_user(
            @Field("u_id") String u_id

    );


    @FormUrlEncoded
    @POST("api.php?forgotpassword")
    Call<SuccessModel> forgotpassword(
            @Field("phone") String phone

    );

    @FormUrlEncoded
    @POST("api.php?change_password")
    Call<SuccessModel> change_password(
            @Field("phone") String phone,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("api.php?mobilenumberverify_setting")
    Call<RegisterModel> mobilenumberverify_setting(
            @Field("phone") String phone,
            @Field("confirm_code") String confirm_code);


    @FormUrlEncoded
    @POST("api.php?postUsermobileRegister")
    Call<SuccessModel> postUserRegister(
            @Field("name") String name,
            @Field("email") String email,
            @Field("country_code") String country_code,
            @Field("phone") String phone,
            @Field("refferal_code") String refferal_code,
            @Field("password") String password,
            @Field("device_id") String device_id
    );
    @FormUrlEncoded
    @POST("api.php?add_order")
    Call<AddOrder> add_order(
            @Field("u_id") String u_id,
            @Field("offer_id") String offer_id,
            @Field("total_amount") String total_amount,
            @Field("dis_amount") String dis_amount,
            @Field("pay_amount") String pay_amount,
            @Field("o_address") String o_address,
            @Field("item_id") String item_id,
            @Field("pg_id") String pg_id

    );


    @FormUrlEncoded
    @POST("api.php?postUserLogin")
    Call<LoginModel> postUserLogin(
            @Field("phone") String email,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("api.php?get_offers_id")
    Call<AllBidder> get_offers_id(
            @Field("o_id") String o_id,
            @Field("u_id") String u_id
    );
    @FormUrlEncoded
    @POST("api.php?get_ticket")
    Call<Ticket> get_ticketnew(
            @Field("o_id") String o_id,
            @Field("user_id") String user_id
    );
    @FormUrlEncoded
    @POST("api.php?getUserProfile")
    Call<UserProfile> getUserProfile    (
            @Field("id") String id
    );
    @FormUrlEncoded
    @POST("api.php?get_reviews")
    Call<ReviewModel> getReviews    (
            @Field("o_id") String o_id
    );


    @FormUrlEncoded
    @POST("api.php?get_bid_user")
    Call<GetBidUser> get_bid_user(
            @Field("u_id") String o_id
    );


    @FormUrlEncoded
    @POST("api.php?get_transaction")
    Call<GetTransaction> get_transaction(
            @Field("user_id") String o_id
    );

    @FormUrlEncoded
    @POST("api.php?get_wallet_passbook")
    Call<GetWallet> get_wallet_passbook(
            @Field("user_id") String o_id
    );

    @FormUrlEncoded
    @POST("api.php?update_wishlist=true")
    Call<WishlistResponse> updateWishlist(
            @Field("u_id") String userId,
            @Field("item_id") String itemId,
            @Field("type") String type
    );
    @FormUrlEncoded
    @POST("api.php?get_wishlist")
    Call<WishlistItem> getWishlist(
            @Field("user_id") String userId

    );

    @FormUrlEncoded
    @POST("api.php?add_bid")
    Call<SuccessModel> add_bid(
            @Field("u_id") String u_id,
            @Field("o_id") String o_id,
            @Field("bd_value") String bd_value,
            @Field("bd_amount") String bd_amount
    );

    @FormUrlEncoded
    @POST("api.php?postUserwalletUpdate")
    Call<SuccessModel> postUserwalletUpdate(
            @Field("user_id") String u_id,
            @Field("wallet") String o_id,
            @Field("package_id") String bd_value,
            @Field("order_id") String bd_amount,
            @Field("amount") String amount
    );


    @Multipart
    @POST("api.php?postUserProfileUpdate")
    Call<SuccessModel> postUserProfileUpdate(
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("phone") RequestBody phone,
            @Part MultipartBody.Part image,
            @Part("id") RequestBody id,
            @Part("password") RequestBody password
    );
    @FormUrlEncoded
    @POST("api.php?get_redeem")
    Call<GetRedeem> get_redeem(
            @Field("city_id") String id,
            @Field("user_id") String user_id
    );

    @GET("api.php?get_seller")
    Call<GetSellerDetails> get_seller(
    );
    @FormUrlEncoded
    @POST("api.php?get_ticket_all")
    Call<SuccessModel> get_ticket_all(
            @Field("o_id") String o_id,
            @Field("bd_value") String bd_value
    );
    @FormUrlEncoded
    @POST("api.php?get_max_value")
    Call<GetBidUser> get_max_value(
            @Field("o_id") String o_id
    );


    /*@GET("api.php?get_time")
    Call<gettime> get_time(
    );*/


    @FormUrlEncoded
    @POST("api.php?post_penny_bid")
    Call<SuccessModel> post_penny_bid(
            @Field("o_id") String o_id,
            @Field("o_etime") String o_etime,
            @Field("o_min") String o_min,
            @Field("o_edate") String o_edate
    );
    @FormUrlEncoded
    @POST("api.php?get_winners")
    Call<Winners> get_winners(
            @Field("user_id") String userId
    );
    @FormUrlEncoded
    @POST("api.php?get_winners_id")
    Call<SingleWinnersId> get_singlewinners_id(
            @Field("user_id") String userId,  @Field("o_id") String o_id
    );
    @FormUrlEncoded
    @POST("api.php?get_winners_id")
    Call<MultipleWinnersId> get_multiplewinners_id(

            @Field("user_id") String userId
            ,@Field("o_id") String o_id
    );

    @FormUrlEncoded
    @POST("api.php?add_address")
    Call<AddressResponse> addAddress(
            @Field("u_id") String userId,
            @Field("address_line1") String addressLine1,
            @Field("address_line2") String addressLine2,
            @Field("city") String city,
            @Field("state") String state,
            @Field("postal_code") String postalCode,
            @Field("country") String country,
            @Field("address_type") String address_type,
            @Field("nickname") String nickname

    );
    @FormUrlEncoded
    @POST("api.php?get_address")
    Call<AddressResponse2> getAddress(@Field("u_id") String userId);



    @FormUrlEncoded
    @POST("api.php?delete_address")
    Call<AddressResponse> deleteAddress(@Field("u_id") String userId, @Field("address_id") String addressId);

    @FormUrlEncoded
    @POST("api.php?edit_address")
    Call<UpdateAddressResponse> updateAddress(
            @Field("address_id") String addressId,
            @Field("nickname") String nickname,
            @Field("u_id") String userId,
            @Field("address_line1") String addressLine1,
            @Field("address_line2") String addressLine2,
            @Field("city") String city,
            @Field("state") String state,
            @Field("postal_code") String postalCode,
            @Field("country") String country,
            @Field("address_type") String addressType,
            @Query("edit_address") boolean editAddress
    );
    @FormUrlEncoded
    @POST("api.php?get_lottery_id")
    Call<GetLotteryId> get_lottery_id(
            @Field("o_id") String o_id
    );
    @FormUrlEncoded
    @POST("api.php?add_ticket")
    Call<AddTktResponse> add_ticket(
            @Field("o_id") String o_id,
            @Field("user_id") String user_id,
            @Field("ball_1") String ball_1,
            @Field("ball_2") String ball_2,
            @Field("ball_3") String ball_3,
            @Field("ball_4") String ball_4,
            @Field("ball_5") String ball_5,
            @Field("ball_6") String ball_6,
            @Field("ball_7") String ball_7,
            @Field("ball_8") String ball_8


    );

    @FormUrlEncoded
    @POST("api.php?add_review")
    Call<SuccessModel> add_review(
            @Field("user_id") String u_id,
            @Field("item_id") String item_id,
            @Field("rating") String rating,
            @Field("review") String review
    );

    @FormUrlEncoded
    @POST("api.php?update_review")
    Call<SuccessModel> update_review(
            @Field("user_id") String u_id,
            @Field("item_id") String item_id,
            @Field("rating") String rating,
            @Field("review") String review
    );

    @GET("api.php?get_city")
    Call<getcity> get_city(
    );
}