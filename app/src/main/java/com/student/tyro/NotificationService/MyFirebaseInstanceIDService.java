package com.student.tyro.NotificationService;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    public static String refreshedToken;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Refreshed token", ": " + refreshedToken);
    }
    }
