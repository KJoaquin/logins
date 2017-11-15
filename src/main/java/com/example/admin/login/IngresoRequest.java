package com.example.admin.login;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class IngresoRequest  extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://192.168.1.38/api/reporte";
    private Map<String, String> params;

    public IngresoRequest(String id, String horas, String decripcion, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("id", id);
        params.put("horas", horas);
        params.put("resumen", decripcion);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
