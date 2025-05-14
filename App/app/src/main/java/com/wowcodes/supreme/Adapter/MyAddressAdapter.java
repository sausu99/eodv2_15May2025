package com.wowcodes.supreme.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Activity.AddAddress;
import com.wowcodes.supreme.Modelclas.AddressResponse;
import com.wowcodes.supreme.Modelclas.AddressResponse2;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAddressAdapter extends RecyclerView.Adapter<MyAddressAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<AddressResponse2.Address> addressesList;
    String oid;
    String oamt;
    String otype;

    public MyAddressAdapter(Context context, ArrayList<AddressResponse2.Address> addressesList, String oid, String oamt, String otype) {
        this.context = context;
        this.addressesList = addressesList;
        this.oid = oid;
        this.oamt = oamt;
        this.otype = otype;
    }



    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.selectaddressadapterlayout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (position >= addressesList.size()) return;
        AddressResponse2.Address address = addressesList.get(position);
        holder.addressLine1Text.setText(address.getAddressLine1());

        String addressType = address.getAddress_type();
        if ("Home".equals(addressType)) {
            holder.addressTypeText.setText(address.getAddress_type());
            Glide.with(context)
                    .load(R.drawable.homeselector)
                    .error(R.drawable.img_background)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(holder.typeIcon);
        } else if ("Work".equals(addressType)) {
            holder.addressTypeText.setText(address.getAddress_type());
            Glide.with(context)
                    .load(R.drawable.office_ic)
                    .error(R.drawable.img_background)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(holder.typeIcon);
        } else if ("Other".equals(addressType)) {
            holder.addressTypeText.setText(address.getNickname());

            Glide.with(context)
                    .load(R.drawable.location)
                    .error(R.drawable.img_background)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(holder.typeIcon);
        }

        holder.addressLine2Text.setText(address.getAddressLine2());
        holder.cityText.setText(address.getCity() + "," + address.getPostalCode());
        holder.stateText.setText(address.getState() + "," + address.getCountry());

        holder.delete.setOnClickListener(v -> {
            deleteAddress(address.getAddressId(), holder.getAdapterPosition());
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddAddress.class);
                intent.putExtra("address_id", address.getAddressId());
                intent.putExtra("address_line1", address.getAddressLine1());
                intent.putExtra("address_line2", address.getAddressLine2());
                intent.putExtra("city", address.getCity());
                intent.putExtra("state", address.getState());
                intent.putExtra("postal_code", address.getPostalCode());
                intent.putExtra("country", address.getCountry());
                intent.putExtra("address_type", address.getAddress_type());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return addressesList != null ? addressesList.size() : 0;
    }

    public void updateAddressList(ArrayList<AddressResponse2.Address> addresses) {
        this.addressesList.clear();
        this.addressesList.addAll(addresses);
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView typeIcon, edit, delete;
        TextView addressTypeText;
        TextView addressLine1Text;
        TextView addressLine2Text;
        TextView cityText;
        TextView stateText;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            typeIcon = itemView.findViewById(R.id.typeic);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            addressTypeText = itemView.findViewById(R.id.addresstypetxt);
            addressLine1Text = itemView.findViewById(R.id.addressline1txt);
            addressLine2Text = itemView.findViewById(R.id.addressline2txt);
            cityText = itemView.findViewById(R.id.citytxt);
            stateText = itemView.findViewById(R.id.statetxt);
        }
    }

    private void deleteAddress(String addressId, int position) {
        BindingService apiService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
        Call<AddressResponse> call = apiService.deleteAddress(new SavePref(context).getUserId(), addressId);

        call.enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AddressResponse deleteResponse = response.body();
                    addressesList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, addressesList.size());
                    Toast.makeText(context, "Address deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete address", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}

