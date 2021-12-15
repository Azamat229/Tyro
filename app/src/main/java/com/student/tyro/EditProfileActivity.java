package com.student.tyro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.student.tyro.Util.ApiCallInterface;
import com.student.tyro.Util.Constants_Urls;
import com.student.tyro.Util.FileCompressor;
import com.student.tyro.Util.HelperClass;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    EditText edtfusername, edtlusername, edtphone, edtemail, edtcity, edtusermailid, edtpswd, edtcpswd, edabt;
    Button save, cancel;
    ImageView img_selectimg;
    public static int SELECT_FILE = 4;
    public static int REQUEST_CAMERA = 5;
    String picturePath, User_id;
    HelperClass helperClass;
    NetworkConnection networkConnection;
    ImageView profile_pic;
    ImageView back;
    String userid, first_username, last_username, password, useremail, userphone, userloc, userpic, abut, strlatitude, strlongitude;

    File mPhotoFile1;

    FileCompressor mCompressor;
    private static final Pattern UNICODE_HEX_PATTERN = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");
    private static final Pattern UNICODE_OCT_PATTERN = Pattern.compile("\\\\([0-7]{3})");

    int AUTOCOMPLETE_REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        edtfusername = findViewById(R.id.et_fusername);
        edtlusername = findViewById(R.id.et_lusername);
        edtphone = findViewById(R.id.et_phone);
        edtemail = findViewById(R.id.et_email);
        edtcity = findViewById(R.id.et_location);
        edtusermailid = findViewById(R.id.et_emailid);
        edtpswd = findViewById(R.id.et_pswd);
        edtcpswd = findViewById(R.id.et_cpswd);
        save = findViewById(R.id.btnSave);
        edabt = findViewById(R.id.et_about);
        cancel = findViewById(R.id.btnCancel);
        img_selectimg = findViewById(R.id.select_pic);
        profile_pic = findViewById(R.id.user_selected_pic);
        back = findViewById(R.id.ivBack);
        helperClass = new HelperClass(EditProfileActivity.this);
        networkConnection = new NetworkConnection(EditProfileActivity.this);
        SharedPreferences sharedPreferences = EditProfileActivity.this.getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        User_id = sharedPreferences.getString("User_id", "");
        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        mCompressor = new FileCompressor(this);

        if (b != null) {
            userid = (String) b.get("user_id");
            first_username = (String) b.get("firstname");
            edtfusername.setText(first_username);
            last_username = (String) b.get("lastname");
            edtlusername.setText(last_username);
            password = (String) b.get("userpswd");
            edtpswd.setText(password);
            edtcpswd.setText(password);
            useremail = (String) b.get("usermail");
            edtemail.setText(useremail);
            edtusermailid.setText(useremail);
            userphone = (String) b.get("userphone");
            edtphone.setText(userphone);
            userloc = (String) b.get("userlocation");
            edtcity.setText(userloc);
            userpic = (String) b.get("user_image");
            Picasso.get().load(Constants_Urls.pic_base_url + userpic)
                    .placeholder(R.drawable.user)
                    .into(profile_pic);
            abut = (String) b.get("aboutus");
            strlatitude = (String) b.get("latitude");
            strlongitude = (String) b.get("longitude");
            edabt.setText(abut);
        }
        img_selectimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        edtcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initilizeValues();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfileActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
              /*  FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, new UsersFragment()).commit();*/
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtfusername.getText().toString().isEmpty()) {
                    showToast("Please enter first name");
                } else if (edtlusername.getText().toString().isEmpty()) {
                    showToast("Please enter last name");
                } else if (edtphone.getText().toString().isEmpty()) {
                    showToast(getString(R.string.enter_number));
                } else if (edtphone.getText().toString().length() < 10) {

                    Toast.makeText(EditProfileActivity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
                } else if (edtemail.getText().toString().isEmpty()) {
                    showToast(getString(R.string.Enter_email));
                } else if (!helperClass.validateEmail(edtemail.getText().toString().trim())) {
                    helperClass.showToast(EditProfileActivity.this, getString(R.string.please_enter_your_valid_email));
                } else if (edtcity.getText().toString().isEmpty()) {
                    showToast(getString(R.string.entercty));
                }
//                else if (edabt.getText().toString().isEmpty()) {
//                    showToast(getString(R.string.abt));
//                }
                else if (edtpswd.getText().toString().isEmpty()) {
                    showToast(getString(R.string.enter_password));
                } else if (edtcpswd.getText().toString().isEmpty()) {
                    showToast(getString(R.string.confirm_password));
                } else if (!edtpswd.getText().toString().equals(edtcpswd.getText().toString())) {
                    Toast.makeText(EditProfileActivity.this, getString(R.string.pwd_not_matching), Toast.LENGTH_SHORT).show();
                } else if (networkConnection.isConnectingToInternet()) {
                    Updateprofile();
                } else {
                    Toast.makeText(EditProfileActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfileActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }


    private void initilizeValues() {

        Places.initialize(getApplicationContext(), "AIzaSyCziqe1Q-d4HMC3D9ZyYDFkBtx8ZHrzGzM");

        PlacesClient placesClient = Places.createClient(this);

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);


    }


    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void selectImage() {

        final CharSequence[] items = {getString(R.string.takeaphoto), getString(R.string.choosefrmgallery),
                getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE)
//                onSelectFromGalleryResult(data);
                onSelectFromGalleryResult1(data, 5);
            else if (requestCode == REQUEST_CAMERA) {
                onCameraImage1(data, 1);
//                onCaptureImageResult(data);
            }
            if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    // text_location.setText(place.getName());
                    edtcity.setText(place.getName());
                    double lat = place.getLatLng().latitude;
                    double lng = place.getLatLng().longitude;

                    strlatitude = String.valueOf(lat);
                    strlongitude = String.valueOf(lng);
                    System.out.println("lattitude" + lat + "," + lng);


                    Log.i("Tag", "Place: " + place.getName() + ", " + place.getId());
                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    // TODO: Handle the error.
                    Status status = Autocomplete.getStatusFromIntent(data);
                    Log.i("Tag", status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
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
                    profile_pic.setImageBitmap(bitmap);
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
                Glide.with(getApplicationContext()).load(mPhotoFile1).into(profile_pic);
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
                bm = MediaStore.Images.Media.getBitmap(EditProfileActivity.this.getContentResolver(), data.getData());

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = EditProfileActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = GalleryUriToPath_New.getPath(EditProfileActivity.this, selectedImage);
                Log.e("Gallery_Path", picturePath);
                profile_pic.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
                    //  Log.e("CameraPath", destination.getAbsolutePath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                profile_pic.setImageBitmap(thumbnail);
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

    public static String encodeEmoji(String message) {
        try {
            return URLEncoder.encode(message,
                    "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }

    public static String decodeFromNonLossyAscii(String original) {
        Matcher matcher = UNICODE_HEX_PATTERN.matcher(original);
        StringBuffer charBuffer = new StringBuffer(original.length());
        while (matcher.find()) {
            String match = matcher.group(1);
            char unicodeChar = (char) Integer.parseInt(match, 16);
            matcher.appendReplacement(charBuffer, Character.toString(unicodeChar));
        }
        matcher.appendTail(charBuffer);
        String parsedUnicode = charBuffer.toString();

        matcher = UNICODE_OCT_PATTERN.matcher(parsedUnicode);
        charBuffer = new StringBuffer(parsedUnicode.length());
        while (matcher.find()) {
            String match = matcher.group(1);
            char unicodeChar = (char) Integer.parseInt(match, 8);
            matcher.appendReplacement(charBuffer, Character.toString(unicodeChar));
        }
        matcher.appendTail(charBuffer);
        return charBuffer.toString();
    }

    public static String encodeToNonLossyAscii(String original) {
        Charset asciiCharset = Charset.forName("US-ASCII");
        if (asciiCharset.newEncoder().canEncode(original)) {
            return original;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < original.length(); i++) {
            char c = original.charAt(i);
            if (c < 128) {
                stringBuffer.append(c);
            } else if (c < 256) {
                String octal = Integer.toOctalString(c);
                stringBuffer.append("\\");
                stringBuffer.append(octal);
            } else {
                String hex = Integer.toHexString(c);
                stringBuffer.append("\\u");
                stringBuffer.append(hex);
            }
        }
        return stringBuffer.toString();
    }

    public static String decodeEmoji(String message) {
        String myString = null;
        try {
            return URLDecoder.decode(
                    message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }

    public void Updateprofile() {

        RequestBody r_current_password;
        File file = null;
        MultipartBody.Part image_profile = null;
        try {
            if (picturePath != null && !picturePath.isEmpty()) {
                file = new File(picturePath);
                RequestBody requestBody = RequestBody.create(MediaType.parse(getMimeType(picturePath)), file);
                image_profile = MultipartBody.Part.createFormData("profile_pic", file.getName(), requestBody);
                Log.d("Image", ">>>>>>>>>>" + image_profile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String bio = edabt.getText().toString();

        RequestBody r_user_id = RequestBody.create(MediaType.parse("multipart/form-data"), User_id);
        RequestBody r_first_name = RequestBody.create(MediaType.parse("multipart/form-data"), edtfusername.getText().toString());
        RequestBody r_last_name = RequestBody.create(MediaType.parse("multipart/form-data"), edtlusername.getText().toString());
        RequestBody r_email = RequestBody.create(MediaType.parse("multipart/form-data"), edtemail.getText().toString());
        RequestBody r__mobile_no = RequestBody.create(MediaType.parse("multipart/form-data"), edtphone.getText().toString());
        r_current_password = RequestBody.create(MediaType.parse("multipart/form-data"), edtpswd.getText().toString());
        RequestBody r_about = RequestBody.create(MediaType.parse("multipart/form-data"), bio);
        RequestBody lat = RequestBody.create(MediaType.parse("multipart/form-data"), strlatitude);
        RequestBody lng = RequestBody.create(MediaType.parse("multipart/form-data"), strlongitude);
        //RequestBody r_confirm_password = RequestBody.create(MediaType.parse("multipart/form-data"),edtcpswd.getText().toString());
        /*if(edtpswd.getText().toString().contains("**********"))
        {
            edtpswd.setText("");
             r_current_password = RequestBody.create(MediaType.parse("multipart/form-data"),edtpswd.getText().toString());
        }
        else
        {
             r_current_password = RequestBody.create(MediaType.parse("multipart/form-data"),edtpswd.getText().toString());
        }
*/
        RequestBody r_address = RequestBody.create(MediaType.parse("multipart/form-data"), edtcity.getText().toString());
        final ApiCallInterface service = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.
                updateuser_profile(r_user_id, r_first_name, r_last_name, r__mobile_no, r_current_password,
                        r_address, r_email, image_profile, r_about,lat,lng);
        final KProgressHUD hud = KProgressHUD.create(EditProfileActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimary)
                .show();

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println("onResponse" + response);
                hud.dismiss();

                if (response.isSuccessful()) {

                    Log.e("onResponse: ", response.body().toString());
                    System.out.println("onResponse" + response.body().toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("1")) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            String user_info = jsonObject.getString("User_info");
                            JSONObject jsonObject1 = new JSONObject(user_info);
                            String user_id = jsonObject1.getString("user_id");
                            String first_name = jsonObject1.getString("firstname");
                            String last_name = jsonObject1.getString("lastname");
                            String email = jsonObject1.getString("email");
                            String mobile_no = jsonObject1.getString("phone");
                            String location = jsonObject1.getString("location");
                            String user_image = jsonObject1.getString("profile_pic");
                            String auth_level = jsonObject1.getString("auth_level");
                            String latitude = jsonObject1.getString("latitude");
                            String longitude = jsonObject1.getString("longitude");
                            SharedPreferences sharedPreferences = getSharedPreferences("Login_details", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("User_id", user_id);
                            editor.putString("login_name", first_name + " " + last_name);
                            editor.putString("login_firstname", first_name);
                            editor.putString("login_email", email);
                            editor.putString("auth_level", auth_level);
                            editor.putString("login_number", mobile_no);
                            editor.putString("User_pic", user_image);
                            editor.putString("location", location);
                            editor.putString("lat", latitude);
                            editor.putString("long", longitude);
                            editor.commit();
                            editor.apply();
                            Intent i = new Intent(EditProfileActivity.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);

                        } else if (status.equals("0")) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {

                        Log.e("error", e.toString());
                    }

                    hud.dismiss();

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                System.out.println("onResponse" + t.toString());
            }
        });

    }

    @Override
    public void onBackPressed() {

//            super.onBackPressed();
//        moveTaskToBack(true);
        Intent i = new Intent(EditProfileActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public static byte[] getStreamByteFromImage(final File imageFile) {

        Bitmap photoBitmap = BitmapFactory.decodeFile(imageFile.getPath());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        int imageRotation = getImageRotation(imageFile);

        if (imageRotation != 0)
            photoBitmap = getBitmapRotatedByDegree(photoBitmap, imageRotation);

        photoBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

        return stream.toByteArray();
    }


    private static int getImageRotation(final File imageFile) {

        ExifInterface exif = null;
        int exifRotation = 0;

        try {
            exif = new ExifInterface(imageFile.getPath());
            exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (exif == null)
            return 0;
        else
            return exifToDegrees(exifRotation);
    }

    private static int exifToDegrees(int rotation) {
        if (rotation == ExifInterface.ORIENTATION_ROTATE_90)
            return 90;
        else if (rotation == ExifInterface.ORIENTATION_ROTATE_180)
            return 180;
        else if (rotation == ExifInterface.ORIENTATION_ROTATE_270)
            return 270;

        return 0;
    }

    private static Bitmap getBitmapRotatedByDegree(Bitmap bitmap, int rotationDegree) {
        Matrix matrix = new Matrix();
        matrix.preRotate(rotationDegree);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
