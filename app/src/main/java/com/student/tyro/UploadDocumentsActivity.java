package com.student.tyro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.student.tyro.Util.FileCompressor;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.Util.ApiCallInterface;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadDocumentsActivity extends AppCompatActivity {

    Button choose, submit;
    TextView edit_path;

    NetworkConnection networkConnection;
    // Integer REQUEST_CAMERA=1, SELECT_FILE=0;
    private static final int GRANT_LOC_ACCESS = 800;
    public static int SELECT_FILE = 4;
    public static int REQUEST_CAMERA = 5;
    private static final int PICK_IMAGE = 21;
    String imageString = "";
    String picturePath, User_id;
    ImageView back, upload_img;
    File mPhotoFile1;

    FileCompressor mCompressor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_documents);
        SharedPreferences sharedPreferences = getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        User_id = sharedPreferences.getString("User_id", "");
        //Toast.makeText(getApplicationContext(),""+User_id,Toast.LENGTH_LONG).show();
        choose = findViewById(R.id.choose_doc);
        edit_path = findViewById(R.id.img_path);
        submit = findViewById(R.id.btnSubmit);
        back = findViewById(R.id.ivBack);
        upload_img = findViewById(R.id.upload_img);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UploadDocumentsActivity.this, LoginMapActivity.class);
                startActivity(i);
                finish();
            }
        });
        mCompressor = new FileCompressor(this);


        networkConnection = new NetworkConnection(UploadDocumentsActivity.this);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (picturePath == null) {
                    Toast.makeText(UploadDocumentsActivity.this, "Please upload licence", Toast.LENGTH_SHORT).show();
                } else if (networkConnection.isConnectingToInternet()) {
                    try {
                        UploadDocument();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(UploadDocumentsActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void selectImage() {

        final CharSequence[] items = {getString(R.string.takeaphoto), getString(R.string.choosefrmgallery),
                getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadDocumentsActivity.this);
        builder.setTitle(R.string.choosefrmgallery);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.takeaphoto))) {
                    cameraIntent();
                } else if (items[item].equals(getString(R.string.choosefrmgallery))) {
                    galleryIntent();
                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
       /* Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);*/
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_FILE);
    }

    private void cameraIntent() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, REQUEST_CAMERA);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(UploadDocumentsActivity.this.getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE) {
//                onSelectFromGalleryResult(data);
                onSelectFromGalleryResult1(data, 5);
            } else if (requestCode == REQUEST_CAMERA) {
//                onCaptureImageResult(data);
                onCameraImage1(data, 1);
            }
        }

        if (requestCode == 22) {
            if (resultCode == RESULT_OK) {

                //Log.e("profile", "" + profile_img);

            }
        }
    }


    private void onCameraImage1(Intent data, int selector) {
        if (data != null) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            if (bitmap != null) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File localStorage = getExternalFilesDir(null);
                if (localStorage == null) {
                    return;
                }
                String storagePath = localStorage.getAbsolutePath();
                String rootPath = storagePath + "/test";
                String fileName = "/upload.jpg";

                File root = new File(rootPath);
                if (!root.mkdirs()) {
                    Log.i("Test", "This path is already exist: " + root.getAbsolutePath());
                }

                File file = new File(rootPath + fileName);
                try {
                    int permissionCheck = ContextCompat.checkSelfPermission(
                            getApplicationContext(),
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        if (!file.createNewFile()) {
                            Log.i("Test", "This file is already exist: " + file.getAbsolutePath());
                        }
                    }

                    FileOutputStream fo;
                    fo = new FileOutputStream(file);
                    fo.write(bytes.toByteArray());
                    fo.close();

                    picturePath = file.getAbsolutePath();
                    Log.e("Gallery_Path", picturePath);
                    upload_img.setImageBitmap(bitmap);
                    edit_path.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private void onSelectFromGalleryResult1(Intent data, int selector) {
        if (data != null) {
            Uri selectedImage = data.getData();
            try {
                mPhotoFile1 = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                picturePath = mPhotoFile1.getAbsolutePath();
                Log.e("Gallery_Path", picturePath);
                Glide.with(getApplicationContext()).load(mPhotoFile1).into(upload_img);
                edit_path.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(UploadDocumentsActivity.this.getContentResolver(), data.getData());

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = UploadDocumentsActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = GalleryUriToPath_New.getPath(UploadDocumentsActivity.this, selectedImage);
                Log.e("Gallery_Path", picturePath);
                upload_img.setImageBitmap(bm);
                edit_path.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // profile_img.setImageBitmap(bm);

    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void onCaptureImageResult(Intent data) {
        try {
            if (data != null) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    picturePath = destination.getAbsolutePath();

                    Log.e("CameraPath", picturePath);
                    upload_img.setImageBitmap(thumbnail);
                    edit_path.setText("");
                    //  Log.e("CameraPath", destination.getAbsolutePath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // profile_img.setImageBitmap(thumbnail);
                // Picasso.get().load(picturePath).into(profile_img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        Log.d("MIME_TYPE_EXT", extension);
        if (extension != null && extension != "") {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            //  Log.d("MIME_TYPE", type);
        } else {
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            type = fileNameMap.getContentTypeFor(url);
        }
        return type;
    }

    private void UploadDocument() throws NullPointerException {
        File file = null;
        MultipartBody.Part image_profile = null;

        try {
            if (picturePath != null && !picturePath.isEmpty()) {
                file = new File(picturePath);
                //RequestBody requestBody = RequestBody.create(MediaType.parse(getMimeType(picturePath)), file);
                RequestBody requestBody = RequestBody.create(MediaType.parse(getMimeType(picturePath)),
                        saveBitmapToFile(file));
                image_profile = MultipartBody.Part.createFormData("licence", file.getName(), requestBody);
                Log.d("Image", ">>>>>>>>>>" + image_profile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // RequestBody r_full_name = RequestBody.create(MediaType.parse("multipart/form-data"), file_profile_one);
        RequestBody r_full_name1 = RequestBody.create(MediaType.parse("multipart/form-data"), User_id);
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.upload_license(r_full_name1, image_profile);

        final KProgressHUD hud = KProgressHUD.create(UploadDocumentsActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimary)
                .show();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                hud.dismiss();
                if (response.isSuccessful()) {
                    try {
                        Log.e("jvcnxcvb ", response.body().toString());
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            SharedPreferences sharedPreferences = getSharedPreferences("Login_details", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("loginstatus", true);
                            editor.commit();
                            editor.apply();

                            Intent intent = new Intent(UploadDocumentsActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            String user_info = jsonObject.getString("licence_info");
                            JSONObject jsonObject1 = new JSONObject(user_info);
                            String user_id = jsonObject1.getString("user_id");
                            System.out.println("User_id" + user_id);
                            // String user_name= jsonObject1.getString("username");
                            // String user_email = jsonObject1.getString("email");


                        } else if (status.equals("0")) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                        Log.e("shfdsfds", e.toString());
                    }
                }
                hud.dismiss();

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

                Log.e("shdfdsf ", t.toString());

            }
        });
    }

    public File saveBitmapToFile(File file) {
        try {
            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image
            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();
            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;
            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);
            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();
            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return file;
        } catch (Exception e) {
            return null;
        }
    }
}