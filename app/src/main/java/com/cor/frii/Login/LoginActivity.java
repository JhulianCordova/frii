package com.cor.frii.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.cor.frii.MainActivity;
import com.cor.frii.R;
import com.cor.frii.persistence.DatabaseClient;
import com.cor.frii.persistence.Session;
import com.cor.frii.persistence.entity.Acount;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private TextView NewAccount,forgottedPassword;
    private TextInputEditText password;
    //--
    public String baseUrl = "http://34.71.251.155";
    private String key = "friibusiness";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.EmailLogin);
        password = findViewById(R.id.txtPassword);
        NewAccount = findViewById(R.id.NewAccountLogin);
        forgottedPassword=findViewById(R.id.ForgottenPasswordLogin);

        final Session s = new Session(getApplicationContext());
        if (s.getToken() > 0) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        NewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewAccountActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        forgottedPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ForgottenPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    // Login
    public void iniciarSesion(View view) {
        final String user = email.getText().toString();
        final String pass = password.getText().toString();

        final Session session = new Session(getApplicationContext());
        int token_app = session.getToken();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            object.put("username", user);
            object.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Metodo de login si es que existe conexion a INTERNET
        String url = this.baseUrl + "/api/auth/obtain_token/";
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String token = response.getString("token");
                            if (!token.equals("")) {

                                JWT parsedJWT = new JWT(token);
                                Claim subscriptionMetaData = parsedJWT.getClaim("user_id");
                                int id_user = subscriptionMetaData.asInt();

                                // Si es que existe el usuario en la DB - pasar al main activity
                                // si no insertar el usuario  -> si es que el TOKEN es valido
                                Acount cuenta = DatabaseClient.getInstance(getApplicationContext())
                                        .getAppDatabase()
                                        .getAcountDao()
                                        .login(user, pass);

                                if (cuenta != null) {
                                    Toast.makeText(getApplicationContext(), "Credenciales validas OK", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    insertarUsuario(token, id_user, pass);
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("id", id_user);
                                    startActivity(intent);
                                    finish();
                                }
                                session.setToken(cuenta.getId());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley post", "error voley" + error.toString());
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                JSONObject obj = new JSONObject(res);
                                System.out.println(obj.toString());

                                Toast.makeText(getApplicationContext(), "Credenciales invalidas", Toast.LENGTH_LONG).show();
                            } catch (UnsupportedEncodingException | JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }) {
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        return super.parseNetworkResponse(response);
                    }
                };

        requestQueue.add(jsonObjectRequest);


        /*DatabaseClient.getInstance(getApplicationContext())
                .getAppDatabase()
                .getAcountDao()
                .deleteById(3);*/
        // Todo - Implementacion  de LOGIN si es que no tiene coneccion a INTERNET
        // code...
    }

    // Implementacion de datos de usuario del servidor
    public void datosUsuario(final String token, int id, final String pass) {

    }


    public void insertarUsuario(final String token, final int id, final String pass) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                JSONObject object = new JSONObject();
                String url = baseUrl + "/api/clients/" + id;
                JsonObjectRequest jsonObjectRequest =
                        new JsonObjectRequest(Request.Method.GET, url, object, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Acount cuenta = new Acount();
                                try {
                                    cuenta.setId(response.getJSONObject("client").getInt("client_id"));
                                    cuenta.setNumDocumento(response.getJSONObject("client").getString("num_document"));
                                    cuenta.setNombre(response.getJSONObject("client").getString("name"));
                                    cuenta.setPhoneTwo(response.getJSONObject("client").getString("phone1"));
                                    cuenta.setPhoneTwo(response.getJSONObject("client").getString("phone2"));
                                    cuenta.setDireccion(response.getJSONObject("client").getString("address"));
                                    cuenta.setEmail(response.getString("user"));
                                    cuenta.setPassword(pass);

                                    DatabaseClient.getInstance(getApplicationContext())
                                            .getAppDatabase()
                                            .getAcountDao()
                                            .addUser(cuenta);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Volley post", "error voley" + error.toString());
                                NetworkResponse response = error.networkResponse;
                                if (error instanceof ServerError && response != null) {
                                    try {
                                        String res = new String(response.data,
                                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                        JSONObject obj = new JSONObject(res);
                                        System.out.println(obj.toString());
                                    } catch (UnsupportedEncodingException | JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<>();
                                Log.d("Voley get", token);
                                headers.put("Authorization", "JWT " + token);
                                headers.put("Content-Type", "application/json");
                                return headers;
                            }
                        };

                requestQueue.add(jsonObjectRequest);

                return null;
            }
        }.execute();
    }
}
