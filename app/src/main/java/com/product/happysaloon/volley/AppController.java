package com.product.happysaloon.volley;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;
    private static AppController mInstance;

    public static final String TAG1 = AppController.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    int socketTimeout = 38000;//10 seconds - change to what you want
    int DEFAULT_MAX_RETRIES = 0;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
	/*RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
			DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);*/

    public static synchronized AppController getInstance() {
        return mInstance;
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        VolleyLog.DEBUG = false;
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        VolleyLog.DEBUG = false;
        return this.mImageLoader;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag, Context context) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(policy);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(policy);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public static void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static void cancelPendingRequests() {
        if (mRequestQueue != null) {
            mRequestQueue = null;
            mImageLoader = null;
        }
    }

    public void clear() {
        if (mRequestQueue != null) {
            mRequestQueue.getCache().clear();
            mRequestQueue = null;
        }
    }

    public static void cancelaalrequests() {
        mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }
}
