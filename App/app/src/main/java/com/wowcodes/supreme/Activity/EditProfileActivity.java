package com.wowcodes.supreme.Activity;

import static com.wowcodes.supreme.Constants.imageurl;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Modelclas.SuccessModel;
import com.wowcodes.supreme.Modelclas.UserProfile;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;
import com.wowcodes.supreme.StaticData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;

public class EditProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST_CODE = 123;
    RequestBody id,name,email,pass,phonenumber,image;
    MultipartBody.Part partImage;
    String imgPart;
    ImageView imageAdd;
    CircleImageView imageProfile;
    int PICK_IMAGE_REQUEST = 111;
    BindingService videoService;
    LinearLayout lvlEprofile;
    Boolean imgselected = false;
    ProgressDialog dialog;
    private Button buttonUpdate;
    private EditText edPass;
    TextView txtTittle,txtYourName,txtEmailId,txtPhoneNumb;
    private ImageView imgBackk;
    private SavePref savePref;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.bg_gradiantop);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transprant));
        window.setNavigationBarColor(getResources().getColor(R.color.transprant));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.activity_editprofile);

        savePref = new SavePref(EditProfileActivity.this);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

        txtTittle = findViewById(R.id.txtAucname);
        edPass = findViewById(R.id.edPass);
        imgBackk = findViewById(R.id.imgBackk);
        lvlEprofile = findViewById(R.id.linearlay);
        txtTittle.setText(R.string.string11);
        imgBackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        buttonUpdate = findViewById(R.id.buttonUpdate);
        imageAdd = findViewById(R.id.imageAdd);
        imageProfile = findViewById(R.id.imageProfile);
        txtPhoneNumb = findViewById(R.id.txtPhoneNumb);
        txtYourName = findViewById(R.id.edt_fname);
        txtEmailId = findViewById(R.id.txtEmailId);

        getprofile();
        imageAdd.setOnClickListener(view -> {
            if (checkWriteExternalPermission())
                openGallery();
            else
                checkPermissionOfStorage();
        });

        dialog = new ProgressDialog(this);
        dialog.setMessage(getText(R.string.string148));
        dialog.setCancelable(false);
        buttonUpdate.setOnClickListener(view -> {
            if (validation()) {
                if (imgPart != null) {
                    File imagefile = new File(imgPart);
                    image = RequestBody.create(MediaType.parse("multipart/form-data"), imagefile);
                    partImage = MultipartBody.Part.createFormData("image", "abc", image);
                }

                id = RequestBody.create(MediaType.parse("multipart/form-data"), savePref.getUserId());
                name = RequestBody.create(MediaType.parse("multipart/form-data"), txtYourName.getText().toString());
                email = RequestBody.create(MediaType.parse("multipart/form-data"), txtEmailId.getText().toString());
                pass = RequestBody.create(MediaType.parse("multipart/form-data"), edPass.getText().toString());
                phonenumber = RequestBody.create(MediaType.parse("multipart/form-data"), txtPhoneNumb.getText().toString());
                imageAddapi();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri dataimage = data.getData();
            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), dataimage);
                    Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                    imageProfile.setImageBitmap(bitmap);
                    String[] imageprojection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(dataimage, imageprojection, null, null, null);
                    imgselected = true;

                    if (cursor != null) {
                        cursor.moveToFirst();
                        int indexImage = cursor.getColumnIndex(imageprojection[0]);
                        imgPart = cursor.getString(indexImage);
                        cursor.close();
                    }
                }
            } catch (IOException ignore) {}
        }
    }

    private boolean validation () {
        try {
            if (txtEmailId.getText().toString().equalsIgnoreCase(" ") || !isValidMail(txtEmailId.getText().toString())) {
                Toast.makeText(EditProfileActivity.this, R.string.string152, Toast.LENGTH_LONG).show();
                return false;
            } else if (TextUtils.isEmpty(txtYourName.getText().toString())) {
                Toast.makeText(EditProfileActivity.this, R.string.string154, Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (Exception ignore) {}

        return true;
    }

    private boolean isValidMail (String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void getprofile () {
        lvlEprofile.setVisibility(View.VISIBLE);
        try {
            callgetApi().enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                    try {
                        lvlEprofile.setVisibility(View.GONE);
                        ArrayList<UserProfile.User_profile_Inner> arrayList = response.body().getJSON_DATA();
                        txtYourName.setText(arrayList.get(0).getName());
                        txtEmailId.setText(arrayList.get(0).getEmail());
                        txtPhoneNumb.setText(arrayList.get(0).getPhone());
                        edPass.setText(arrayList.get(0).getPassword());
                        if (arrayList.get(0).getImage().equalsIgnoreCase("")) {
                            try {
                                Glide.with(EditProfileActivity.this)
                                        .load(R.drawable.img_user)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .fitCenter()
                                        .into(imageProfile);
                            } catch (Exception ignore) {}
                        } else {
                            try {
                                Glide.with(EditProfileActivity.this)
                                        .load(imageurl + arrayList.get(0).getImage())
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .placeholder(R.drawable.img_user)
                                        .fitCenter()
                                        .into(imageProfile);
                            } catch (Exception ignore) {}
                        }
                    } catch (Exception e) {
                        lvlEprofile.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<UserProfile> call, Throwable t) {
                    lvlEprofile.setVisibility(View.GONE);
                }
            });
        } catch (Exception ignore) {}
    }

    private Call<UserProfile> callgetApi () {
        return videoService.getUserProfile(savePref.getUserId());
    }

    public void imageAddapi () {
        lvlEprofile.setVisibility(View.VISIBLE);
        callimageAddapi().enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, retrofit2.Response<SuccessModel> response) {
                try {
                    ArrayList<SuccessModel.Suc_Model_Inner> arrayList = response.body().getJSON_DATA();
                    Toast.makeText(EditProfileActivity.this, "" + response.body().getJSON_DATA().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                    if (response.body().getJSON_DATA().get(0).getSuccess().equalsIgnoreCase("1")) {
                        StaticData.userProfileList.clear();
                        Intent i = new Intent(EditProfileActivity.this, MainActivity.class);
                        i.putExtra("comefrom", "activity_editprofile");
                        startActivity(i);
                        finish();
                    } else
                        lvlEprofile.setVisibility(View.GONE);
                } catch (Exception ignore) {}
            }

            @Override public void onFailure(Call<SuccessModel> call, Throwable t) {lvlEprofile.setVisibility(View.GONE);}
        });
    }
    private Call<SuccessModel> callimageAddapi () {
        return videoService.postUserProfileUpdate(name, email, phonenumber, partImage, id, pass);
    }

    private static final int RC_STORAGE_PERM = 123; // Request code for permissions

    public void checkPermissionOfStorage() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }

        // Check if permissions are already granted
        if (EasyPermissions.hasPermissions(this, permissions)) {
            openGallery();
        } else {
            // Request permissions with a rationale dialog
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.msg_rationale), // Rationale dialog message
                    RC_STORAGE_PERM,
                    permissions
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Delegating permission result handling to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, new EasyPermissions.PermissionCallbacks() {

            @Override
            public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
                if (requestCode == RC_STORAGE_PERM) {
                    openGallery();
                }
            }

            @Override
            public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
                if (EasyPermissions.somePermissionPermanentlyDenied(EditProfileActivity.this, perms)) {
                    new AppSettingsDialog.Builder(EditProfileActivity.this)
                            .setTitle(getString(R.string.txt_warning)) // Settings dialog title
                            .setRationale(getString(R.string.msg_rationale)) // Rationale message for settings dialog
                            .build()
                            .show();
                }
            }

            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                // This method is redundant and can be omitted.
            }
        });
    }
    private boolean checkWriteExternalPermission() {
        String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = EditProfileActivity.this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

}