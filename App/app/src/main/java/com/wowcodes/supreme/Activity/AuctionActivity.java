package com.wowcodes.supreme.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.wowcodes.supreme.Adapter.ImagesAdapter;
import com.wowcodes.supreme.Adapter.ProductListAdapter;
import com.wowcodes.supreme.Adapter.AuctionItemAdapter;
import com.wowcodes.supreme.Adapter.ReviewAdapter;
import com.wowcodes.supreme.BidStatusCallback;
import com.wowcodes.supreme.Constants;
import com.wowcodes.supreme.Modelclas.AllBidder;
import com.wowcodes.supreme.Modelclas.AllBidderInner;
import com.wowcodes.supreme.Modelclas.AllReviewer;
import com.wowcodes.supreme.Modelclas.GetBidUser;
import com.wowcodes.supreme.Modelclas.GetCategories;
import com.wowcodes.supreme.Modelclas.GetSellerItems;
import com.wowcodes.supreme.Modelclas.ReviewModel;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.Modelclas.WishlistResponse;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuctionActivity extends AppCompatActivity {
    SavePref savePref;

    // Date and Time
    String curr_dt_time = "0000-00-00 00:00:00";
    String bidincrement,edittitle;
    LinearLayout winnerlay,loading;
    RelativeLayout txt_morelikethis;

    String startDate;
    private boolean countdownStarted = false; // Flag to track countdown status

    RecyclerView seller_items,moreitems;
    String winnername,winningvalue="ap";

    // Bids and Amounts
    String getBids;
    double minimum;
    double maximum;
    String getAmount,bidstatus;
    String itemid;
    Double ItemMaxValue;
    // UI Elements
    public BindingService apiinfoservice;
    ImageView imgBackBtn, imgShareBtn, leftimgbtn, rightimgbtn, wishlistbtn, redheartbtn;
    LinearLayout walletbtn,  pointslay, placebidbtn;
    ConstraintLayout mainlayout;
    ViewPager imgpager;
    CircleImageView imgseller;
    ImageView sellerstar1,sellerstar2,sellerstar3,sellerstar4,sellerstar5;
    TextView discountedtxt, or, discountpr, oname, oamount, itemdesc, whatquestion,norating,
            noreviewtxt, reviewfailure, txtItemSeller, walletbal, allBids, yourBids,
            mrp, timeRemaining, desctext, learnmore,otherlisting,winnnernametxt,sellerabouttxt,placetxt;
    ImageView p0, p1, p2, p3, p4;
    RecyclerView review_RecyclerView;
    // Seller Information
    String sellerid, SellerName, SellerAbout, SellerLink, SellerImage,Sellerrating, SellerJoinDate;
    // Order Information
    String oid, name, totalwallet, obuy, o_amount, image, otype;
    String dialogtitle, wishliststatus;
    // Collections

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savePref = new SavePref(AuctionActivity.this);
        if (savePref.getLang() == null) {
            savePref.setLang("en");
        }
        if (Objects.equals(savePref.getLang(), "en")) {
            setLocale("");
        } else {
            setLocale(savePref.getLang());
        }

// Configure window and layout
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);

// Set content view and initialize views
        setContentView(R.layout.activity_auction);
        loading = findViewById(R.id.linearlay);
        mainlayout=findViewById(R.id.maincontent);
        mainlayout.setVisibility(GONE);
        loading.setVisibility(VISIBLE);
        Handler handler=new Handler();

// Bind UI elements
        oid = getIntent().getStringExtra("O_id");
        review_RecyclerView = findViewById(R.id.recycler_view);
        review_RecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imgpager = findViewById(R.id.image_pager);
        txtItemSeller = findViewById(R.id.sellername);
        imgBackBtn = findViewById(R.id.imgBackk);
        desctext = findViewById(R.id.desctexxt);
        imgShareBtn = findViewById(R.id.ShareBtn);
        wishlistbtn = findViewById(R.id.wishlistbtn);
        redheartbtn = findViewById(R.id.redheartbtn);
        walletbtn = findViewById(R.id.walletbtn);
        placebidbtn = findViewById(R.id.placebidbtn);
        walletbal = findViewById(R.id.walletbal);
        learnmore = findViewById(R.id.learnmore);
        allBids = findViewById(R.id.allBids);
        yourBids = findViewById(R.id.yourBids);
        noreviewtxt = findViewById(R.id.noreviewtxt);
        reviewfailure = findViewById(R.id.fetchingfailure);
        discountpr = findViewById(R.id.discountpr);
        leftimgbtn = findViewById(R.id.leftimgbtn);
        rightimgbtn = findViewById(R.id.rightimgbtn);
        itemdesc = findViewById(R.id.itemDescr);
        oname = findViewById(R.id.o_name);
        oamount = findViewById(R.id.oamount);
        whatquestion = findViewById(R.id.whatquestion);
        timeRemaining = findViewById(R.id.time_remaining);
        winnerlay=findViewById(R.id.winnerlay);
        winnnernametxt=findViewById(R.id.winnername);
        otherlisting=findViewById(R.id.otherlisting);
        or = findViewById(R.id.or);
        imgseller = findViewById(R.id.sellerimg);
        mrp = findViewById(R.id.mrp);
        p0 = findViewById(R.id.p0);
        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);
        pointslay = findViewById(R.id.pointslay);
        discountedtxt = findViewById(R.id.discountedtxt);
        sellerstar1=findViewById(R.id.sellerrating1);
        sellerstar2=findViewById(R.id.sellerrating2);
        sellerstar3=findViewById(R.id.sellerrating3);
        sellerstar4=findViewById(R.id.sellerrating4);
        sellerstar5=findViewById(R.id.sellerrating5);
        norating=findViewById(R.id.noratingstar);
        sellerabouttxt=findViewById(R.id.sellerabouttxt);
        placetxt=findViewById(R.id.placetxt);
        moreitems=findViewById(R.id.moreitems);
        txt_morelikethis=findViewById(R.id.txt_morelikethis);
        moreitems.setLayoutManager(new LinearLayoutManager(AuctionActivity.this,RecyclerView.HORIZONTAL,false));



// Initialize API service and setup UI
        apiinfoservice = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        isNetworkConnected();
        handledynamiclinks();
        getmoreitems();
        getprofile();
        getofferapi();
        getReview();


        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(savePref.getUserId(), "0")){
                    Intent intent=new Intent(AuctionActivity.this,LoginActivity.class);
                    intent.putExtra("Decider", "Category");
                    startActivity(intent);
                }

                shareDeepLink();
            }
        });


        allBids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AuctionActivity.this, AllBidderActivity.class);
                i.putExtra("o_id", oid);
                startActivity(i);
            }
        });
        yourBids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AuctionActivity.this, AllUserBidderActivity.class);
                i.putExtra("o_id", oid);
                startActivity(i);

            }
        });


        leftimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = imgpager.getCurrentItem();
                if (currentItem > 0) {
                    imgpager.setCurrentItem(currentItem - 1);
                }
            }
        });

        rightimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = imgpager.getCurrentItem();
                if (currentItem < imgpager.getAdapter().getCount() - 1) {
                    imgpager.setCurrentItem(currentItem + 1);
                }
            }
        });

        wishlistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtowishlist();
                wishlistbtn.setVisibility(GONE);
                redheartbtn.setVisibility(VISIBLE);

            }
        });
        redheartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletefromwishlist();
                wishlistbtn.setVisibility(VISIBLE);
                redheartbtn.setVisibility(GONE);
            }
        });



        learnmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (otype.toString()){
                    case "1": {//lowest auciton
                        Intent intent=new Intent(AuctionActivity.this, HowBidWorks.class);
                        startActivity(intent);

                        break;
                    }
                    case "2": {//highest auciton
                        Intent intent=new Intent(AuctionActivity.this, HighestBidWorks.class);
                        startActivity(intent);
                        break;
                    }
                    case "7": {//english auciton
                        Intent intent=new Intent(AuctionActivity.this, DynamicWorks.class);
                        startActivity(intent);
                        break;
                    }
                    case "8": {//penny auciton
                        Intent intent=new Intent(AuctionActivity.this, PennyWorks.class);
                        startActivity(intent);

                        break;
                    }
                    case "10": {//reverse auciton
                        Intent intent=new Intent(AuctionActivity.this, Reverseworks.class);
                        startActivity(intent);


                        break;
                    }
                    case "11": {//slot auciton
                        Intent intent=new Intent(AuctionActivity.this, Slotworks.class);
                        startActivity(intent);



                        break;
                    }
                    default: {
                        Intent intent=new Intent(AuctionActivity.this, AuctionWorks.class);
                        startActivity(intent);

                        break;
                    }

                }


            }
        });

        txtItemSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        otherlisting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (SellerName != null && !SellerName.isEmpty()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            openWindialog();
                        } else {
                            // Handle the case for lower API levels if needed
                            Log.d("ListingClick", "API level is lower than O");
                        }
                    } else {
                        Log.e("ListingClick", "SellerName is null or empty");
                        Toast.makeText(getApplicationContext(), "Seller name is not specified.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("ListingClick", "Error occurred while opening the dialog", e);
                    // Optionally, show a user-friendly message
                    Toast.makeText(getApplicationContext(), "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        placebidbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!otype.toString().equalsIgnoreCase("8")) {

                    Dialog dialog = new Dialog(AuctionActivity.this);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.dialog_place_bid);
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                Button submit = dialog.findViewById(R.id.submitbtn);
                Button cancel = dialog.findViewById(R.id.cancel);
                TextView d_title = dialog.findViewById(R.id.d_title);
                TextView editcurrency = dialog.findViewById(R.id.bidvaluecurrency);
                TextView desctxt = dialog.findViewById(R.id.desctxt);
                TextView bidamttxt = dialog.findViewById(R.id.bidamttxt);
                EditText bidvalue = dialog.findViewById(R.id.bidamt);
                editcurrency.setText(MainActivity.currency);

                String confirmbid = " ";
                String placedbidbefore = " ";
                String placeafter = " ";
                String dialogtitle = " ";

                    switch (otype.toString()) {
                        case "1":
                            placedbidbefore = " Your lowest unique bid of";
                            placeafter = " has been submitted";
                            confirmbid = "Single lowest unique bid of ";
                            dialogtitle = getString(R.string.string530);
                            break;
                        case "2":
                            placedbidbefore = " Your Highest unique bid of";
                            placeafter = " has been submitted";
                            confirmbid = "Highest unique bid of ";
                            dialogtitle = getString(R.string.string534);
                            break;
                        case "7":
                            placedbidbefore = "Your bid for ";
                            placeafter = " has been placed successfully ";
                            confirmbid = "You are placing a bid of ";
                            dialogtitle = getString(R.string.string548);
                            desctxt.setText(R.string.englishauction);
                            break;
                        case "8":
                            placedbidbefore = "Your bid of ";
                            placeafter = " has been submitted";
                            confirmbid = "No confirmation, place bid directly ";
                            dialogtitle = getString(R.string.string536);
                            desctxt.setText(R.string.pennyauction);
                            break;
                        case "10":
                            placedbidbefore = "You bid for ";
                            placeafter = " ";
                            confirmbid = "You are placing a bid of ";
                            dialogtitle = getString(R.string.string537);
                            break;
                        case "11":
                            placedbidbefore = " You purchased slot of ";
                            placeafter = " ";
                            placetxt.setText(getString(R.string.string517));
                            bidamttxt.setText(getString(R.string.string518));
                            desctxt.setText(getString(R.string.string516));
                            confirmbid ="You are purchasing one slot of ";
                            dialogtitle = getString(R.string.string538);
                            break;
                        default:
                            placedbidbefore = "Submitted bid of ";
                            placeafter = " ";
                            confirmbid = "You are placing a bid of  ";
                            dialogtitle = "Submit A Bid";
                            break;
                    }


                d_title.setText(dialogtitle);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                String finalConfirmbid = confirmbid;
                String finalPlacedbidbefore = placedbidbefore;
                String finalPlaceafter = placeafter;
                String finalConfirmbid1 = confirmbid;
                String finalPlacedbidbefore1 = placedbidbefore;
                String finalPlaceafter1 = placeafter;
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String bidText = bidvalue.getText().toString();
                        if (!TextUtils.isEmpty(bidText)) {
                            try {
                                // Parse bid amount
                                DecimalFormat form = new DecimalFormat("0.00");
                                double bidAmount = Double.parseDouble(bidText);

                                // Get minimum and maximum bid amounts
                                double omin = Double.parseDouble(String.valueOf(minimum));
                                double omax = Double.parseDouble(String.valueOf(maximum));

                                // Validate bid amount
                                if (bidAmount >= omin && bidAmount <= omax) {
                                    // Proceed with bid confirmation
                                    String finalConfirmbid = finalConfirmbid1;
                                    String finalPlacedbidbefore = finalPlacedbidbefore1;
                                    String finalPlaceafter = finalPlaceafter1;

                                    getBids = form.format(bidAmount);

                                        dialog.dismiss();
                                        Dialog dialogcnf = new Dialog(AuctionActivity.this);
                                        dialogcnf.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                        dialogcnf.setContentView(R.layout.dialog_confirm_bid);
                                        Window window = dialogcnf.getWindow();
                                        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                        Button confirm = dialogcnf.findViewById(R.id.submitbtn);
                                        Button cancel = dialogcnf.findViewById(R.id.cancel);
                                        TextView amountshown = dialogcnf.findViewById(R.id.amountshown);
                                        TextView reducetxt = dialogcnf.findViewById(R.id.reducetxt);

                                        amountshown.setText(finalConfirmbid + MainActivity.currency + " " + bidText);
                                        reducetxt.setText(o_amount + " Coins will be Deducted from your wallet balance on confirmation");

                                        cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialogcnf.dismiss();
                                            }
                                        });

                                        confirm.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (isValidNumber(totalwallet) && isValidNumber(o_amount)) {
                                                    int totalWalletInt = Integer.parseInt(totalwallet);
                                                    int oAmountInt = Integer.parseInt(o_amount);

                                                    if (totalWalletInt >= oAmountInt) {
                                                        dialogcnf.dismiss();
                                                        Dialog dialogplacedbid = new Dialog(AuctionActivity.this);
                                                        dialogplacedbid.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                                        dialogplacedbid.setContentView(R.layout.dialog_bid_placed);
                                                        Window window = dialogplacedbid.getWindow();
                                                        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        Button ok = dialogplacedbid.findViewById(R.id.ok);
                                                        TextView submittedbid = dialogplacedbid.findViewById(R.id.bidsubmitted);
                                                        TextView bidstautustxt = dialogplacedbid.findViewById(R.id.bidstatustxt);

                                                        addbid(new BidStatusCallback() {
                                                            @Override
                                                            public void onBidStatusReceived(String bidStatus) {
                                                                bidstatus = bidStatus;
                                                                getprofile(); // Ensure this also happens after bid status is received

                                                                if (otype != null && (otype.equals("1") || otype.equals("2"))) {
                                                                    if (bidstatus != null) {
                                                                        bidstautustxt.setVisibility(View.VISIBLE);
                                                                        switch (bidstatus) {
                                                                            case "1":
                                                                                if (otype.equals("1")) {
                                                                                    bidstautustxt.setText("It is Unique but not Lowest");
                                                                                } else if (otype.equals("2")) {
                                                                                    bidstautustxt.setText("It is Unique but not Highest");
                                                                                }
                                                                                break;
                                                                            case "2":
                                                                                bidstautustxt.setText("Unfortunately it is NOT UNIQUE");
                                                                                break;
                                                                            case "3":
                                                                                bidstautustxt.setText("Congratulations, It's UNIQUE & you are WINNING right now");
                                                                                break;
                                                                            default:
                                                                                bidstautustxt.setVisibility(View.GONE);
                                                                                break;
                                                                        }
                                                                    } else {
                                                                        bidstautustxt.setVisibility(View.GONE);
                                                                    }
                                                                } else {
                                                                    bidstautustxt.setVisibility(View.GONE);
                                                                }

                                                                submittedbid.setText(finalPlacedbidbefore + MainActivity.currency + " " + bidText + finalPlaceafter);

                                                                ok.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        getofferapi();
                                                                        dialogplacedbid.dismiss();

                                                                    }
                                                                });

                                                                dialogplacedbid.show();
                                                            }
                                                        });
                                                    } else {
                                                        Toast.makeText(AuctionActivity.this, R.string.string36, Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(AuctionActivity.this, "Insufficient for total wallet or amount", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        dialogcnf.show();

                                } else {
                                    // Bid amount out of range
                                    Toast.makeText(AuctionActivity.this, "Bid amount must be between " + MainActivity.currency + minimum + " and " + MainActivity.currency + maximum, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(AuctionActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AuctionActivity.this, "Please enter a bid amount properly", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
                } else {
                    try {
                        if (Integer.parseInt(totalwallet) >= Integer.parseInt(o_amount)) {
                            getBids = String.valueOf(Double.parseDouble(String.valueOf(minimum)) + Double.parseDouble(bidincrement));

                            addbid(new BidStatusCallback() {
                                @Override
                                public void onBidStatusReceived(String bidStatus) {
                                    runOnUiThread(() -> {
                                        bidstatus = bidStatus;
                                        getprofile();
                                        getofferapi(); // Ensure this also happens after bid status is received
                                        Toast.makeText(AuctionActivity.this, "Your bid has been placed directly", Toast.LENGTH_SHORT).show();
                                    });
                                }
                            });
                        } else {
                            runOnUiThread(() -> Toast.makeText(AuctionActivity.this, R.string.string36, Toast.LENGTH_SHORT).show());
                        }
                    } catch (Exception e) {
                        runOnUiThread(() -> Toast.makeText(AuctionActivity.this, "Wallet empty", Toast.LENGTH_SHORT).show());
                    }
                }
            }
        });





    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void openWindialog() {
        Dialog dialog = new Dialog(AuctionActivity.this);
        dialog.setContentView(R.layout.activity_user_profile);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogback2);


        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextView seller_name = dialog.findViewById(R.id.user_name);
        TextView txtuser_profile = dialog.findViewById(R.id.txtuser_profile);
        TextView join_date = dialog.findViewById(R.id.join_date);
        TextView txtuser_int = dialog.findViewById(R.id.txtuser_int);
        TextView seller_website = dialog.findViewById(R.id.seller_website);
        ImageView seller_img = dialog.findViewById(R.id.profile_img);
        ImageView close = dialog.findViewById(R.id.close);
        seller_items = dialog.findViewById(R.id.user_int);
        seller_items.setLayoutManager(new LinearLayoutManager(AuctionActivity.this));

        try {
            if (SellerLink.isEmpty()) {
                seller_website.setVisibility(View.GONE);
            }
        } catch (Exception ignore) {}

        // Check and parse SellerJoinDate
        if (SellerJoinDate != null && !SellerJoinDate.isEmpty()) {
            try {
                LocalDate currentDate = LocalDate.parse(SellerJoinDate);
                Month mon = currentDate.getMonth();
                int year = currentDate.getYear();
                String month = String.valueOf(mon.toString().charAt(0)).toUpperCase() + mon.toString().substring(1).toLowerCase();
                join_date.setText(getResources().getString(R.string.member_since) + month + " " + year);
            } catch (Exception e) {
                Log.e("openWindialog", "Error parsing SellerJoinDate: " + SellerJoinDate, e);
                join_date.setText(getResources().getString(R.string.member_since) + "Unknown");
            }
        } else {
            join_date.setText(getResources().getString(R.string.member_since) + "Unknown");
        }

        txtuser_profile.setText(getResources().getString(R.string.seller_profile));
        seller_name.setText(SellerName);
        txtuser_int.setText("- " + SellerName + getResources().getString(R.string.other_products));

        try {
            Glide.with(AuctionActivity.this)
                    .load(SellerImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerInside()
                    .into(seller_img);
        } catch (Exception ignore) {}

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        seller_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SellerLink.isEmpty()) {
                    Intent intent = new Intent(AuctionActivity.this, WebViewActivity.class);
                    intent.putExtra("url", SellerLink);
                    intent.putExtra("from", "categories");
                    intent.putExtra("title", "2");
                    startActivity(intent);
                }
            }
        });

        load_seller_products();
        dialog.show();
    }

    public void load_seller_products(){
        try {
            apiinfoservice.get_seller_items(sellerid).enqueue(new Callback<GetSellerItems>() {
                @Override
                public void onResponse(Call<GetSellerItems> call, retrofit2.Response<GetSellerItems> response) {
                    try {
                        ArrayList<GetSellerItems.Get_items_Inner> arrayList = response.body().getJSON_DATA();
                        seller_items.setAdapter(new ProductListAdapter(AuctionActivity.this, arrayList,sellerid));
                    } catch (Exception ignore) {}
                }
                @Override public void onFailure(Call<GetSellerItems> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }




    private void shareDeepLink() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        String deepLink= Constants.baseurl.substring(0,Constants.baseurl.indexOf("seller/")) + "share.php?share_id="+oid;
        intent.putExtra(Intent.EXTRA_TEXT, deepLink);
        startActivity(intent);

    }


    private Call<UserProfile> callgetApi() {
        return apiinfoservice.getUserProfile(savePref.getUserId());
    }



    public void getofferapi() {
        loading.setVisibility(VISIBLE);
        mainlayout.setVisibility(View.INVISIBLE);

        try {
            callofferApi().enqueue(new Callback<AllBidder>() {
                @Override
                public void onResponse(Call<AllBidder> call, retrofit2.Response<AllBidder> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        AllBidder allBidder = response.body();
                        ArrayList<AllBidderInner> arrayList = allBidder.getJSON_DATA();


                        List<String> images = new ArrayList<>();
                        processImages(images, arrayList);

                        updateViewPager(images);

                        updateUIElements(arrayList);

                        if (arrayList != null && !arrayList.isEmpty()) {
                            if (!countdownStarted) { // Only start countdown if not already started
                                startCountdownTimer(arrayList.get(0).getO_edate(), arrayList.get(0).getO_etime());
                                countdownStarted = true; // Set flag to true after starting countdown
                            }
                        } else {
                            Log.e("TAG", "The arrayList is empty");
                        }
                    } else {
                        Log.e("API", "Unsuccessful response or null body");
                    }
                    loading.setVisibility(GONE);
                    mainlayout.setVisibility(VISIBLE);

                }

                @Override
                public void onFailure(Call<AllBidder> call, Throwable t) {
                    Log.e("API", "Network call failed", t);
                    loading.setVisibility(GONE);
                    mainlayout.setVisibility(VISIBLE);

                }
            });
        } catch (Exception e) {
            loading.setVisibility(GONE);
            mainlayout.setVisibility(VISIBLE);

            Log.e("API", "Exception", e);
        }
    }



    private void processImages(List<String> images, ArrayList<AllBidderInner> arrayList) {
        if (arrayList.size() > 0) {
            addValidImage(images, arrayList.get(0).getO_image());
            addValidImage(images, arrayList.get(0).getO_image1());
            addValidImage(images, arrayList.get(0).getO_image2());
            addValidImage(images, arrayList.get(0).getO_image3());
            addValidImage(images, arrayList.get(0).getO_image4());
        }
        else {
            pointslay.setVisibility(GONE);
        }
    }

    private void addValidImage(List<String> images, String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty() && !imageUrl.endsWith("/")) {
            images.add(imageUrl);
        }else{
            pointslay.setVisibility(GONE);
            leftimgbtn.setVisibility(GONE);
            rightimgbtn.setVisibility(GONE);
        }
    }

    private void updateViewPager(List<String> images) {
        imgpager.setAdapter(new ImagesAdapter(AuctionActivity.this, images));

        // Set page change listener for ViewPager
        imgpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updateIndicatorImages(position);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void updateIndicatorImages(int position) {
        ImageView[] indicators = {p0, p1, p2, p3, p4};
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setImageDrawable(getResources().getDrawable(i == position ?
                    R.drawable.img_selected_b : R.drawable.img_notselected_b));
        }
    }

    private void updateUIElements(ArrayList<AllBidderInner> arrayList) {
        if (arrayList.size() > 0) {
            AllBidderInner item = arrayList.get(0);
            name = item.getO_name();
            image = item.getO_image();
            obuy = item.getO_price();
            String oMinString = item.getO_min();
            double itemMaxValue = parseDoubleSafely(oMinString);
            ItemMaxValue = itemMaxValue;
            otype = item.getO_type();
            getAmount = arrayList.get(0).getO_amount().toString();
            minimum=parseDoubleSafely(arrayList.get(0).getO_min());
            maximum=parseDoubleSafely(arrayList.get(0).getO_max());
            itemid=item.getItem_id();
            SellerName=item.getSeller_name();
            SellerImage=item.getSeller_image();
            Sellerrating=item.getSeller_ratting();
            SellerAbout=item.getSeller_about();
            SellerLink=item.getSeller_link();
            SellerJoinDate=item.getSeller_join_date();
            String biddernum=item.getTotal_users();
            allBids.setText(biddernum+" Bidders");
            winnername=item.getWon_name();
            bidincrement=item.getBid_increment();

            wishliststatus= item.getWishlist_status();
            if (Objects.equals(wishliststatus, "1") ||Integer.parseInt(wishliststatus)>=1) {
                wishlistbtn.setVisibility(GONE);
                redheartbtn.setVisibility(VISIBLE);
            }else if(Objects.equals(wishliststatus, "0")){
                wishlistbtn.setVisibility(VISIBLE);
                redheartbtn.setVisibility(GONE);
            }
            String desc=item.getO_desc();


           sellerid=item.getSeller_id();
            switch (Sellerrating){
                case "1":{ sellerstar1.setVisibility(VISIBLE);
                    sellerstar2.setVisibility(View.INVISIBLE);
                    sellerstar3.setVisibility(View.INVISIBLE);
                    sellerstar4.setVisibility(View.INVISIBLE);
                    sellerstar5.setVisibility(View.INVISIBLE);
                    break;}
                case "2":{ sellerstar1.setVisibility(VISIBLE);
                    sellerstar2.setVisibility(VISIBLE);
                    sellerstar3.setVisibility(View.INVISIBLE);
                    sellerstar4.setVisibility(View.INVISIBLE);
                    sellerstar5.setVisibility(View.INVISIBLE);
                    break;}
                case "3":{ sellerstar1.setVisibility(VISIBLE);
                    sellerstar2.setVisibility(VISIBLE);
                    sellerstar3.setVisibility(VISIBLE);
                    sellerstar4.setVisibility(View.INVISIBLE);
                    sellerstar5.setVisibility(View.INVISIBLE);
                    break;}
                case "4":{ sellerstar1.setVisibility(VISIBLE);
                    sellerstar2.setVisibility(VISIBLE);
                    sellerstar3.setVisibility(VISIBLE);
                    sellerstar4.setVisibility(VISIBLE);
                    sellerstar5.setVisibility(View.INVISIBLE);
                    break;}
                case "5":{ sellerstar1.setVisibility(VISIBLE);
                    sellerstar2.setVisibility(View.VISIBLE);
                    sellerstar3.setVisibility(View.VISIBLE);
                    sellerstar4.setVisibility(View.VISIBLE);
                    sellerstar5.setVisibility(VISIBLE);
                    break;}
                default:{sellerstar1.setVisibility(GONE);
                    sellerstar2.setVisibility(GONE);
                    sellerstar3.setVisibility(GONE);
                    sellerstar4.setVisibility(GONE);
                    sellerstar5.setVisibility(GONE);
                    norating.setVisibility(VISIBLE);
                    break;
                }

            }

            try {
                Glide.with(AuctionActivity.this)
                        .load(SellerImage)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerInside()
                        .into(imgseller);

                txtItemSeller.setText(SellerName != null ? SellerName : "No SellerName");
                txtItemSeller.setClickable(SellerName != null);
                txtItemSeller.setEnabled(SellerName != null);
            } catch (Exception e) {
                Log.e("API", "Exception in updateSellerUI", e);
                txtItemSeller.setText("No SellerName");
                txtItemSeller.setClickable(false);
                txtItemSeller.setEnabled(false);
            }

            switch (String.valueOf(otype)) {
                case "1": {
                    dialogtitle = getString(R.string.string530);
                    desctext.setText(R.string.lowestuniquebid);

                    if (winnername != null && !winnername.equals("0")) {
                        winnnernametxt.setText(winnername + getString(R.string.string531));
                    } else if (String.valueOf(item.getWon_id()).equals(savePref.getUserId())) {
                        winnnernametxt.setText(getString(R.string.string532));
                    } else {
                        winnnernametxt.setText(getString(R.string.string533));
                    }

                    winnnernametxt.setVisibility(View.VISIBLE);
                    break;
                }
                case "2": {
                    dialogtitle = getString(R.string.string534);
                    desctext.setText(R.string.highestuniquebid);

                    if (winnername != null && !winnername.equals("0")) {
                        winnnernametxt.setText(winnername + " "+getString(R.string.string531));
                    } else if (String.valueOf(item.getWon_id()).equals(savePref.getUserId())) {
                        winnnernametxt.setText(getString(R.string.string532));
                    } else {
                        winnnernametxt.setText(getString(R.string.string533));
                    }

                    winnnernametxt.setVisibility(View.VISIBLE);
                    break;
                }
                case "3":
                case "4":
                case "5":
                case "6": {
                    winnerlay.setVisibility(View.GONE);
                    break;
                }
                case "7":
                case "8":
                case "10": {
                    winningvalue = item.getWinning_bid();
                    if (winnername != null && !winnername.equals("0") && winningvalue != null && !winningvalue.equals("0")) {
                        String text = winnername + " "  +getString(R.string.string457)+" "  + MainActivity.currency + winningvalue;
                        winnnernametxt.setText(text);
                    } else if (String.valueOf(item.getWon_id()).equals(savePref.getUserId())) {
                        String text = getString(R.string.string535)+" " + MainActivity.currency + winningvalue;
                        winnnernametxt.setText(text);
                    } else {
                        winnnernametxt.setText(getString(R.string.string515));
                    }

                    winnnernametxt.setVisibility(View.VISIBLE);
                    dialogtitle = (otype.equals("7") || otype.equals("8"))
                            ? getString(R.string.string536)
                            :getString(R.string.string537);
                    desctext.setText((otype.equals("7") || otype.equals("8"))
                            ? R.string.pennyauction
                            : R.string.reverseauction);
                    break;
                }
                case "11": {
                    dialogtitle = getString(R.string.string538);
                    winnerlay.setVisibility(View.GONE);
                    desctext.setText(R.string.slotauction);
                    break;
                }
                default: {
                    break;
                }
            }

            if (item.getO_buy() == null || item.getO_buy().isEmpty() || item.getO_buy().equals("0")) {
                mrp.setVisibility(View.GONE);
                discountpr.setVisibility(View.GONE);
                discountedtxt.setVisibility(View.GONE);
                or.setVisibility(View.GONE);
            } else {
                mrp.setText("Actual price: " + MainActivity.currency + " " + item.getO_price());
                discountpr.setText("Discounted Price: "+MainActivity.currency + " " + item.getO_buy());
            }
            if (item.getO_desc() == null||item.getO_desc().isEmpty()) {
                itemdesc.setText("No Description");

            }else{
                CharSequence sequence;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    sequence = Html.fromHtml(desc, Html.FROM_HTML_MODE_LEGACY);
                } else {
                    sequence = Html.fromHtml(desc);
                }


                Linkify.addLinks(itemdesc,Linkify.WEB_URLS);
                itemdesc.setText(sequence);
                itemdesc.setMovementMethod(LinkMovementMethod.getInstance());

            }

            SellerAbout=item.getSeller_about();
            sellerabouttxt.setText(SellerAbout);


            o_amount = item.getO_amount();
            oamount.setText(item.getO_amount() + " Coin");
            oname.setText(item.getO_name());
        }
    }

    private void startCountdownTimer(String endDate, String endTime) {
        try {
            String dateTime = endDate + " " + endTime;
            Thread myThread = new Thread(new CountDownRunner(timeRemaining, dateTime));
            myThread.start();
        } catch (Exception ignore) {
        }
    }
    private Call<ReviewModel> callGetApiReview(){
        return apiinfoservice.getReviews(oid);
    }
    public void addtowishlist(){
        try{
            calladdtoWishlist().enqueue(new Callback<WishlistResponse>() {
                @Override
                public void onResponse(Call<WishlistResponse> call, Response<WishlistResponse> response) {
                    if (response.isSuccessful()&& response.body()!= null) {
                        WishlistResponse wishlistResponse = response.body();
                        if (wishlistResponse != null && wishlistResponse.getJSON_DATA() != null && !wishlistResponse.getJSON_DATA().isEmpty()) {
                            WishlistResponse.WishlistMessage message = wishlistResponse.getJSON_DATA().get(0);
                            Toast.makeText(AuctionActivity.this, "Added to Wishlist: " + message.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AuctionActivity.this, "Empty response body or jsonData list", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(AuctionActivity.this, "Failed to add to Wishlist. Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<WishlistResponse> call, Throwable t) {
                    Toast.makeText(AuctionActivity.this, "Failed to add to Wishlist. Response code: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception ignore){
        }

    }
    public void deletefromwishlist(){
        try{
            calldeletefromWishlist().enqueue(new Callback<WishlistResponse>() {
                @Override
                public void onResponse(Call<WishlistResponse> call, Response<WishlistResponse> response) {

                    if (response.isSuccessful()&& response.body()!= null) {
                        WishlistResponse wishlistResponse = response.body();
                        if (wishlistResponse != null && wishlistResponse.getJSON_DATA() != null && !wishlistResponse.getJSON_DATA().isEmpty()) {
                            WishlistResponse.WishlistMessage message = wishlistResponse.getJSON_DATA().get(0);
                            Toast.makeText(AuctionActivity.this, "Deleted from Wishlist: " + message.getMsg() , Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AuctionActivity.this, "Empty response body or jsonData list", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(AuctionActivity.this, "Failed to delete from Wishlist. Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<WishlistResponse> call, Throwable t) {
                    Toast.makeText(AuctionActivity.this, "Failed to  delete from  Wishlist. Response code: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception ignore){
        }

    }


    private Call<WishlistResponse> calldeletefromWishlist() {
        return apiinfoservice.updateWishlist(savePref.getUserId(), itemid,  "delete");
    }
    private Call<WishlistResponse> calladdtoWishlist() {
        return apiinfoservice.updateWishlist(savePref.getUserId(), itemid,  "add");
    }

    public void getReview(){
        loading.setVisibility(VISIBLE);
        mainlayout.setVisibility(View.INVISIBLE);

        try{
            callGetApiReview().enqueue(new Callback<ReviewModel>() {
                @Override
                public void onResponse(Call<ReviewModel> call, Response<ReviewModel> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            ArrayList<AllReviewer> reviewList = response.body().getJSON_DATA();
                            if (reviewList != null && !reviewList.isEmpty()) {
                                ReviewAdapter adapter = new ReviewAdapter(AuctionActivity.this, reviewList);
                                review_RecyclerView.setAdapter(adapter);
                            } else {
                                noreviewtxt.setVisibility(View.VISIBLE);
                            }
                        } else {
                            reviewfailure.setVisibility(View.VISIBLE);
                            Log.e("NewAuctionActivity", "Unsuccessful API response: " + response.code());
                        }
                    loading.setVisibility(GONE);
                    mainlayout.setVisibility(VISIBLE);


                }
                @Override
                public void onFailure(Call<ReviewModel> call, Throwable t) {
                    reviewfailure.setVisibility(VISIBLE);
                    loading.setVisibility(GONE);
                    mainlayout.setVisibility(VISIBLE);

                }
            });
        }catch (Exception ignore){
            loading.setVisibility(GONE);
            mainlayout.setVisibility(VISIBLE);

        }
    }






    public void getprofile() {
        loading.setVisibility(VISIBLE);
        mainlayout.setVisibility(View.INVISIBLE);

        try {
            callgetApi().enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                    try {
                        ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                        totalwallet = arrayList.get(0).getWallet();
                        walletbal.setText(totalwallet);
                        loading.setVisibility(GONE);
                        mainlayout.setVisibility(VISIBLE);


                    } catch (Exception ignore) {  loading.setVisibility(GONE);
                        mainlayout.setVisibility(VISIBLE);
                    }
                }

                @Override public void onFailure(Call<UserProfile> call, Throwable t) {  loading.setVisibility(GONE);
                    mainlayout.setVisibility(VISIBLE);
                }
            });
        } catch (Exception ignore) {  loading.setVisibility(GONE);
            mainlayout.setVisibility(VISIBLE);
        }
    }

    class CountDownRunner implements Runnable {
        TextView textView;
        String o_etime;

        public CountDownRunner(TextView tx_time, String o_etime) {
            this.textView = tx_time;
            this.o_etime = o_etime;
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork(textView, o_etime);
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception ignore) {}
            }
        }
    }
    public void doWork(final TextView textView, final String o_etime) {
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                gettime();
                startDate=curr_dt_time;
                findDifference(startDate, textView, o_etime);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void gettime() {
        String currentDate= String.valueOf(java.time.LocalDate.now());
        String currentTime= String.valueOf(java.time.LocalTime.now()).substring(0,8);
        curr_dt_time= currentDate + " " + currentTime;
    }

    public void addbid(BidStatusCallback callback) {
        loading.setVisibility(View.VISIBLE);
        try {
            calladdbidApi().enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                    loading.setVisibility(View.GONE); // Move this line here to ensure it's set to GONE only after the response is handled
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            ArrayList<SuccessModel.Suc_Model_Inner> arrayList2 = response.body().getJSON_DATA();
                            Toast.makeText(AuctionActivity.this, arrayList2.get(0).getMsg(), Toast.LENGTH_SHORT).show();
                            String bidStatus = arrayList2.get(0).getBid_status().toString();
                            if (arrayList2.get(0).getSuccess().equalsIgnoreCase("1")) {
                                callback.onBidStatusReceived(bidStatus);
                            } else {
                                Toast.makeText(AuctionActivity.this, "Adding Failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(AuctionActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Log the response status and error body for debugging
                        if (!response.isSuccessful()) {
                            Log.e("addbid", "Response unsuccessful: " + response.code() + " " + response.message());
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("addbid", "Error body: " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            Toast.makeText(AuctionActivity.this, "Response unsuccessful ", Toast.LENGTH_SHORT).show();
                        } else if (response.body() == null) {
                            Toast.makeText(AuctionActivity.this, "Response is null", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SuccessModel> call, Throwable t) {
                    loading.setVisibility(View.GONE); // Ensure this is set to GONE on failure as well
                    t.printStackTrace();
                    Toast.makeText(AuctionActivity.this, "onFailure addbid", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            loading.setVisibility(View.GONE); // Ensure this is set to GONE in case of an exception
            e.printStackTrace();
        }
    }

    private Call<SuccessModel> calladdbidApi() {
        return apiinfoservice.add_bid(savePref.getUserId(), oid, getBids, getAmount);
    }

    private Call<GetBidUser> get_maxvalue() {
        return apiinfoservice.get_max_value(oid);
    }






    private void handledynamiclinks() {
        loading.setVisibility(VISIBLE);
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null)
                            deepLink = pendingDynamicLinkData.getLink();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {@Override public void onFailure(@NonNull Exception e) {}});
    }

    void findDifference(String start_date, TextView textView, String end_date) {
        textView.setText(R.string.string41);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            if (d1 == null || d2 == null) {
                return;
            }

            long difference_In_Time = d2.getTime() - d1.getTime();
            long difference_In_Seconds = (difference_In_Time / 1000) % 60;
            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
            long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
            long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;

            if (difference_In_Seconds <= 0 && difference_In_Minutes <= 0 && difference_In_Hours <= 0 && difference_In_Days <= 0) {
                textView.setVisibility(View.GONE);

                // Safely handle back navigation
                Activity activity;
                Context context = textView.getContext();
                if (context instanceof Activity) {
                    activity = (Activity) context;
                } else {
                    activity = null;
                }

                if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                    activity.runOnUiThread(() -> {
                        FragmentManager fragmentManager = activity.getFragmentManager();
                        if (fragmentManager != null && fragmentManager.getBackStackEntryCount() > 0) {
                            fragmentManager.popBackStack();
                        } else {
                            activity.onBackPressed();
                        }
                    });
                }
            } else {
                textView.setVisibility(View.VISIBLE);
                StringBuilder timeDifference = new StringBuilder();

                if (difference_In_Days > 0) {
                    timeDifference.append(difference_In_Days).append("d : ");
                }

                if (difference_In_Hours > 0 || difference_In_Days > 0) {
                    timeDifference.append(String.format("%02dh : ", difference_In_Hours));
                }

                if (difference_In_Minutes > 0 || difference_In_Hours > 0 || difference_In_Days > 0) {
                    timeDifference.append(String.format("%02dm : ", difference_In_Minutes));
                }
                timeDifference.append(String.format("%02ds", difference_In_Seconds));

                textView.setText(timeDifference.toString());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Call<AllBidder> callofferApi() {
        return apiinfoservice.get_offers_id(oid, savePref.getUserId());
    }



    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected())
            getprofile();
        else{
            Intent intent = new Intent(getApplicationContext(), NoInternetActivity.class);
            startActivity(intent);
        }
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();

    }
        private boolean isValidNumber(String str) {
            if (str == null || str.isEmpty()) {
                return false;
            }
            try {
                Double.parseDouble(str);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }}
    private double parseDoubleSafely(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0; // Default value in case of empty or null string
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0; // Default value in case of parsing error
        }
    }


    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration ,getBaseContext().getResources().getDisplayMetrics());
    }


    @Override
    protected void onResume() {
        super.onResume();
        countdownStarted=false;
    }

    public void getmoreitems(){
        try {
            apiinfoservice.get_offers_id(oid==null? "1":oid, savePref.getUserId()).enqueue(new Callback<AllBidder>() {
                @Override
                public void onResponse(Call<AllBidder> call, retrofit2.Response<AllBidder> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        AllBidder allBidder = response.body();
                        ArrayList<AllBidderInner> arrayList = allBidder.getJSON_DATA();

                        txt_morelikethis.setVisibility(VISIBLE);
                        moreitems.setVisibility(VISIBLE);
                        moreitems.setAdapter(new AuctionItemAdapter(AuctionActivity.this, (ArrayList<GetCategories.JSONDATum>) arrayList.get(0).getSimiliar_items(),"live",false));
                    }
                }

                @Override
                public void onFailure(Call<AllBidder> call, Throwable t) {
                    txt_morelikethis.setVisibility(GONE);
                    moreitems.setVisibility(GONE);
                }
            });
        } catch (Exception e) {
            txt_morelikethis.setVisibility(GONE);
            moreitems.setVisibility(GONE);
        }
    }
}