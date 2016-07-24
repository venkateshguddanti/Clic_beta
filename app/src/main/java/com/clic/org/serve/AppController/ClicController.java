package com.clic.org.serve.AppController;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.clic.org.R;
import com.helpshift.All;
import com.helpshift.Core;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Venkatesh on 22-05-2016.
 */
public class ClicController extends Application {

    public static final String TAG = ClicController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    HashMap config = new HashMap();

    private static ClicController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        Core.init(All.getInstance());
        Core.install(this,
                getString(R.string.help_shift_apikey),
                 getString(R.string.help_shift_domainName),
                 getString(R.string.help_shift_appID),
                 config);
       /* Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.e("MyApplication", ex.getMessage());

                Toast.makeText(getApplicationContext(),"Exception catched.."+ex.toString(),Toast.LENGTH_LONG).show();
            }
        });*/
    }

    public static synchronized ClicController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }



        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
