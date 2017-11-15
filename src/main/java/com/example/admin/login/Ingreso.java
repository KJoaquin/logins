package com.example.admin.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Ingreso extends AppCompatActivity {

    private TextView nombre;
    private EditText id,horas,decripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso);

        if (findViewById(R.id.edvid).isFocusable()) {
            findViewById(R.id.edhora).requestFocus();
        }

        nombre = (TextView)findViewById(R.id.txvProyecto);
        id = (EditText)findViewById(R.id.edvid);
        horas = (EditText)findViewById(R.id.edhora);
        decripcion = (EditText)findViewById(R.id.eddescrip);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String id1 = intent.getStringExtra("id");

        nombre.setText(name);
        id.setText("  "+id1);

        Button iniciar = (Button) findViewById(R.id.enviar);
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_i = id.getText().toString();
                String horas_i = horas.getText().toString();
                String decripcion_i = decripcion.getText().toString();

                Toast.makeText(getApplicationContext(),"Cargando",Toast.LENGTH_SHORT).show();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("result");
                            Log.e(getClass().getName(), jsonResponse.toString());
                            if (success) {
                                String a = jsonResponse.optString("message").toString();
                                AlertDialog.Builder builder = new AlertDialog.Builder(Ingreso.this);
                                builder.setMessage(a)
                                        .setNegativeButton("OK", null)
                                        .create()
                                        .show();
                                horas.setText("");
                                decripcion.setText("");

                            } else {
                                String a = jsonResponse.optString("message").toString();
                                AlertDialog.Builder builder = new AlertDialog.Builder(Ingreso.this);
                                builder.setMessage(a)
                                        .setNegativeButton("Reintentar", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Ingreso.this);
                        builder.setMessage("Registro fallido")
                                .setNegativeButton("Reintentar", null)
                                .create()
                                .show();
                    }
                };

                IngresoRequest registerRequest = new IngresoRequest(id_i, horas_i, decripcion_i, responseListener,errorListener);
                RequestQueue queue = Volley.newRequestQueue(Ingreso.this);
                queue.add(registerRequest);
            }
        });

    }
}
