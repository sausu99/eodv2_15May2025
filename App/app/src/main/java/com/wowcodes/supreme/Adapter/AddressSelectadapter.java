package com.wowcodes.supreme.Adapter;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Activity.GetOrderActivity;
import com.wowcodes.supreme.Activity.RazorpayActivity;
import com.wowcodes.supreme.Activity.AddAddress;
import com.wowcodes.supreme.Modelclas.AddOrder;
import com.wowcodes.supreme.Modelclas.AddressResponse;
import com.wowcodes.supreme.Modelclas.AddressResponse2;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class AddressSelectadapter extends RecyclerView.Adapter<AddressSelectadapter.ItemViewHolder> {

    private Context context;
    private ArrayList<AddressResponse2.Address> addressesList;
    private String oid,oamt,otype,name,image,claimable;


    public AddressSelectadapter(Context context, ArrayList<AddressResponse2.Address> addressesList, String oid, String oamt, String otype, String name, String image, String claimable) {
        this.context = context;
        this.addressesList = addressesList;
        this.oid = oid;
        this.oamt = oamt;
        this.otype = otype;
        this.name = name;
        this.image = image;
        this.claimable = claimable;
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
        holder.addressLine2Text.setText(address.getAddressLine2());
        holder.cityText.setText(address.getCity() + "," + address.getPostalCode());
        holder.stateText.setText(address.getState() + "," + address.getCountry());

        holder.delete.setOnClickListener(v -> {
            deleteAddress(address.getAddressId(), holder.getAdapterPosition());
        });
        holder.itemView.setOnClickListener(v -> {
            if (Integer.parseInt(otype) == 3) {
                addClaimBid(address.getAddressLine1()+","+address.getAddressLine2()+","+address.getCity() + "," + address.getPostalCode()+","+address.getState() + "," + address.getCountry()+" ");

            } else if (Integer.parseInt(otype)==9) {
                Intent intent = new Intent(context, RazorpayActivity.class);
                intent.putExtra("email", new SavePref(context).getemail());
                intent.putExtra("activity", "CategoryDetailsAct");
                intent.putExtra("amount", oamt);
                intent.putExtra("name", name);
                intent.putExtra("O_id", oid);
                intent.putExtra("link", image);
                intent.putExtra("address_Id", address.getAddressId());
                intent.putExtra("address",address.getAddressLine1()+","+address.getAddressLine2()+","+address.getCity() + "," + address.getPostalCode()+","+address.getState() + "," + address.getCountry());
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
        ImageView typeIcon, edit,delete;
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

    public void addClaimBid(String address) {
        try {
            BindingService apiService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

            apiService.add_order(new SavePref(context).getUserId(), oid, oamt, "", oamt, address,"","2").enqueue(new Callback<AddOrder>() {
                @Override
                public void onResponse(Call<AddOrder> call, Response<AddOrder> response) {
                    ArrayList<AddOrder.Add_Order_Inner> arrayList = response.body().getJSON_DATA();
                    Toast.makeText(context, "" + arrayList.get(0).getMsg(), Toast.LENGTH_SHORT).show();
                    update_api("3",claimable);
                    claimable="";
                }

                @Override
                public void onFailure(Call<AddOrder> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }

    public void update_api(String status,String sid){
        try {
            BindingService apiService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);
            apiService.update_consolation(status,sid).enqueue(new Callback<SuccessModel>() {
                @Override public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                    openConfirmOrderDialog();
                }
                @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
            });
        } catch (Exception ignore) {}
    }
    private void openConfirmOrderDialog() {
        final Dialog animationDialog = new Dialog((Activity) context);
        animationDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        animationDialog.setContentView(R.layout.done_anim_dialog);
        Window animationWindow = animationDialog.getWindow();
        animationWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LottieAnimationView animationView = animationDialog.findViewById(R.id.animationView);
        animationView.setAnimation(R.raw.doneanim);
        animationView.playAnimation();
        final MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ordered);

        Log.d("Dialog", "Showing animation dialog");
        animationDialog.show();

        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                Log.d("Dialog", "Animation started");
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Log.d("Dialog", "Animation ended, dismissing dialog");
                animationDialog.dismiss();
                Log.d("Dialog", "Dialog dismissed, showing order dialog");
                showOrderDialog();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                Log.d("Dialog", "Animation canceled");
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                Log.d("Dialog", "Animation repeated");
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("Dialog", "Media player completed");
                mediaPlayer.release();
            }
        });
    }
    private void showOrderDialog() {
        final Dialog orderDialog = new Dialog(context);
        orderDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        orderDialog.setContentView(R.layout.dialog_orderconfirmed);
        Window window = orderDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        orderDialog.show();

        LinearLayout layout = orderDialog.findViewById(R.id.layoutmypurchases);
        LinearLayout ratingLayout = orderDialog.findViewById(R.id.layoutrateapp);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDialog.dismiss();
                Intent i = new Intent(context, GetOrderActivity.class);
                i.putExtra("page", "1");
                context.startActivity(i);
                ((Activity) context).finish(); // Finish the activity after starting the new one
            }
        });

        ratingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDialog.dismiss();
                ((Activity)context).finish(); // Finish the activity when the dialog is dismissed
            }
        });
        orderDialog.setOnDismissListener(dialog -> ((Activity)context).finish());

    }



}

