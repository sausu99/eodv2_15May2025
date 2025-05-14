package com.wowcodes.supreme.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wowcodes.supreme.Modelclas.GetReferrals;
import com.wowcodes.supreme.R;

import java.util.ArrayList;

public class referralAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<GetReferrals.Get_Referrals_Inner> referralsList;

    public referralAdapter(Context context, ArrayList<GetReferrals.Get_Referrals_Inner> referralsList) {
        this.context = context;
        this.referralsList = referralsList;
    }

    @Override
    public int getCount() {
        return referralsList.size();
    }

    @Override
    public Object getItem(int position) {
        return referralsList.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_referral_card, parent,false);
            holderView = new HolderView(convertView);
            convertView.setTag(holderView);
        } else
            holderView = (HolderView) convertView.getTag();

        GetReferrals.Get_Referrals_Inner list = referralsList.get(position);
        holderView.name.setText(list.getName());
        holderView.email.setText(list.getEmail());
        holderView.userid.setText(list.getUser_id());
        holderView.bonus.setText(list.getReferral_bonus());

        return convertView;
    }

    private static class HolderView{
        private final TextView name,email,bonus,userid;

        public HolderView(View view){
            name = view.findViewById(R.id.refname);
            email = view.findViewById(R.id.refemail);
            userid = view.findViewById(R.id.refid);
            bonus = view.findViewById(R.id.refbonus);
        }
    }
}
