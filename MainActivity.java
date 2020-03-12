package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL ="http://10.0.2.2:3000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
    findViewById(R.id.btlogin).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            handleLoginDialog();
        }
    });
    findViewById(R.id.btsignup).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            handleSignupDialog();
        }
    });
    }

    private void handleLoginDialog() {
        View view =getLayoutInflater().inflate(R.layout.login_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).show();
        Button btnlogin = findViewById(R.id.btlogin);
        final EditText etemail = findViewById(R.id.etemail);
        final EditText etpassword = findViewById(R.id.etpassword);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> map = new HashMap<>();
                map.put("email",etemail.getText().toString());
                map.put("password",etpassword.getText().toString());
                Call<LoginResult> call = retrofitInterface.executeLogin(map);
                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                        if(response.code()==200)
                        {
                            LoginResult result =response.body();

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                            builder1.setTitle(result.getName());
                            builder1.setMessage(result.getEmail());

                            builder1.show();
                        }
                        else if(response.code()==404)
                        {
                            Toast.makeText(MainActivity.this,"Wrong Credentials",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
    private void handleSignupDialog() {
        View view =getLayoutInflater().inflate(R.layout.signup_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).show();

        Button btnsignup = findViewById(R.id.btsignup);
        final EditText etname = findViewById(R.id.etname);
        final EditText etemail = findViewById(R.id.etemail);
        final EditText etpassword = findViewById(R.id.etpassword);
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> map = new HashMap<>();
                map.put("name",etname.getText().toString());
                map.put("email",etemail.getText().toString());
                map.put("password",etpassword.getText().toString());

                Call<Void> call = retrofitInterface.executeSignup(map);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.code()==200)
                            {

                                Toast.makeText(MainActivity.this,"Signud Up Succesfully",Toast.LENGTH_LONG).show();
                            }
                            else if (response.code()==400)
                            {
                                Toast.makeText(MainActivity.this,"Email already Registered",Toast.LENGTH_LONG).show();

                            }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

}
