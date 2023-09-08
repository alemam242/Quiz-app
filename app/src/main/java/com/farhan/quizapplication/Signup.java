package com.farhan.quizapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Signup extends AppCompatActivity {

    ProgressBar progressBar;
    EditText Name,userName,Email,passWord,conPassword;
    CheckBox showPassword;
    Button signupButton;
    TextView loginText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressBar = findViewById(R.id.progressBar);
        Name = findViewById(R.id.Name);
        userName = findViewById(R.id.userName);
        Email = findViewById(R.id.Email);
        passWord = findViewById(R.id.passWord);
        conPassword = findViewById(R.id.conPassword);
        showPassword = findViewById(R.id.showPassword);
        signupButton = findViewById(R.id.signupButton);
        loginText = findViewById(R.id.loginText);


        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked)
                {
                    //Show Password
                    passWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    conPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    //Hide Password
                    passWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    conPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this,Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name= Name.getText().toString();
                String username= userName.getText().toString();
                String email= Email.getText().toString();
                String password= passWord.getText().toString();
                String conPass= conPassword.getText().toString();

                if(name.isEmpty())
                {
                    Name.setError("Enter Your Name");
                } else if (username.isEmpty()) {
                    Name.setError("Enter Your Username");
                }else if (email.isEmpty()) {
                    Email.setError("Enter Your Email");
                }else if (password.isEmpty()) {
                    passWord.setError("Set Your Password");
                }else if (conPass.isEmpty()) {
                    conPassword.setError("Confirm Your Password");
                }else if (password.length()<5) {
                    passWord.setError("Password must contain 5 characters");
                }else if (conPass.length()<5) {
                    conPassword.setError("Password must contain 5 characters");
                }
                else
                {
                    username = username.toLowerCase();
                    if(!password.equals(conPass))
                    {
                        new AlertDialog.Builder(Signup.this)
                                .setTitle("Invalid Password")
                                .setMessage("Check your password")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .show();
                    }
                    else
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        String url="https://appservice272.000webhostapp.com/app/signup.php?nm="+name+"&un="+username+"&em="+email
                                +"&ps="+password;

                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                if(response.contains("Success")) {
                                    AlertDialog.Builder alertadd = new AlertDialog.Builder(Signup.this);
                                    LayoutInflater factory = LayoutInflater.from(Signup.this);
                                    final View view = factory.inflate(R.layout.after_signup, null);
                                    alertadd.setView(view);
                                    alertadd.setTitle("Account Created");
                                    alertadd.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            Intent intent = new Intent(Signup.this,Login.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    alertadd.show();
                                }
                                else if(response.contains("Failed"))
                                {
                                    signupButton.setText(response);
                                    AlertDialog.Builder alertadd = new AlertDialog.Builder(Signup.this);
                                    LayoutInflater factory = LayoutInflater.from(Signup.this);
                                    final View view = factory.inflate(R.layout.login_failed, null);
                                    alertadd.setView(view);
                                    alertadd.setTitle("Registration Failed");
                                    alertadd.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    alertadd.show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressBar.setVisibility(View.GONE);
                                Log.d("ResponseError",""+error);
                                AlertDialog.Builder alertadd = new AlertDialog.Builder(Signup.this);
                                LayoutInflater factory = LayoutInflater.from(Signup.this);
                                final View view = factory.inflate(R.layout.login_failed, null);
                                alertadd.setView(view);
                                alertadd.setTitle("Registration Failed");
                                alertadd.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                alertadd.show();
                            }
                        });

                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        queue.add(stringRequest);
                    }
                }
            }
        });


    }
}