/**
 * The ChangepassActivity class is an Android activity that allows users to change their password and
 * makes an API call to update the password in the backend.
 */
package com.wowcodes.supreme.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class ChangepassActivity extends AppCompatActivity {

    EditText edConfrimPass,edPass;
    TextView txtSubmit,mobno;
    ImageView imgBackk;
    SavePref savePref;
    public BindingService videoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);

        edPass=findViewById(R.id.edPass);
        edConfrimPass=findViewById(R.id.edConfrimPass);
        imgBackk=findViewById(R.id.imgBackk);
        mobno=findViewById(R.id.mobno);
        txtSubmit=findViewById(R.id.txtSubmit);
        txtSubmit.setEnabled(false);

        savePref=new SavePref(ChangepassActivity.this);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

        mobno.setText(getString(R.string.string60).replace("mobno",savePref.getUserPhone()));
        txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edPass.getText().toString().equalsIgnoreCase(edConfrimPass.getText().toString()))
                    changepassapi();
                else
                    Toast.makeText(ChangepassActivity.this,R.string.string150, Toast.LENGTH_SHORT).show();
            }
        });

        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        edConfrimPass.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(edConfrimPass.getText().toString().isEmpty() || edPass.getText().toString().isEmpty()){
                    txtSubmit.setEnabled(false);
                    txtSubmit.getBackground().setColorFilter(Color.parseColor("#96b5f2"), PorterDuff.Mode.SRC_ATOP);
                }
                else {
                    txtSubmit.setEnabled(true);
                    txtSubmit.getBackground().setColorFilter(Color.parseColor("#1a48c1"), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        edPass.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(edConfrimPass.getText().toString().isEmpty() || edPass.getText().toString().isEmpty()){
                    txtSubmit.setEnabled(false);
                    txtSubmit.getBackground().setColorFilter(Color.parseColor("#96b5f2"), PorterDuff.Mode.SRC_ATOP);
                }
                else {
                    txtSubmit.setEnabled(true);
                    txtSubmit.getBackground().setColorFilter(Color.parseColor("#1a48c1"), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
    }

    public void changepassapi(){
        try {
            callloginApi().enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                    ArrayList<SuccessModel.Suc_Model_Inner> arrayList= response.body().getJSON_DATA();
                    String msg;
                    msg=arrayList.get(0).getMsg();
                    Toast.makeText(ChangepassActivity.this, ""+msg, Toast.LENGTH_SHORT).show();

                    if (arrayList.get(0).getSuccess().equalsIgnoreCase("1")){
                        Intent i=new Intent(ChangepassActivity.this,LoginActivity.class);
                        startActivity(i);
                    }
                }

                @Override public void onFailure(Call<SuccessModel> call, Throwable t) {}
            });
        }catch (Exception ignore){}
    }

    private Call<SuccessModel> callloginApi() {
        return videoService.change_password(savePref.getUserPhone(),edPass.getText().toString());
    }
}