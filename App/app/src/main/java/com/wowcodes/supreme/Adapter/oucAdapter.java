package com.wowcodes.supreme.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wowcodes.supreme.R;

public class oucAdapter extends RecyclerView.Adapter<oucAdapter.MyViewHolder> {
    int[] oucDataList;
    Context context;

    public oucAdapter(int[] oucDataList, Context c) {
        this.oucDataList = oucDataList;
        this.context=c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ouccard, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        int data = oucDataList[i];
        viewHolder.num.setText(String.valueOf(data));
        viewHolder.card.setBackground(context.getResources().getDrawable(R.drawable.desm1));

        if(i%3==0)
            viewHolder.card.setBackground(context.getResources().getDrawable(R.drawable.desm2));
        if(i==largest())
            viewHolder.card.setBackground(context.getResources().getDrawable(R.drawable.des4));
        if(i == (int)(largest()/2))
            viewHolder.card.setBackground(context.getResources().getDrawable(R.drawable.des3));
        if(data==7 || data==52 || data==16 || data==77)
            viewHolder.card.setBackground(context.getResources().getDrawable(R.drawable.des2));
        if(data==10 || data==4 || data==11 || data==17)
            viewHolder.card.setBackground(context.getResources().getDrawable(R.drawable.des1));
    }

    @Override
    public int getItemCount() {
        return oucDataList.length;
    }

    public int largest(){
        int max=0;
        for(int i=0;i<oucDataList.length;i++){
            if(oucDataList[max]<oucDataList[i])
                max=i;
        }
        return max;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView num;
        RelativeLayout card;
        public MyViewHolder(View itemView) {
            super(itemView);
            num=(TextView) itemView.findViewById(R.id.num);
            card=(RelativeLayout) itemView.findViewById(R.id.card);
        }
    }

}