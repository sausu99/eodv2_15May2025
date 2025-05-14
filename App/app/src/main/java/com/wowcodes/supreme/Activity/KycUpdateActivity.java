package com.wowcodes.supreme.Activity;
import static com.wowcodes.supreme.Constants.imageurl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.Modelclas.GetKycDetails;
import com.wowcodes.supreme.Modelclas.PostKycUpdate;
import com.wowcodes.supreme.R;
import com.wowcodes.supreme.RetrofitUtils.BindingService;
import com.wowcodes.supreme.RetrofitUtils.RetrofitVideoApiBaseUrl;
import com.wowcodes.supreme.SavePref;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KycUpdateActivity extends AppCompatActivity {

    String[] idtypes = {"National ID", "Passport", "Tax ID", "Other"};
    String selectedidtype="";
    TextView updatekyc;
    TextView kycstatus;
    Spinner idtypespinner;
    EditText fname,lname,country,dob,idno;
    ImageView frontimg,backimg;
    String frontimgpart="";
    String backimgpart="";
    boolean back=false;
    BindingService videoService;
    SavePref savePref;
    int status=0;
    boolean opengallery=true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc_update);

        fname=findViewById(R.id.fname);
        lname=findViewById(R.id.lname);
        country=findViewById(R.id.country);
        dob=findViewById(R.id.dob);
        idtypespinner = findViewById(R.id.idtype);
        idno=findViewById(R.id.idno);
        frontimg=findViewById(R.id.addfront);
        backimg=findViewById(R.id.addback);
        updatekyc=findViewById(R.id.updatekyc);
        kycstatus=findViewById(R.id.kycstatus);
        savePref=new SavePref(KycUpdateActivity.this);
        videoService = RetrofitVideoApiBaseUrl.getClient().create(BindingService.class);

        getstatus();

        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, idtypes);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        idtypespinner.setAdapter(ad);
        idtypespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedidtype=idtypes[i];
            }

            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        dob.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i==KeyEvent.KEYCODE_DEL)
                    back=true;
                return false;
            }
        });

        dob.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!back) {
                    if (dob.getText().toString().length() == 2 || dob.getText().toString().length() == 5)
                        dob.setText(dob.getText().toString() + "-");

                    dob.setSelection(dob.getText().toString().length());
                } else
                    back=false;
            }
        });

        frontimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(opengallery) {
                    if (ContextCompat.checkSelfPermission(KycUpdateActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED)
                        ActivityCompat.requestPermissions(KycUpdateActivity.this, new String[] {Manifest.permission.READ_MEDIA_IMAGES}, 52);

                    Intent i = new Intent(Intent.ACTION_PICK);
                    i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 7);
                }
            }
        });

        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(opengallery) {
                    if (ContextCompat.checkSelfPermission(KycUpdateActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED)
                        ActivityCompat.requestPermissions(KycUpdateActivity.this, new String[] {Manifest.permission.READ_MEDIA_IMAGES}, 52);

                    Intent i = new Intent(Intent.ACTION_PICK);
                    i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 10);
                }
            }
        });


        updatekyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status == 0 || status == 3) {
                    if (fname.getText().toString().isEmpty())
                        Toast.makeText(KycUpdateActivity.this, getResources().getString(R.string.enternamefirst), Toast.LENGTH_SHORT).show();
                    if (lname.getText().toString().isEmpty())
                        Toast.makeText(KycUpdateActivity.this, getResources().getString(R.string.enternamefirst), Toast.LENGTH_SHORT).show();
                    else if (country.getText().toString().isEmpty())
                        Toast.makeText(KycUpdateActivity.this, getResources().getString(R.string.entercountryfirst), Toast.LENGTH_SHORT).show();
                    else if (dob.getText().toString().isEmpty())
                        Toast.makeText(KycUpdateActivity.this, getResources().getString(R.string.enterdobfirst), Toast.LENGTH_SHORT).show();
                    else if (!isDateValid())
                        Toast.makeText(KycUpdateActivity.this, getResources().getString(R.string.invaliddate), Toast.LENGTH_SHORT).show();
                    else if (idno.getText().toString().isEmpty())
                        Toast.makeText(KycUpdateActivity.this, getResources().getString(R.string.enteridnofirst), Toast.LENGTH_SHORT).show();
                    else if (frontimgpart.isEmpty())
                        Toast.makeText(KycUpdateActivity.this, getResources().getString(R.string.uploadfrontimg), Toast.LENGTH_SHORT).show();
                    else if (backimgpart.isEmpty())
                        Toast.makeText(KycUpdateActivity.this, getResources().getString(R.string.uploadbackimg), Toast.LENGTH_SHORT).show();
                    else if (frontimgpart.equals(backimgpart))
                        Toast.makeText(KycUpdateActivity.this, getResources().getString(R.string.sameimages), Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(KycUpdateActivity.this, "Uploading Documents!..", Toast.LENGTH_LONG).show();
                        File frontimagefile = new File(frontimgpart);
                        File backimagefile = new File(backimgpart);
                        RequestBody frontimage = RequestBody.create(MediaType.parse("multipart/form-data"), frontimagefile);
                        RequestBody backimage = RequestBody.create(MediaType.parse("multipart/form-data"), backimagefile);
                        MultipartBody.Part frontpartImage = MultipartBody.Part.createFormData("id_front", frontimagefile.getName(), frontimage);
                        MultipartBody.Part backpartImage = MultipartBody.Part.createFormData("id_back", backimagefile.getName(), backimage);
                        RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), savePref.getUserId());
                        RequestBody idtype = RequestBody.create(MediaType.parse("multipart/form-data"), selectedidtype);
                        RequestBody idnumber = RequestBody.create(MediaType.parse("multipart/form-data"), idno.getText().toString());
                        RequestBody idcountry = RequestBody.create(MediaType.parse("multipart/form-data"), country.getText().toString());
                        RequestBody iddob = RequestBody.create(MediaType.parse("multipart/form-data"), dob.getText().toString());
                        RequestBody firstname = RequestBody.create(MediaType.parse("multipart/form-data"), fname.getText().toString());
                        RequestBody lastname = RequestBody.create(MediaType.parse("multipart/form-data"), lname.getText().toString());

                        try {
                            videoService.postKycUpdate(id, idtype, idnumber, frontpartImage, backpartImage, idcountry, iddob, firstname, lastname).enqueue(new Callback<PostKycUpdate>() {
                                @Override
                                public void onResponse(Call<PostKycUpdate> call, Response<PostKycUpdate> response) {
                                    Toast.makeText(KycUpdateActivity.this, getResources().getString(R.string.kycsubmitted), Toast.LENGTH_LONG).show();
                                    kycstatus.setText(getResources().getString(R.string.kycstatuspending));
                                }

                                @Override
                                public void onFailure(Call<PostKycUpdate> call, Throwable t) {
                                    Toast.makeText(KycUpdateActivity.this, "failed : " + t, Toast.LENGTH_SHORT).show();
                                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("PRIZEX : file not found", ""+t);
                                    clipboard.setPrimaryClip(clip);
                                }
                            });
                        } catch (Exception e) {
                            Toast.makeText(KycUpdateActivity.this, "error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                    Toast.makeText(KycUpdateActivity.this, getResources().getString(R.string.cannoteditnow), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isDateValid() {
        DateFormat dtf=new SimpleDateFormat("dd-MM-yyyy");
        dtf.setLenient(false);
        try{
            dtf.parse(dob.getText().toString());
        }
        catch (Exception e){
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            if(requestCode==7){
                frontimg.setImageURI(data.getData());
                frontimgpart=getPathFromURI(KycUpdateActivity.this,data.getData());
            }
            if(requestCode==10){
                backimg.setImageURI(data.getData());
                backimgpart=getPathFromURI(KycUpdateActivity.this,data.getData());
            }
        }
    }

    public void getstatus() {
        try {
            videoService.get_kyc_status(savePref.getUserId()).enqueue(new Callback<GetKycDetails>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<GetKycDetails> call, retrofit2.Response<GetKycDetails> response) {
                    try {
                        ArrayList<GetKycDetails.Get_kycstatus_Inner> arrayList = response.body().getJSON_DATA();

                        status=Integer.parseInt(arrayList.get(0).getKyc_status());
                        if(status==0) {
                            kycstatus.setText(getResources().getString(R.string.kycstatusincomplete));
                            editable();
                        }
                        else if(status==1) {
                            kycstatus.setText(getResources().getString(R.string.kycstatuspending));
                            uneditable();
                            settext();
                        }
                        else if(status==2) {
                            kycstatus.setText(getResources().getString(R.string.kycstatuscomplete));
                            uneditable();
                            settext();
                        }
                        else if(status==3){
                            kycstatus.setText(getResources().getString(R.string.kycstatusrejected));
                            editable();
                            settext();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GetKycDetails> call, Throwable t) {
                    Toast.makeText(KycUpdateActivity.this, getResources().getString(R.string.kyctryafter), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void settext() {
        try {
            videoService.get_kyc_status(savePref.getUserId()).enqueue(new Callback<GetKycDetails>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<GetKycDetails> call, retrofit2.Response<GetKycDetails> response) {
                    try {
                        ArrayList<GetKycDetails.Get_kycstatus_Inner> arrayList = response.body().getJSON_DATA();

                        fname.setText(arrayList.get(0).getId_firstname());
                        lname.setText(arrayList.get(0).getId_lastname());
                        idno.setText(arrayList.get(0).getId_number());
                        country.setText(arrayList.get(0).getId_country());
                        dob.setText(arrayList.get(0).getDob());
                        selectedidtype=arrayList.get(0).getId_type();
                        if(selectedidtype.equalsIgnoreCase(idtypes[0]))
                            idtypespinner.setSelection(0);
                        else if(selectedidtype.equalsIgnoreCase(idtypes[1]))
                            idtypespinner.setSelection(1);
                        else if(selectedidtype.equalsIgnoreCase(idtypes[2]))
                            idtypespinner.setSelection(2);
                        else if(selectedidtype.equalsIgnoreCase(idtypes[3]))
                            idtypespinner.setSelection(3);

                        try {
                            Glide.with(getApplicationContext())
                                    .load(imageurl + arrayList.get(0).getId_front())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .fitCenter()
                                    .into(frontimg);


                            Glide.with(getApplicationContext())
                                    .load(imageurl + arrayList.get(0).getId_back())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .fitCenter()
                                    .into(backimg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GetKycDetails> call, Throwable t) {
                    Toast.makeText(KycUpdateActivity.this, getResources().getString(R.string.kyctryafter), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uneditable(){
        fname.setTextColor(getResources().getColor(R.color.black));
        lname.setTextColor(getResources().getColor(R.color.black));
        dob.setTextColor(getResources().getColor(R.color.black));
        idno.setTextColor(getResources().getColor(R.color.black));
        country.setTextColor(getResources().getColor(R.color.black));

        fname.setEnabled(false);
        lname.setEnabled(false);
        dob.setEnabled(false);
        idno.setEnabled(false);
        country.setEnabled(false);
        idtypespinner.setEnabled(false);
        opengallery=false;
        updatekyc.setVisibility(View.GONE);
    }

    public void editable(){
        fname.setTextColor(getResources().getColor(R.color.black));
        lname.setTextColor(getResources().getColor(R.color.black));
        dob.setTextColor(getResources().getColor(R.color.black));
        idno.setTextColor(getResources().getColor(R.color.black));
        country.setTextColor(getResources().getColor(R.color.black));

        fname.setEnabled(true);
        lname.setEnabled(true);
        dob.setEnabled(true);
        idno.setEnabled(true);
        country.setEnabled(true);
        idtypespinner.setEnabled(true);
        opengallery=true;
        updatekyc.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 52) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, getResources().getString(R.string.permission), Toast.LENGTH_LONG).show();
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_MEDIA_IMAGES }, 77);
            }
        }

        if (requestCode == 77) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, getResources().getString(R.string.permissiondenied), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }


    public static String getPathFromURI(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}


/*

                String[] imageprojection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(data.getData(), imageprojection, null, null, null);

                if (cursor != null) {
                    cursor.moveToFirst();
                    int indexImage = cursor.getColumnIndex(imageprojection[0]);
                    frontimgpart = cursor.getString(indexImage);
                    cursor.close();
                }

 */