package com.wowcodes.supreme.Fragments;

import android.content.Intent;
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
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.SearchActivity;

import java.util.ArrayList;
import java.util.List;
public class AuctionFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView searchviewtext;
    LinearLayout search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_auction, container, false);
        viewPager = view.findViewById(R.id.viewpager);
        search=view.findViewById(R.id.searchll);
        searchviewtext=view.findViewById(R.id.searchviewtext);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchActivity.class));

            }
        });
        searchviewtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchActivity.class));

            }
        });
        if (getActivity() != null && getActivity().getIntent().hasExtra("SELECTED_TAB_INDEX")) {
            viewPager.post(new Runnable() {
                @Override
                public void run() {
                    int tabIndex = getActivity().getIntent().getIntExtra("SELECTED_TAB_INDEX", 0);
                    viewPager.setCurrentItem(tabIndex);
                }
            });
        }
        setupViewPager(viewPager);
        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new LiveFragment(), getString(R.string.auctions));
        adapter.addFragment(new UpcomingFragment(),getString(R.string.raffle));
        adapter.addFragment(new GameZoneFragment(), getString(R.string.marketplace));
        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override public CharSequence getPageTitle(int position) {return mFragmentTitleList.get(position);}
    }
}