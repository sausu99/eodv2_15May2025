package com.wowcodes.supreme.Fragments;
import static com.wowcodes.supreme.Activity.MainActivity.active;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.wowcodes.supreme.Activity.ReferralsActivity;
import com.wowcodes.supreme.Backpressedlistener;
import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class ReferralFragment extends Fragment implements Backpressedlistener {

    TextView txtRefercode,txtShare,taptocopy,txtReferrals;
    LinearLayout lvlEarn;
    String code;
    public BindingService videoService;
    SavePref savePref;
    public static Backpressedlistener listener;
    public static boolean referral=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_referral, container, false);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        savePref=new SavePref(getContext());
        txtRefercode=view.findViewById(R.id.txtRefercode);
        txtShare=view.findViewById(R.id.txtShare);
        taptocopy=view.findViewById(R.id.taptocopy);
        txtReferrals=view.findViewById(R.id.txtreferrals);
        lvlEarn =view.findViewById(R.id.linearlay);
        txtShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                    String sAux = "Invite your friends and share code  " + code + "\n\n";
                    sAux = sAux + Uri.parse("https://play.google.com/store/apps/details?id=" + getContext().getApplicationContext().getPackageName());
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Choose one"));
                } catch (Exception ignore) {}
            }
        });

        getprofile();
        taptocopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                    clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Referral Code", txtRefercode.getText());
                clipboard.setPrimaryClip(clip);
            }
        });

        txtReferrals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ReferralsActivity.class);
                i.putExtra("userid",savePref.getUserId());
                getActivity().startActivity(i);
            }
        });

        return view;
    }

    public void getprofile() {
        lvlEarn.setVisibility(View.VISIBLE);
        try {
            callgetApi().enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                    try {
                        lvlEarn.setVisibility(View.GONE);
                        ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                        txtRefercode.setText(arrayList.get(0).getCode());
                        code=arrayList.get(0).getCode();
                    }catch (Exception e){
                        lvlEarn.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<UserProfile> call, Throwable t) {
                    lvlEarn.setVisibility(View.GONE);
                }
            });
        } catch (Exception ignore) {}
    }

    private Call<UserProfile> callgetApi() {
        return videoService.getUserProfile(savePref.getUserId());
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag("wallet");
        fragmentTransaction.hide(active).show(fragment);
        fragmentTransaction.commit();
        active=fragment;
        referral=false;
    }

    @Override
    public void onPause() {
        listener=null;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        listener=this;
    }
}