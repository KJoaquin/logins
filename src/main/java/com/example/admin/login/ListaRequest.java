package com.example.admin.login;

import android.content.Intent;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class ListaRequest extends StringRequest {
    private static final String LISTA_REQUEST_URL = "http://192.168.1.38/api/lista";
    private Map<String, String> params;

    public ListaRequest(int id, String name, String email, Response.Listener<String> listener) {
        super(Request.Method.POST, LISTA_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id", id+"");
        params.put("name", name);
        params.put("email", email);


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
