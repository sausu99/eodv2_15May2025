package com.wowcodes.supreme.Fragments;
import static com.wowcodes.supreme.Activity.MainActivity.active;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wowcodes.supreme.Activity.NoInternetActivity;
import com.wowcodes.supreme.Adapter.WinnersAdapter;
import com.wowcodes.supreme.Backpressedlistener;
import com.wowcodes.supreme.Modelclas.Winners;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WinnerList extends Fragment implements Backpressedlistener {
    public static Backpressedlistener backpressedlistener;
    SavePref savePref;
    BindingService apiwinnerservice;
    ArrayList<Winners.winners_inner> winnerslist;
    RecyclerView winneritemsrecyclerview;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout loadinglayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_winner_list, container, false);
        apiwinnerservice = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        savePref = new SavePref(getContext());
        winnerslist = new ArrayList<>();
        winneritemsrecyclerview = view.findViewById(R.id.winnerrecycler1);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        loadinglayout=view.findViewById(R.id.linearlay);

        // Configure the SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getwinner(); // Refresh data
            }
        });

        getwinner();

        return view;
    }

    @Override
    public void onPause() {
        backpressedlistener = null;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        backpressedlistener = this;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag("home");
        fragmentTransaction.hide(active).show(fragment);
        fragmentTransaction.commit();
        active = fragment;
    }

    public void getwinner() {
        // Show the loading layout before making the network request
        loadinglayout.setVisibility(View.VISIBLE);
        winneritemsrecyclerview.setVisibility(View.GONE); // Hide the recycler view while loading

        callgetwinnerinfo().enqueue(new Callback<Winners>() {
            @Override
            public void onResponse(Call<Winners> call, Response<Winners> response) {
                if (response.body() != null) {
                    ArrayList<Winners.winners_inner> items = (ArrayList<Winners.winners_inner>) response.body().getJSON_DATA();
                    winneritemsrecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    winneritemsrecyclerview.setAdapter(new WinnersAdapter(getContext(), items));

                    // Hide the loading layout and show the RecyclerView after data is fetched
                    loadinglayout.setVisibility(View.GONE);
                    winneritemsrecyclerview.setVisibility(View.VISIBLE);
                }

                // Stop the refreshing animation if swipeRefreshLayout is active
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Winners> call, Throwable t) {
                // Hide the loading layout in case of failure
                loadinglayout.setVisibility(View.GONE);
                winneritemsrecyclerview.setVisibility(View.VISIBLE);  // Show RecyclerView (empty state)
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                // Stop the refreshing animation in case of failure
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {

        } else {
            Intent intent = new Intent(getContext(), NoInternetActivity.class);
            startActivity(intent);
        }
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private Call<Winners> callgetwinnerinfo() {
        return apiwinnerservice.get_winners(savePref.getUserId());
    }

}
