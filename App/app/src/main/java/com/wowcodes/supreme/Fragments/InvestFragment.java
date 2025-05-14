package com.wowcodes.supreme.Fragments;

import static com.wowcodes.supreme.Activity.MainActivity.active;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wowcodes.supreme.Activity.NoInternetActivity;
import com.wowcodes.supreme.Adapter.AllJarsAdapter;
import com.wowcodes.supreme.Adapter.MyJarsAdapter;
import com.wowcodes.supreme.Backpressedlistener;
import com.wowcodes.supreme.Modelclas.GetInvestPlans;
import com.wowcodes.supreme.Modelclas.GetMyPlans;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class InvestFragment extends Fragment implements Backpressedlistener {

    View view;
    SavePref savePref;
    TextView all_plans,my_plans;
    RecyclerView all_plans_rv,my_plans_rv;
    AllJarsAdapter all_plans_adapter;
    MyJarsAdapter my_plans_adapter;
    ArrayList<GetInvestPlans.Get_Invest_Plans_Inner> all_plans_list;
    ArrayList<GetMyPlans.Get_My_Plans_Inner> my_plans_list;
    LinearLayout noplans;
    BindingService videoService;
    public static boolean reload=false;
    public static Backpressedlistener listener;
    public static boolean invester=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        savePref = new SavePref(getContext());
        view = inflater.inflate(R.layout.fragment_invest, container, false);
        all_plans=view.findViewById(R.id.all_plans);
        my_plans=view.findViewById(R.id.my_plans);
        all_plans_rv=view.findViewById(R.id.all_plans_rv);
        my_plans_rv=view.findViewById(R.id.my_plans_rv);
        noplans=view.findViewById(R.id.noplans);

        all_plans_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        my_plans_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);


        new CountDownTimer(10000, 20000) {
            @Override public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {
                //if (!responseReceived)
                //openalertdialog();
            }
        }.start();

        all_plans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all_plans.setBackground(getResources().getDrawable(R.drawable.tab_sel));
                my_plans.setBackground(getResources().getDrawable(R.drawable.tab_not_sel));
                all_plans.setTextColor(getResources().getColor(R.color.whitewhite));
                my_plans.setTextColor(getResources().getColor(R.color.blackblack));
                all_plans_rv.setVisibility(View.VISIBLE);
                my_plans_rv.setVisibility(View.GONE);

                if(all_plans_list==null) {
                    try {
                        videoService.get_jars(savePref.getUserId(), savePref.getCityId()).enqueue(new Callback<GetInvestPlans>() {
                            @Override
                            public void onResponse(Call<GetInvestPlans> call, retrofit2.Response<GetInvestPlans> response) {
                                try {
                                    all_plans_list = response.body().getJSON_DATA();
                                    all_plans_adapter = new AllJarsAdapter(getContext(), all_plans_list);
                                    all_plans_rv.setAdapter(all_plans_adapter);
                                } catch (Exception ignore) {}

                                if (all_plans_list.isEmpty()) {
                                    noplans.setVisibility(View.VISIBLE);
                                    all_plans_rv.setVisibility(View.GONE);
                                } else {
                                    noplans.setVisibility(View.GONE);
                                    all_plans_rv.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Call<GetInvestPlans> call, Throwable t) {}
                        });
                    } catch (Exception ignore) {}
                }
            }
        });

        my_plans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all_plans.setBackground(getResources().getDrawable(R.drawable.tab_not_sel));
                my_plans.setBackground(getResources().getDrawable(R.drawable.tab_sel));
                all_plans.setTextColor(getResources().getColor(R.color.blackblack));
                my_plans.setTextColor(getResources().getColor(R.color.whitewhite));
                all_plans_rv.setVisibility(View.GONE);
                my_plans_rv.setVisibility(View.VISIBLE);

                if(my_plans_list==null || reload) {
                    my_plans_rv.setVisibility(View.GONE);

                    try {
                        videoService.get_my_jars(savePref.getUserId()).enqueue(new Callback<GetMyPlans>() {
                            @Override
                            public void onResponse(Call<GetMyPlans> call, retrofit2.Response<GetMyPlans> response) {
                                try {
                                    my_plans_list = response.body().getJSON_DATA();
                                    my_plans_adapter = new MyJarsAdapter(getContext(), my_plans_list);
                                    my_plans_rv.setAdapter(my_plans_adapter);
                                    my_plans_rv.setVisibility(View.VISIBLE);
                                } catch (Exception ignore) {}

                                if (my_plans_list.isEmpty()) {
                                    noplans.setVisibility(View.VISIBLE);
                                    my_plans_rv.setVisibility(View.GONE);
                                } else {
                                    noplans.setVisibility(View.GONE);
                                    my_plans_rv.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Call<GetMyPlans> call, Throwable t) {}
                        });
                    } catch (Exception ignore) {}
                    reload=false;
                }
            }
        });

        isNetworkConnected();
        return view;
    }

    public void openalertdialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                .setTitle("Slow Internet Connection").setMessage(
                        "Please check your internet connection!");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog alert = dialog.create();
        alert.show();
    }

    private void isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected())
            load_data();
        else {
            Intent intent = new Intent(getContext(), NoInternetActivity.class);
            startActivity(intent);
        }
    }

    public void load_data(){
        all_plans.setBackground(getResources().getDrawable(R.drawable.tab_sel));
        my_plans.setBackground(getResources().getDrawable(R.drawable.tab_not_sel));
        all_plans_rv.setVisibility(View.VISIBLE);
        my_plans_rv.setVisibility(View.GONE);
        noplans.setVisibility(View.GONE);


        try {
            videoService.get_jars(savePref.getUserId(),savePref.getCityId()).enqueue(new Callback<GetInvestPlans>() {
                @Override
                public void onResponse(Call<GetInvestPlans> call, retrofit2.Response<GetInvestPlans> response) {
                    try {
                        all_plans_list=response.body().getJSON_DATA();
                        all_plans_adapter=new AllJarsAdapter(getContext(),all_plans_list);
                        all_plans_rv.setAdapter(all_plans_adapter);

                        if(all_plans_list.isEmpty()){
                            noplans.setVisibility(View.VISIBLE);
                            all_plans_rv.setVisibility(View.GONE);
                        }
                        else {
                            noplans.setVisibility(View.GONE);
                            all_plans_rv.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception ignore){}
                }
                @Override public void onFailure(Call<GetInvestPlans> call, Throwable t) {}
            });
        } catch (Exception ignore){}

        try {
            videoService.get_my_jars(savePref.getUserId()).enqueue(new Callback<GetMyPlans>() {
                @Override
                public void onResponse(Call<GetMyPlans> call, retrofit2.Response<GetMyPlans> response) {
                    try {
                        my_plans_list=response.body().getJSON_DATA();
                        my_plans_adapter=new MyJarsAdapter(getContext(),my_plans_list);
                        my_plans_rv.setAdapter(my_plans_adapter);

                        if(my_plans_list.isEmpty()){
                            noplans.setVisibility(View.VISIBLE);
                            my_plans_rv.setVisibility(View.GONE);
                        }
                        else {
                            noplans.setVisibility(View.GONE);
                            my_plans_rv.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception ignore){}
                }
                @Override public void onFailure(Call<GetMyPlans> call, Throwable t) {}
            });
        } catch (Exception ignore){}
    }

    @Override
    public void onResume() {
        if(reload) {
            all_plans.setBackground(getResources().getDrawable(R.drawable.tab_not_sel));
            my_plans.setBackground(getResources().getDrawable(R.drawable.tab_sel));
            all_plans.setTextColor(getResources().getColor(R.color.black));
            my_plans.setTextColor(getResources().getColor(R.color.white));
            all_plans_rv.setVisibility(View.GONE);
            my_plans_rv.setVisibility(View.GONE);

            try {
                videoService.get_my_jars(savePref.getUserId()).enqueue(new Callback<GetMyPlans>() {
                    @Override
                    public void onResponse(Call<GetMyPlans> call, retrofit2.Response<GetMyPlans> response) {
                        try {
                            my_plans_list = response.body().getJSON_DATA();
                            my_plans_adapter = new MyJarsAdapter(getContext(), my_plans_list);
                            my_plans_rv.setAdapter(my_plans_adapter);
                            my_plans_rv.setVisibility(View.VISIBLE);
                        } catch (Exception ignore) {}

                        if (my_plans_list.isEmpty()) {
                            noplans.setVisibility(View.VISIBLE);
                            my_plans_rv.setVisibility(View.GONE);
                        } else {
                            noplans.setVisibility(View.GONE);
                            my_plans_rv.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<GetMyPlans> call, Throwable t) {
                    }
                });
            } catch (Exception ignore) {}
            reload=false;
        }

        super.onResume();
        listener=this;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag("wallet");
        fragmentTransaction.hide(active).show(fragment);
        fragmentTransaction.commit();
        active=fragment;

        invester=false;
    }

    @Override
    public void onPause() {
        listener=null;
        super.onPause();
    }
}