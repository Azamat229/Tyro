package com.student.tyro;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.student.tyro.Adapter.ChatHistoryAdapter;
import com.student.tyro.Model.ChatModel;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.Util.ApiCallInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    ImageView send;
    EditText et_send_text;
    String Sender_id, Reciver_id;
    ArrayList<ChatModel> chatModels;
    RecyclerView chatRecyclerView;
    NetworkConnection networkConnection;
    ImageView back, select_image;
    public static int SELECT_FILE = 4;
    public static int REQUEST_CAMERA = 5;
    String picturePath;
    LinearLayout nochat;
    TextView tvnochat;
    public static ChatHistoryAdapter chathistoryAdapter;
    private String notify;

    private BroadcastReceiver mHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Sender_id = getIntent().getStringExtra("sender_id");
            Reciver_id = getIntent().getStringExtra("receiver_id");
            getChatHistory(ChatActivity.this);

        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        networkConnection = new NetworkConnection(ChatActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mHandler, new IntentFilter("FCM-MESSAGE"));
        Sender_id = getIntent().getStringExtra("sender_id");
        Reciver_id = getIntent().getStringExtra("receiver_id");
        notify = getIntent().getStringExtra("notify");
        //  Log.e("chat Sender_id", Sender_id);
        //  Log.e("chat Reciver_id", Reciver_id);
        if (networkConnection.isConnectingToInternet()) {

            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    // Enter your code which you want to execute every 2 second
                    getChatHistory(ChatActivity.this);
                }
            }, 0, 30000);
        } else {
            Toast.makeText(ChatActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }
        et_send_text = findViewById(R.id.et_send_text);
        send = findViewById(R.id.send);
        chatRecyclerView = findViewById(R.id.chat_recycler);
        select_image = findViewById(R.id.img_select);
        back = findViewById(R.id.ivBack);
        nochat = findViewById(R.id.linear_nochat);
        tvnochat = findViewById(R.id.tv_nochat);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notify != null && notify.equals("1")) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    finish();
                }
            }
        });
        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_send_text.getText().toString().isEmpty()) {

                    if (networkConnection.isConnectingToInternet()) {
                        sendMessage();
                    } else {
                        Toast.makeText(ChatActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        serviceReadmessages(Sender_id);
    }

    private void serviceReadmessages(String sender_id) {
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = apiClass.read_chat_messages(sender_id);
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        int status = jsonObject.optInt("status");
                        String message = jsonObject.optString("message");
                        if (status == 1) {
                        } else {
                            Toast.makeText(ChatActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendMessage() {
       /* RequestBody r_sender_id = RequestBody.create(MediaType.parse("multipart/form-data"),"147");
        RequestBody r_receiver_id = RequestBody.create(MediaType.parse("multipart/form-data"), "5");
        RequestBody r_message = RequestBody.create(MediaType.parse("multipart/form-data"),et_send_text.getText().toString());*/
        KProgressHUD hud = KProgressHUD.create(ChatActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimary)
                .show();
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = apiClass.Send_ChatText(Sender_id, Reciver_id, et_send_text.getText().toString());
        //  callRetrofit = apiClass.Send_ChatText(r_sender_id,r_receiver_id,r_message);
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //  progressDoalog.dismiss();
                hud.dismiss();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        int status = jsonObject.optInt("status");
                        String message = jsonObject.optString("message");
                        if (status == 1) {
                            et_send_text.setText("");
                            //Toast.makeText(ChatActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                            getChatHistory(ChatActivity.this);

                        } else {
                            Toast.makeText(ChatActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }/*else {
                    Toast.makeText(activity, ""+response.message(), Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //progressDoalog.dismiss();
                hud.dismiss();
                Toast.makeText(ChatActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getChatHistory(final ChatActivity activity) {
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.chat_history(Sender_id, Reciver_id);
        //Call<JsonElement> call = apiClass.chat_history("147","5");
//        final KProgressHUD hud = KProgressHUD.create(ChatActivity.this)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setBackgroundColor(R.color.colorPrimary)
//                .show();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

//                hud.dismiss();

                if (response.isSuccessful()) {
                    try {
                        Log.e("jvcnxcvb ", response.body().toString());
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            String chat_data = jsonObject.optString("data");
                            System.out.println("services" + chat_data);
                            JSONArray jsonArray = new JSONArray(chat_data);
                            nochat.setVisibility(View.GONE);
                            chatModels = new ArrayList<>();
                            chatRecyclerView.setLayoutManager(new GridLayoutManager(ChatActivity.this, 1));


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                String id = jsonObject1.getString("message_id");
                                String sender_id = jsonObject1.getString("sender_id");
                                String receiver_id = jsonObject1.getString("receiver_id");
                                String message = jsonObject1.getString("message");
                                String read_status = jsonObject1.getString("read_status");
                                String added_at = jsonObject1.getString("added_at");
                                String image = jsonObject1.getString("user_image");
                                String firstname = jsonObject1.getString("firstname");
                                String receiver_name = jsonObject1.getString("receiver_name");
                                String receiver_image = jsonObject1.getString("receiver_image");
                                String updated_at = jsonObject1.getString("updated_at");


                                chatModels.add(new ChatModel(id, sender_id, receiver_id, message, read_status, added_at, image, firstname, receiver_name, receiver_image, updated_at));


                            }
                            chathistoryAdapter = new ChatHistoryAdapter(ChatActivity.this, chatModels, Sender_id);
                            chatRecyclerView.setHasFixedSize(true);
                            chatRecyclerView.setAdapter(chathistoryAdapter);
                            chatRecyclerView.smoothScrollToPosition(chatModels.size());
                            chathistoryAdapter.notifyDataSetChanged();


                        } else if (status.equals("0")) {

                            Log.e("chathistory response", jsonObject.getString("message"));
                            nochat.setVisibility(View.VISIBLE);
                            tvnochat.setText(jsonObject.getString("message"));
                        }


                    } catch (Exception e) {
                        e.printStackTrace();

                        Log.e("shfdsfds", e.toString());
                    }
                }


            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(ChatActivity.this, t.toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void selectImage() {

        final CharSequence[] items = {getString(R.string.takeaphoto), getString(R.string.choosefrmgallery),
                getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
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
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            final Dialog dialog = new Dialog(ChatActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            Window window = dialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            dialog.setContentView(R.layout.activity_send_image);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.show();
            ImageView cancel = dialog.findViewById(R.id.img_cls);
            ImageView send = dialog.findViewById(R.id.img_send);
            ImageView imgview = dialog.findViewById(R.id.viewimage);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    send_Image_Message();
                    dialog.dismiss();
                }
            });
            Bitmap bitmap = null;
            try {
                File f = new File(picturePath);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
                imgview.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 22) {
            if (resultCode == RESULT_OK) {

                //Log.e("profile", "" + profile_img);

            }
        }
    }

    private void send_Image_Message() {
        File file = null;
        MultipartBody.Part image_profile = null;

        if (picturePath != null && !picturePath.isEmpty()) {
            file = new File(picturePath);
            //  RequestBody requestBody = RequestBody.create(MediaType.parse(getMimeType(picturePath)), file);
            RequestBody requestBody = RequestBody.create(MediaType.parse(getMimeType(picturePath)),
                    saveBitmapToFile(file));
            image_profile = MultipartBody.Part.createFormData("message", file.getName(), requestBody);
            Log.d("Image", ">>>>>>>>>>" + image_profile);
        }
        RequestBody r_sender_id = RequestBody.create(MediaType.parse("multipart/form-data"), Sender_id);
        RequestBody r_receiver_id = RequestBody.create(MediaType.parse("multipart/form-data"), Reciver_id);
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        KProgressHUD hud = KProgressHUD.create(ChatActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimary)
                .show();

        Call<JsonElement> callRetrofit = null;
        //   callRetrofit = apiClass.Send_ChatText(Sender_id,Reciver_id,et_send_text.getText().toString());
        callRetrofit = apiClass.Send_ChatImage(r_sender_id, r_receiver_id, image_profile);
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //  progressDoalog.dismiss();
                hud.dismiss();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        int status = jsonObject.optInt("status");
                        String message = jsonObject.optString("message");
                        if (status == 1) {
                            et_send_text.setText("");
                            getChatHistory(ChatActivity.this);
                            // Toast.makeText(ChatActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ChatActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }/*else {
                    Toast.makeText(activity, ""+response.message(), Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //progressDoalog.dismiss();
                hud.dismiss();
                Toast.makeText(ChatActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(ChatActivity.this.getContentResolver(), data.getData());

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = ChatActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = GalleryUriToPath_New.getPath(ChatActivity.this, selectedImage);
                Log.e("Gallery_Path", picturePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //  profile_pic.setImageBitmap(bm);

    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void onCaptureImageResult(Intent data) {
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
        //profile_pic.setImageBitmap(thumbnail);
        // Picasso.get().load(picturePath).into(profile_img);

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

    @Override
    protected void onResume() {
        super.onResume();
        Chating_screen_status.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Chating_screen_status.activityPaused();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mHandler);
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

    @Override
    public void onBackPressed() {
        if (notify != null && notify.equals("1")) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
