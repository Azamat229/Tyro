package com.student.tyro.NotificationService;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.student.tyro.R;
import com.student.tyro.ChatActivity;
import com.student.tyro.SplashActivity;
import com.student.tyro.Util.Constants_Urls;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    int m = 0;
    String language = "", user_id = "", type = "", product_id = "", title = "", message = "";
    JSONObject object;
    private static String CHANNEL_ID = "TYRO STUDENT";
    Bitmap remote_picture = null;
    private static final Pattern UNICODE_HEX_PATTERN = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");
    private static final Pattern UNICODE_OCT_PATTERN = Pattern.compile("\\\\([0-7]{3})");

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            if (remoteMessage != null) {
                Random random = new Random();
                m = random.nextInt(9999 - 1000) + 1000;
                //while App is in BackGround
                if (remoteMessage.getData().size() > 0) {
                    String data = remoteMessage.getData().toString();
                    Map<String, String> params = remoteMessage.getData();
                    object = new JSONObject(params);
                    type = object.getString("type");
                    title = object.getString("title");
                    message = object.getString("message");
                    notification_handle_data();
                }
                //while App is in ForeGround
                if (remoteMessage.getNotification() != null) {
                    String notification = remoteMessage.getNotification().getBody();
                    Log.d("TAGTAGTAG", "onMessageReceived: " + notification);
                    object = new JSONObject(remoteMessage.getData());
                    Log.d("JSON_OBJECT", object.toString());
                    title = object.getString("title");
                    type = object.getString("type");
                    //   description = object.getString("message");
                    notification_handle_data();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("onMessageReceived: ", e.getMessage());
        }
    }

    private void notification_handle_data() {
        try {

            Intent fullScreenIntent;
            if (type.equalsIgnoreCase("Chat message")) {
                String sender_id = object.getString("sender_id");
                String receiver_id = object.getString("receiver_id");
                fullScreenIntent = new Intent(getApplicationContext(), ChatActivity.class);
                fullScreenIntent.putExtra("sender_id", receiver_id);
                fullScreenIntent.putExtra("receiver_id", sender_id);
                fullScreenIntent.putExtra("notify", "1");
                handleNotification(fullScreenIntent, object.getString("message"));
            } else {
                fullScreenIntent = new Intent(this, SplashActivity.class);
                handleNotification(fullScreenIntent, object.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleNotification(Intent intent, String message) {
        if (isAppIsInBackground(this)) {
            int i = message.lastIndexOf('.');
            String fileExtension = message.substring(i + 1);
            if (fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png")) {
                Bitmap bitmap = getBitmapFromURL(Constants_Urls.pic_base_url + message);
                Log.i("the image path is", Constants_Urls.pic_base_url + message);
                createImageNotification(intent, bitmap);
            } else {
                message = message;
                createNotification(intent, message);
            }
        }
    }

    private void createImageNotification(Intent intent, Bitmap message) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder
                .setContentTitle(title)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.app_icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setOngoing(false)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(message))
                .setPriority(Notification.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        //notificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, builder.build());
        notificationManager.notify(m, builder.build());
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

    public static String decodeEmoji(String message) {
        String myString = null;
        try {
            return URLDecoder.decode(
                    message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }

    private void createNotification(Intent intent, String message) {
        intent.putExtra("message", message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, m, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getApplicationContext());

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(message);

        Notification notification;
        CharSequence name = getApplicationContext().getString(R.string.app_name);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker("").setWhen(0)
                .setAutoCancel(true)
                .setChannelId(CHANNEL_ID)
                .setContentTitle(title)
                .setStyle(inboxStyle)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        notificationManager.notify(m, notification);


    }


    public boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            assert am != null;
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            assert am != null;
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    //    @Override
//    public void onNewToken(@NonNull String s) {
//        super.onNewToken(s);
//        Log.d("TOGTOKEN", "onNewToken: "+s);
//    }
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
