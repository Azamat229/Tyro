package com.student.tyro.Util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by The Dark on 17/12/2017.
 */

public class MySingleton {
    private static MySingleton Minstance;
    private RequestQueue requestQueue ;
    private static Context mctx ;

    private MySingleton(Context context)
    {
        mctx = context ;
        requestQueue = getRequestQueue();
    }

    public static synchronized MySingleton getMinstance (Context context)
    {
        if(Minstance == null)
        {
            Minstance = new MySingleton(context);
        }
        return Minstance;
    }

    public RequestQueue getRequestQueue()
    {
        if(requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;
    }
    public <T>void addRequestQueuex(Request<T> request)
    {
        requestQueue.add(request);
    }

}



