package com.farhan.quizapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    MaterialToolbar toolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;
    View myView;
    CircleImageView headerImage;
    TextView headerTitle,headerEmail,headerUsername,highScore;

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static String Username,FullName,Email,Userid;
    String nm="Guest",unm="guest",eml="guest@gmail.com";
    RequestQueue queue;
    String navItem="";
    boolean Notify = false;

    HashMap<String,String> hashMap;
    public static ArrayList<HashMap<String,String>> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        frameLayout = findViewById(R.id.frameLayout);

        navigationView = findViewById(R.id.navigationView);
        myView = navigationView.getHeaderView(0);

        headerImage = myView.findViewById(R.id.headerImage);
        headerTitle = myView.findViewById(R.id.headerTitle);
        headerUsername = myView.findViewById(R.id.headerUsername);
        headerEmail = myView.findViewById(R.id.headerEmail);
        highScore = myView.findViewById(R.id.highScore);

        sharedPreferences = getSharedPreferences(""+R.string.app_name,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Username = sharedPreferences.getString("username","guest");
        FullName = sharedPreferences.getString("name","guest");
        Email = sharedPreferences.getString("email","null");
        Userid = sharedPreferences.getString("userid","0");


        getHighScore();

        headerTitle.setText(FullName);
        headerUsername.setText(Username);
        headerEmail.setText(Email);



        frameLayout.removeAllViews();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, new Dashboard());
        fragmentTransaction.commit();
        drawerLayout.closeDrawer(GravityCompat.START);




        //this is the original Google Drive link to the image
        String s="https://drive.google.com/file/d/1x-TcdCFm4FkbHl8QrcYjLlC6Gqceb9Y-/view?usp=drive_link";

        String[] p=s.split("/");
        //Create the new image link
        String imageLink="https://drive.google.com/uc?export=download&id="+p[5];
//        Picasso.get().load(imageLink)
//                .into(headerImage);

        // for nav drawer toogle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MainActivity.this,drawerLayout,toolbar,R.string.drawer_close,R.string.drawer_open
        ){
            @Override
            public void onDrawerOpened(View drawerView) {
                /////////////////////////////////
            }
        };
        drawerLayout.addDrawerListener(toggle);
        /////////////////////////

        //toolbar
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.logout)
                {
                    new AlertDialog.Builder(MainActivity.this)
                            .setCancelable(false)
                            .setTitle("LOGOUT")
                            .setMessage("Do you really want to logout?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    editor.clear().commit();
                                    Intent intent = new Intent(getApplicationContext(),Login.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setIcon(R.drawable.warning)
                            .show();
                }


                return false;
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.home)
                {
                    frameLayout.removeAllViews();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.frameLayout, new Dashboard());
                    fragmentTransaction.commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (item.getItemId()==R.id.leaderboard)
                {
                    frameLayout.removeAllViews();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.frameLayout, new Leaderboard());
                    fragmentTransaction.commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (item.getItemId()==R.id.deleteAccount)
                {
                    new AlertDialog.Builder(MainActivity.this)
                            .setCancelable(false)
                            .setTitle("Delete Account")
                            .setMessage("Do you really want to delete your account?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteAccount();
//                                    editor.clear().commit();
                                    Intent intent = new Intent(getApplicationContext(),Login.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setIcon(R.drawable.warning)
                            .show();
                }
                else if (item.getItemId()==R.id.share)
                {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "https://drive.google.com/file/d/1W4pLXuz0RWsEJmw5Lzb0u3GeYD0WuoO9/view?usp=sharing";
                    String shareSub = "Share our app";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "using"));
                    drawerLayout.closeDrawer(GravityCompat.START);
                }


                return true;
            }
        });

    //end of On Create
    }

    private void deleteAccount()
    {
        String url="https://appservice272.000webhostapp.com/app/deleteAccount.php?uid="+Userid;
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String res = response.getString("result");
                    if(res.equals("success"))
                    {
                        editor.clear().commit();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(objectRequest);
    }

    private void getHighScore()
    {
        String url="https://appservice272.000webhostapp.com/app/getHighScore.php?uid="+Userid;
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String res = response.getString("result");
                    if(res.equals("success")) {

                        String score = response.getString("highScore");
                        highScore.setText("High Score: ");
                        highScore.append(""+score);
                    }
                    else
                    {
                        highScore.setText("High Score: ");
                        highScore.append("0");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(objectRequest);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAndRemoveTask();
                    }
                })
                .setIcon(R.drawable.warning)
                .show();
//        super.onBackPressed();
    }


    //end
}