package com.example.admin.login;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Lista extends AppCompatActivity {

    ArrayList<HashMap<String, String>> contactList;
    private Session session;
    private ProgressDialog pDialog;
    String[] Version;

    private String TAG = Lista.class.getSimpleName();
    private ListView lv;
    private static String url = "http://192.168.1.38/api/lista";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);


        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lista);
        new GetContacts().execute();
        session = new Session(this);
        if(!session.loggedin()){
            logout();
        }
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                logout();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(Lista.this,Login.class));
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(Lista.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray contacts = jsonObj.getJSONArray("records");
                    for (int i = 0; i < contacts.length(); i++) {
                        if (pDialog.isShowing())pDialog.dismiss();
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("nombre");

                        Version = new String[]{id,name};

                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);

                        // adding contact to contact list
                        contactList.add(contact);
                        if (pDialog.isShowing())pDialog.dismiss();
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
                if (pDialog.isShowing())pDialog.dismiss();
            } else {
                Log.e(TAG, "No se pudo obtener json del servidor.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Intent z = new Intent(getApplicationContext(),Recarga.class);
                        startActivity(z);
                        finish();

                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //if (pDialog.isShowing())pDialog.dismiss();
            ListAdapter adapter = new SimpleAdapter(Lista.this, contactList, R.layout.activity_item, new String[]{"id", "name"}, new int[]{R.id.lista_texto, R.id.nombre});
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    HashMap<String, String> item = contactList.get(i);
                    String name = item.get("name");
                    String id = item.get("id");
                    Intent intent = new Intent(Lista.this, Ingreso.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    Lista.this.startActivity(intent);
                }
            });
        }

    }

}
