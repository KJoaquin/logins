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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import in.anshul.libray.PasswordEditText;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button iniciar;
    EditText etUsername;
    PasswordEditText etPassword;
    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new Session(this);
        etUsername = (EditText)findViewById(R.id.us);
        etPassword = (PasswordEditText)findViewById(R.id.pas);
        iniciar = (Button)findViewById(R.id.entrar);
        TextView registrar = (TextView)findViewById(R.id.registro);
        registrar.setOnClickListener(this);

        if(session.loggedin()){
            startActivity(new Intent(Login.this,Lista.class));
            finish();
        }

    }

    @Override
    public void onClick(View view) {
        Intent z = new Intent(getApplicationContext(),Registro.class);
        startActivity(z);
        this.finish();
    }


    public void llista(View v){
        final String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("result");
                    Log.e(getClass().getName(), jsonResponse.toString());

                    if (success) {
                        session.setLoggedin(true);
                        String name = jsonResponse.getJSONObject("records").getString("name");
                        String id = jsonResponse.getJSONObject("records").getString("id");

                        Intent intent = new Intent(Login.this, Lista.class);
                        intent.putExtra("id", id);
                        intent.putExtra("name", name);
                        intent.putExtra("username", username);
                        Login.this.startActivity(intent);
                        finish();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setMessage(jsonResponse.optString("message").toString())
                                .setNegativeButton("Reintentar", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        };
        Login_Request loginRequest = new Login_Request(username, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Login.this);
        queue.add(loginRequest);

    }

}
