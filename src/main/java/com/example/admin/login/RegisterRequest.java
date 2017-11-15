package com.example.admin.login;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://192.168.1.38/api/registro";
    private Map<String, String> params;

    public RegisterRequest(String unombre, String username, String password_c, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("email", username);
        params.put("name", unombre);
        params.put("password", password_c);
    }

    @Override
        public Map<String, String> getParams() {
        return params;
    }
}
