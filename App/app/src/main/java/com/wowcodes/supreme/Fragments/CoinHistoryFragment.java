/**
 * The CoinHistoryFragment class is a fragment in an Android app that displays the user's coin history
 * and allows them to perform various actions related to their coins, such as getting more coins,
 * sharing their referral code, and viewing their transaction history.
 */
package com.wowcodes.supreme.Fragments;
import static com.wowcodes.supreme.Activity.MainActivity.active;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wowcodes.supreme.ADSHOW;
import com.wowcodes.supreme.Activity.GetCoinTraActivity;
import com.wowcodes.supreme.Activity.LoginActivity;
import com.wowcodes.supreme.Activity.MainActivity;
import com.wowcodes.supreme.Activity.PurchaseCoinsActivity;
import com.wowcodes.supreme.Activity.RazorpayActivity;
import com.wowcodes.supreme.Activity.RegisterActivity;
import com.wowcodes.supreme.Activity.ScratchRewardsActivity;
import com.wowcodes.supreme.Activity.VisitWebsiteActivity;
import com.wowcodes.supreme.Activity.WalletPassbookActivity;
import com.wowcodes.supreme.Activity.game_spin_wheel;
import com.wowcodes.supreme.Modelclas.GetCoin;
import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;
import com.wowcodes.supreme.StaticData;
import com.wowcodes.supreme.gamesActivity;

import java.util.ArrayList;
import java.util.Objects;


import retrofit2.Call;
import retrofit2.Callback;

public class CoinHistoryFragment extends Fragment {
    //RecyclerView recyclerCoinhist;
    TextView txtGetCoin, txtGetCoinHis,details;
    ImageView imgPrivacy;
    LinearLayout lvlCoinhistr;
    TextView txtShare;
    SavePref savePref;
    TextView txtTran;
    CardView mancard;
    String code;
    ImageView imglogin;
    TextView accesspage;
    public BindingService videoService;
    TextView txtView1, txtView2, txtRegister, txtLoginActi, txtChooseLang,txtLogin;
    EditText edPass;
    LinearLayout Layout1, Layout2;
    TextView next;
    String txtAmount;
    LinearLayout logintop;
    EditText edtEmail;
    TextView bottomtxt;
    TextView username,balance,mobno;
    LinearLayout addmoney;
    RelativeLayout watch,scratch,spin,play,refer,invest;
    BottomNavigationView bottomNavigationView;
    public static boolean redeem=false;
    ArrayList<GetCoin.Get_Coin_Inner> arrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_coin, container, false);
        View view1 = inflater.inflate(R.layout.activity_login, container, false);
        txtTran = view.findViewById(R.id.text_tran);
        imglogin = view1.findViewById(R.id.imglogin);
        accesspage = view1.findViewById(R.id.accesspage);
        txtGetCoin = view.findViewById(R.id.txtGetCoin);
        txtShare = view.findViewById(R.id.txtShare);
        imgPrivacy = view.findViewById(R.id.imgPrivacy);
        txtGetCoinHis = view.findViewById(R.id.txtGetCoinHis);
        next = view.findViewById(R.id.next);
        mancard = view.findViewById(R.id.mancard);
        savePref = new SavePref(getContext());
        //recyclerCoinhist = view.findViewById(R.id.recycler);
        //lvlCoinhistr = view.findViewById(R.id.linearlay);
        txtLoginActi = view1.findViewById(R.id.txtLoginActi);
        txtChooseLang = view1.findViewById(R.id.txtChooseLang);
        txtRegister = view1.findViewById(R.id.txtRegister);
        txtLogin = view1.findViewById(R.id.txtLogin);
        txtView2 = view1.findViewById(R.id.txtView2);
        edPass = view1.findViewById(R.id.edPass);
        Layout1 = view1.findViewById(R.id.Layout1);
        Layout2 = view1.findViewById(R.id.Layout2);
        txtView1 = view1.findViewById(R.id.txtView1);
        logintop=view1.findViewById(R.id.logintop);
        //bottomNavigationView=view.findViewById(R.id.bottom_navigation);
        username=view.findViewById(R.id.username);
        balance=view.findViewById(R.id.balance);
        mobno=view.findViewById(R.id.mobno);
        addmoney=view.findViewById(R.id.addmoney);
        details=view.findViewById(R.id.details);
        watch=view.findViewById(R.id.watch);
        scratch=view.findViewById(R.id.scratch);
        spin=view.findViewById(R.id.spin);
        play=view.findViewById(R.id.play);
        refer=view.findViewById(R.id.refer);
        invest=view.findViewById(R.id.invest);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        //recyclerCoinhist.setLayoutManager(new LinearLayoutManager(getContext()));
        txtLoginActi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    i.putExtra("Decider", "Decide");
                    startActivity(i);
                } catch (Exception ignore) {}
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    i.putExtra("Decider", "Decide");
                    startActivity(i);
                } catch (Exception ignore) {}
            }
        });

        mancard.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openemaildialog();
                    }
                }
        );
        next.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openemaildialog();
                    }
                }
        );
        mobno.setText(savePref.getUserPhone());
        getprofile();
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), GetCoinTraActivity.class));
            }
        });
        addmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PurchaseCoinsActivity.class));
                //openemaildialog();
            }
        });
        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ADSHOW.class));
            }
        });
        scratch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ScratchRewardsActivity.class));
            }
        });
        spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), game_spin_wheel.class));
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), gamesActivity.class));
            }
        });
        refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getParentFragmentManager(); // For AppCompatActivity
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = fragmentManager.findFragmentByTag("referrals");
                fragmentTransaction.hide(active).show(fragment);
                fragmentTransaction.commit();
                active=fragment;
                ReferralFragment.referral=true;
            }
        });
        invest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getParentFragmentManager(); // For AppCompatActivity
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = fragmentManager.findFragmentByTag("investments");
                fragmentTransaction.hide(active).show(fragment);
                fragmentTransaction.commit();
                active=fragment;
                InvestFragment.invester=true;

                //startActivity(new Intent(getContext(),ScratchRewardsActivity.class));
            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(getActivity(), RegisterActivity.class);
                    startActivity(i);
                } catch (Exception ignore) {}
            }
        });
        txtTran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), WalletPassbookActivity.class);
                startActivity(i);
            }
        });
        imgPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), VisitWebsiteActivity.class);
                i.putExtra("url", "https://luckyboli.com/faq/");
                i.putExtra("name", "Faq");
                startActivity(i);
            }
        });
        txtGetCoinHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), GetCoinTraActivity.class);
                startActivity(i);
            }
        });
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
                } catch (Exception ignore) {     }
            }
        });
        getcoinapi();
        if (Objects.equals(savePref.getUserId(), "0")) {
            imglogin.setVisibility(View.GONE);
            accesspage.setVisibility(View.VISIBLE);
            txtRegister.setVisibility(View.VISIBLE);
            txtLogin.setVisibility(View.VISIBLE);
            txtChooseLang.setVisibility(View.GONE);
            txtView2.setVisibility(View.GONE);
            edPass.setVisibility(View.GONE);
            Layout1.setVisibility(View.GONE);
            Layout2.setVisibility(View.GONE);
            txtView1.setVisibility(View.GONE);
            logintop.setVisibility(View.GONE);
            return view1;
        } else
            return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getprofile();
    }

    public void getcoinapi() {
        if(!StaticData.coinfragmentList.isEmpty()) {
            arrayList = StaticData.coinfragmentList;
            setcoindata();
        }
        else {
            callcoinApi().enqueue(new Callback<GetCoin>() {
                @Override public void onResponse(Call<GetCoin> call, retrofit2.Response<GetCoin> response) {
                    if (!isAdded()) return;
                    arrayList = response.body().getJSON_DATA();
                    StaticData.coinfragmentList=arrayList;
                    setcoindata();
                }
                @Override public void onFailure(Call<GetCoin> call, Throwable t) {
                    if (!isAdded()) return;                  }
            });
        }
    }

    public void setcoindata(){
        try {
            if(!arrayList.isEmpty()) {
                //lvlCoinhistr.setVisibility(View.VISIBLE);
                //recyclerCoinhist.setAdapter(new CoinAdapter(getContext(), arrayList));
            }
        }
        catch (Exception ignore){}
    }

    private Call<GetCoin> callcoinApi() {
        return videoService.get_coin_list();
    }

    public void getprofile() {
        //lvlCoinhistr.setVisibility(View.VISIBLE);
        try {
            callgetApi().enqueue(new Callback<UserProfile>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                    if (!isAdded()) return;  // Ensure fragment is still attached
                    ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                    txtGetCoin.setText(arrayList.get(0).getWallet() + getResources().getString(R.string.string46));
                    balance.setText(String.valueOf(arrayList.get(0).getWallet()));
                    username.setText(arrayList.get(0).getName());
                    code = arrayList.get(0).getCode();
                }

                @Override public void onFailure(Call<UserProfile> call, Throwable t) {
                    if (!isAdded()) return;  // Ensure fragment is still attached

                }
            });
        } catch (Exception ignore) {}
    }

    private Call<UserProfile> callgetApi() {
        return videoService.getUserProfile(savePref.getUserId());
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (callcoinApi() != null) {
            callcoinApi().cancel();  // Cancel any ongoing requests
        }
    }
    private void openemaildialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_purchase);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
        Button ok = dialog.findViewById(R.id.ok);
        TextView curr = dialog.findViewById(R.id.curr);
        TextView d_title = dialog.findViewById(R.id.d_title);
        bottomtxt = dialog.findViewById(R.id.bottomtxt);
        edtEmail = dialog.findViewById(R.id.edtEmail);
        edtEmail.setInputType(InputType.TYPE_CLASS_NUMBER);
        d_title.setText(getText(R.string.string259));
        curr.setText(MainActivity.currency);
        Button cancel = dialog.findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        edtEmail.addTextChangedListener(
                new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    @Override public void afterTextChanged(Editable s) {
                        if ((s != null) && (!s.toString().equals(""))) {
                            int totalcoins = Integer.parseInt(String.valueOf(MainActivity.coinvalue)) * Integer.parseInt(String.valueOf(s));
                            bottomtxt.setText(getText(R.string.manual1) + " " + totalcoins+" "+getText(R.string.string118));
                        }
                    }
                }
        );
        ok.setText("PAY");
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAmount = edtEmail.getText().toString();
                if (TextUtils.isEmpty(txtAmount)) {
                    Toast.makeText(getContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getContext(), RazorpayActivity.class);
                    intent.putExtra("activity", "PurchaseCoin");
                    intent.putExtra("amount", edtEmail.getText().toString());
                    startActivity(intent);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}