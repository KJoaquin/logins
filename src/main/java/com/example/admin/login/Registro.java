package com.example.admin.login;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Registro extends AppCompatActivity{


    TextView entrada;
    View viewl;
    TextInputEditText nombre,user1,password1,cpassword1;
    TextInputLayout a0,a1,a2,a3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nombre = (TextInputEditText) findViewById(R.id.nom);
        user1 = (TextInputEditText) findViewById(R.id.us1);
        password1 = (TextInputEditText) findViewById(R.id.pas1);
        cpassword1 = (TextInputEditText) findViewById(R.id.conf_pas1);
        a0 = (TextInputLayout)findViewById(R.id.nombre);
        a1 = (TextInputLayout)findViewById(R.id.usuario1);
        a2 = (TextInputLayout)findViewById(R.id.password1);
        a3 = (TextInputLayout)findViewById(R.id.passwordR);

        entrada = (TextView)findViewById(R.id.inicio1);
        LayoutInflater layoutInflater = getLayoutInflater();
        viewl = layoutInflater.inflate(R.layout.menss,(ViewGroup)findViewById(R.id.layout_m));
        Button iniciar = (Button) findViewById(R.id.entrarx);
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String unombre = nombre.getText().toString();
                String username = user1.getText().toString();
                String password_t = password1.getText().toString();
                String password_c = cpassword1.getText().toString();
                int ap1 = 0,ap2 = 0,ap3 = 0,ap4 = 0,ap0=0;

                if (unombre.isEmpty()) {
                    a0.setError("Campo Obligatorio");
                    ap0 = 0;
                }else {
                    a0.setError(null);
                    ap0 = 1;
                }if (username.isEmpty()) {
                    a1.setError("Campo Obligatorio");
                    ap1 = 0;
                }else {
                    if(!emailValidator(username)){
                        a1.setError("Ingrese un Email Valido");
                        ap1 = 0;
                    }else {
                        a1.setError(null);
                        ap1 = 1;
                    }
                }if(password_t.isEmpty()){
                    a2.setError("Campo Obligatorio");
                    ap2 = 0;
                }else{
                    ap2 = 1;
                    a2.setError(null);
                }if(password_c.isEmpty()){
                    a3.setError("Campo Obligatorio");
                    ap3 = 0;
                }else {
                    a3.setError(null);
                    ap3 = 1;
                }if ( ap2 == 1){
                    if (password_t.equals(password_c)){
                        ap4 = 1;
                        a3.setError(null);
                    }else {
                        ap4 = 0;
                        a3.setError("La contraseña no coniside");
                    }
                }

                if (ap1 == 1 && ap3 == 1 && ap4 == 1 && ap0 == 1 ){
                    Toast.makeText(getApplicationContext(),"Cargando",Toast.LENGTH_SHORT).show();
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("result");
                                Log.e(getClass().getName(), jsonResponse.toString());
                                if (success) {
                                    Toast toast1 = Toast.makeText(getApplicationContext(),"Toast:Gravity.TOP",Toast.LENGTH_SHORT);
                                    toast1.setGravity(Gravity.CENTER,0,0);
                                    toast1.setView(viewl);
                                    toast1.show();

                                    Intent intent = new Intent(Registro.this, Login.class);
                                    Registro.this.startActivity(intent);

                                    Registro.this.finish();
                                } else {
                                    String a = jsonResponse.optString("message").toString();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
                            builder.setMessage("Registro fallido")
                                    .setNegativeButton("Reintentar", null)
                                    .create()
                                    .show();
                        }
                    };

                    RegisterRequest registerRequest = new RegisterRequest(unombre, username, password_c, responseListener,errorListener);
                    RequestQueue queue = Volley.newRequestQueue(Registro.this);
                    queue.add(registerRequest);

                }

            }
        });
    }

    public void dato(View view) {
        Intent aa = new Intent(this,Login.class);
        startActivity(aa);
        finish();

    }

    public boolean emailValidator(String email){
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();


    }
}
