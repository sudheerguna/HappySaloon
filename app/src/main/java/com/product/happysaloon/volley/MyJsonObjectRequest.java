package com.product.happysaloon.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public  class MyJsonObjectRequest extends JsonObjectRequest {
    /**
     * Creates a new request.
     * @param method        the HTTP method to use
     * @param url           URL to fetch the JSON from
     * @param jsonRequest   A {@link JSONObject} to post with the request. Null is allowed and
     *                      indicates no parameters will be posted along with request.
     * @param listener      Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public MyJsonObjectRequest(int method, String url, JSONObject jsonRequest,
                               Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        super(method, url, (jsonRequest == null) ? null : jsonRequest, listener,
                errorListener);
        Log.e("JSONObject Request", "#" + jsonRequest);
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
//        headers.put("X-XFERS-APP-API-KEY", "v-zZLm-VA1i-ppWVvrL53GsjmxCh8osuiy4ZtrAVYqs");
//        headers.put("Content-Type", "application/json");
        return headers;
    }
}
