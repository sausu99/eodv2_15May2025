package com.wowcodes.supreme.Fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.wowcodes.supreme.Constants.imageurl;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.wowcodes.supreme.Activity.CityDetailActivity;
import com.wowcodes.supreme.Activity.EditProfileActivity;
import com.wowcodes.supreme.Activity.EditProfileActivityForAndroid13;
import com.wowcodes.supreme.Activity.GetOrderActivity;
import com.wowcodes.supreme.Activity.KycUpdateActivity;
import com.wowcodes.supreme.Activity.LoginActivity;
import com.wowcodes.supreme.Activity.MainActivity;
import com.wowcodes.supreme.Activity.MyAddress;
import com.wowcodes.supreme.Activity.MyWishlist;
import com.wowcodes.supreme.Activity.RegisterActivity;
import com.wowcodes.supreme.Activity.ThemeActivity;
import com.wowcodes.supreme.Activity.VisitWebsiteActivity;
import com.wowcodes.supreme.Activity.WalletPassbookActivity;
import com.wowcodes.supreme.Activity.YourBidsActivity;
import com.wowcodes.supreme.Activity.chooseLanguageActivity;
import com.wowcodes.supreme.Adapter.CoinAdapter;
import com.wowcodes.supreme.Constants;
import com.wowcodes.supreme.Modelclas.GetCoin;
import com.wowcodes.supreme.Modelclas.SettingModel;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.Modelclas.getcity;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;
import com.wowcodes.supreme.StaticData;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class YourProfileFragment extends Fragment {
    public BindingService videoService;
    RecyclerView recyclerYoufrag,achievements;
    TextView txtSetName,txtMyWishlist, txtShare, txtMyAddress, txtWallet, txtLogout, txtPurchase, txtBid, txtChooseLang, txtChooseLang1,txtLogin,txtkyc,txtPlays;
    TextView txtAbout, txtCond, txtPrivacy, txtContact, txtHowto, txtRegister, txtLoginActi, accesspage, txtTheme,auctionFrag;
    TextView txtView1, txtView2,chooseCountry,edit_profile,mobile,submit,resubmit,viewall,txtTran,txtGetCoinHis,no_achievements;
    LinearLayout Layout1, Layout2, logintop,lvlYoufrag,verified,rejected,pending,incomplete;
    SavePref savePref;
    String code,status="0";
    CircleImageView imageProfile;
    ImageView imglogin;
    EditText edPass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_profile, container, false);
        View view1 = inflater.inflate(R.layout.activity_login, container, false);
        imglogin = view1.findViewById(R.id.imglogin);
        logintop = view1.findViewById(R.id.logintop);
        txtLoginActi = view1.findViewById(R.id.txtLoginActi);
        accesspage = view1.findViewById(R.id.accesspage);
        txtRegister = view1.findViewById(R.id.txtRegister);
        txtView2 = view1.findViewById(R.id.txtView2);
        edPass = view1.findViewById(R.id.edPass);
        Layout1 = view1.findViewById(R.id.Layout1);
        Layout2 = view1.findViewById(R.id.Layout2);
        txtView1 = view1.findViewById(R.id.txtView1);
        txtChooseLang1 = view1.findViewById(R.id.txtChooseLang);
        savePref = new SavePref(getContext());
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        lvlYoufrag = view.findViewById(R.id.linearlay);
        imageProfile = view.findViewById(R.id.imageProfile);
        txtMyAddress = view.findViewById(R.id.txtMyAddress);
        txtMyWishlist = view.findViewById(R.id.txtMyWishlist);
        txtBid = view.findViewById(R.id.txtBid);
        txtTheme = view.findViewById(R.id.txtChooseTheme);
        recyclerYoufrag = view.findViewById(R.id.recycler);
        txtLogout = view.findViewById(R.id.txtLogout);
        txtLogin=view1.findViewById(R.id.txtLogin);
        txtChooseLang = view.findViewById(R.id.txtChooseLang);
        txtkyc = view.findViewById(R.id.txtkyc);
        txtPurchase = view.findViewById(R.id.txtPurchase);
        txtTran = view.findViewById(R.id.text_tran);
        txtPrivacy = view.findViewById(R.id.txtPrivacy);
        txtContact = view.findViewById(R.id.txtContact);
        txtSetName = view.findViewById(R.id.txtSetName);
        txtShare = view.findViewById(R.id.txtShare);
        txtGetCoinHis = view.findViewById(R.id.txtGetCoinHis);
        chooseCountry = view.findViewById(R.id.txtChooseCountry);
        edit_profile=view.findViewById(R.id.edit_profile);
        mobile=view.findViewById(R.id.txtMobile);
        verified=view.findViewById(R.id.verified);
        rejected=view.findViewById(R.id.rejected);
        resubmit=view.findViewById(R.id.resubmit);
        pending=view.findViewById(R.id.pending);
        incomplete=view.findViewById(R.id.incomplete);
        submit=view.findViewById(R.id.submit);
       /* viewall=view.findViewById(R.id.viewall_achievements);
        achievements=view.findViewById(R.id.recycler_achievements);
        achievements.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        no_achievements=view.findViewById(R.id.nothing); */

        edit_profile.setPaintFlags(edit_profile.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //viewall.setPaintFlags(viewall.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

       /* try {
            videoService.get_achievements(savePref.getUserId()).enqueue(new Callback<GetAchievements>() {
                @Override
                public void onResponse(Call<GetAchievements> call, retrofit2.Response<GetAchievements> response) {
                    ArrayList<GetAchievements.Get_Achievements_Inner> arrayList = response.body().getJSON_DATA();
                    if(arrayList.isEmpty()){
                        achievements.setVisibility(GONE);
                        no_achievements.setVisibility(VISIBLE);
                        viewall.setVisibility(GONE);
                    }
                    else {
                        achievements.setVisibility(VISIBLE);
                        achievements.setAdapter(new AchievementsAdapter(getContext(), arrayList));
                        no_achievements.setVisibility(GONE);
                        viewall.setVisibility(VISIBLE);
                        viewall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(getContext(), ViewAllAchievementsActivity.class));
                            }
                        });
                    }
                }

                @Override public void onFailure(Call<GetAchievements> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
*/

        try {
            videoService.settings().enqueue(new Callback<SettingModel>() {
                @Override
                public void onResponse(Call<SettingModel> call, retrofit2.Response<SettingModel> response) {
                    ArrayList<SettingModel.Setting_model_Inner> arrayList = response.body().getJSON_DATA();

                    // TODO: You can change Contact us Whatsapp number below ;)
                    txtContact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{arrayList.get(0).getSupport_email()});
                            intent.putExtra(Intent.EXTRA_SUBJECT,"");
                            intent.putExtra(Intent.EXTRA_TEXT,"");
                            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                        }
                    });
                }

                @Override public void onFailure(Call<SettingModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}


        chooseCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CityDetailActivity.class);
                startActivity(intent);
            }
        });



        // TODO: You can change the Privacy Policy Link below  ;)
        txtPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), VisitWebsiteActivity.class);
                i.putExtra("url", Constants.retrobaseurl.replace("/seller","")+"privacy-policy.php");
                i.putExtra("name", "Privacy Policy");
                startActivity(i);
            }
        });


        txtChooseLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), chooseLanguageActivity.class);
                i.putExtra("from","you");
                startActivity(i);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), KycUpdateActivity.class));
            }
        });


        resubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), KycUpdateActivity.class));
            }
        });

        txtkyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), KycUpdateActivity.class));
            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();

            }
        });



        txtBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), YourBidsActivity.class);
                startActivity(i);

            }
        });
        txtMyWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), MyWishlist.class);
                startActivity(i);

            }
        });
        txtMyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), MyAddress.class);
                startActivity(i);

            }
        });
        getprofile();
        getcoinapi();

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    i = new Intent(getContext(), EditProfileActivityForAndroid13.class);
                } else {

                    i = new Intent(getContext(), EditProfileActivity.class);
                }
                getContext().startActivity(i);
            }
        });


        txtTran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), WalletPassbookActivity.class);
                getContext().startActivity(i);
            }
        });

        txtPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), GetOrderActivity.class);
                getContext().startActivity(i);
            }
        });


        txtShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                    // TODO: You can change the share app text message from below ;)
                    String sAux = "Invite your friends and share code  " + code + "\n\n";
                    sAux = sAux + Uri.parse("https://play.google.com/store/apps/details?id=" + getContext().getApplicationContext().getPackageName());
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Choose one"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        txtTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ThemeActivity.class);
                startActivity(intent);
            }
        });


        txtLoginActi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    i.putExtra("Decider", "Decide");
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    i.putExtra("Decider", "Decide");
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(getActivity(), RegisterActivity.class);
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (Objects.equals(savePref.getUserId(), "0")) {
            imglogin.setVisibility(GONE);
            accesspage.setVisibility(VISIBLE);
            txtRegister.setVisibility(VISIBLE);
            txtLogin.setVisibility(VISIBLE);
            txtChooseLang.setVisibility(GONE);
            txtView2.setVisibility(GONE);
            edPass.setVisibility(GONE);
            Layout1.setVisibility(GONE);
            Layout2.setVisibility(GONE);
            txtView1.setVisibility(GONE);
            logintop.setVisibility(GONE);
            return view1;
        } else {
            return view;
        }

    }


    public void logOut() {
        // Delete fcm_notification_token for this user and then log out
        try {
            videoService.set_fcm_token(savePref.getUserId(),"").enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                    savePref.setUserPhone("");
                    savePref.setUserId("0");
                    savePref.setemail("");
                    StaticData.userProfileList.clear();

                    GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                    GoogleSignInClient gsc= GoogleSignIn.getClient(getContext(),gso);
                    gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent i = new Intent(getContext(), LoginActivity.class);
                            i.putExtra("back",0);
                            startActivity(i);
                        }
                    });
                }

                @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    public void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(getString(R.string.logoutconfirm));
        alertDialog.setPositiveButton(getString(R.string.logout), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logOut();
            }
        });

        alertDialog.setNegativeButton(getString(R.string.cancel0), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.show();

        // Get the positive (Log Out) and negative (Cancel) buttons after the dialog is shown
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.black));

    }

    public void getprofile() {
        lvlYoufrag.setVisibility(VISIBLE);
        try {
            callgetApi().enqueue(new Callback<UserProfile>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                    try {
                        lvlYoufrag.setVisibility(GONE);
                        ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                        code = arrayList.get(0).getCode();
                        txtSetName.setText(arrayList.get(0).getName());
                        mobile.setText(arrayList.get(0).getPhone());
                        status=arrayList.get(0).getKyc_status();
                        if(status.equalsIgnoreCase("1")){
                            verified.setVisibility(GONE);
                            rejected.setVisibility(GONE);
                            pending.setVisibility(VISIBLE);
                            incomplete.setVisibility(GONE);
                        }
                        else if(status.equalsIgnoreCase("2")){
                            verified.setVisibility(VISIBLE);
                            rejected.setVisibility(GONE);
                            pending.setVisibility(GONE);
                            incomplete.setVisibility(GONE);
                        }
                        else if(status.equalsIgnoreCase("3")){
                            verified.setVisibility(GONE);
                            rejected.setVisibility(VISIBLE);
                            pending.setVisibility(GONE);
                            incomplete.setVisibility(GONE);
                        }
                        else{
                            verified.setVisibility(GONE);
                            rejected.setVisibility(GONE);
                            pending.setVisibility(GONE);
                            incomplete.setVisibility(VISIBLE);
                        }
                        savePref.setUserPhone(arrayList.get(0).getPhone());
                        savePref.setemail(arrayList.get(0).getEmail());
                        if (arrayList.get(0).getImage().equalsIgnoreCase("")) {
                            try {
                                Glide.with(getContext())
                                        .load(R.drawable.img_user)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .fitCenter()
                                        .into(imageProfile);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                Glide.with(getContext())
                                        .load(imageurl + arrayList.get(0).getImage())
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .fitCenter()
                                        .into(imageProfile);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        lvlYoufrag.setVisibility(GONE);

                    }

                }

                @Override
                public void onFailure(Call<UserProfile> call, Throwable t) {
                    lvlYoufrag.setVisibility(GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Call<UserProfile> callgetApi() {
        return videoService.getUserProfile(savePref.getUserId());
    }

    private Call<getcity> callgetcity() {
        return videoService.get_city();
    }

    public void getcoinapi() {
        lvlYoufrag.setVisibility(VISIBLE);
        try {
            callcoinApi().enqueue(new Callback<GetCoin>() {
                @Override
                public void onResponse(Call<GetCoin> call, retrofit2.Response<GetCoin> response) {

                    try {
                        lvlYoufrag.setVisibility(GONE);
                        ArrayList<GetCoin.Get_Coin_Inner> arrayList = response.body().getJSON_DATA();
                        recyclerYoufrag.setAdapter(new CoinAdapter(getContext(), arrayList));
                    } catch (Exception e) {
                        e.printStackTrace();
                        lvlYoufrag.setVisibility(GONE);
                    }
                }

                @Override
                public void onFailure(Call<GetCoin> call, Throwable t) {
                    lvlYoufrag.setVisibility(GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private Call<GetCoin> callcoinApi() {
        return videoService.get_coin_list();
    }



    private void restart() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getActivity().getResources().updateConfiguration(configuration, getActivity().getResources().getDisplayMetrics());
    }

}
