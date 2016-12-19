package com.example.cameron.tukme;

import android.app.Activity;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Login extends Activity {



    public Login()
    {
        this.url = "http://192.168.56.1/login.php";


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //init variables
        this.etUsername = (EditText)findViewById(R.id.etEmail);
        this.etPassword = (EditText) findViewById(R.id.etPassword);
        this.res = (TextView)findViewById(R.id.response);
        this.btnLogin = (Button)findViewById(R.id.btnLogin);

        this.txtSignUp = (TextView)findViewById(R.id.txtSignUp);
        this.txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(Login.this, Registration.class);
                startActivity(regIntent);
            }
        });

        //set on click listener
        this.btnLogin.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                final RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
                StringRequest stringRequest =  new StringRequest(Request.Method.POST,url,
                        new Response.Listener<String>()
                        {
                            public void onResponse(String response)
                            {
                                res.setText(response);
                                requestQueue.stop();
                            }
                        }, new Response.ErrorListener()
                {
                    public void onErrorResponse(VolleyError error)
                    {
                        res.setText("something is wrong");
                        error.printStackTrace();
                        requestQueue.stop();
                    }
                }
                ) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<String, String>();
                        MyData.put("username", username);
                        MyData.put("password", password);
                        return MyData;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/x-www-form-urlencoded");

                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }


    private Button btnLogin;
    private String url;
    private TextView res;
    private String username;
    private String password;
    private EditText etUsername;
    private EditText etPassword;
    private TextView txtSignUp;

}
