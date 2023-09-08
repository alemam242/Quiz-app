package com.farhan.quizapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import soup.neumorphism.NeumorphCardView;

public class Dashboard extends Fragment {
    GridView gridView;
    ProgressBar progressBar;
    HashMap<String,String> hashMap;
    ArrayList<HashMap<String,String>> arrayList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        gridView = myView.findViewById(R.id.gridView);
        progressBar = myView.findViewById(R.id.progressBar);

        getCategory();

        MyAdapter adapter = new MyAdapter();
        gridView.setAdapter(adapter);

        return myView;
    }

    private class MyAdapter extends BaseAdapter{
        NeumorphCardView cardView;
        ImageView imageView;
        TextView categoryName;

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            View myView = inflater.inflate(R.layout.category_item,viewGroup,false);
            cardView = myView.findViewById(R.id.cardView);
            imageView = myView.findViewById(R.id.imageView);
            categoryName = myView.findViewById(R.id.categoryName);

            hashMap = new HashMap<>();
            hashMap = arrayList.get(i);
            int id = Integer.parseInt(hashMap.get("id"));
            String category = hashMap.get("category");

            categoryName.setText(category);

            if(category.contains("Database"))
            {
                imageView.setImageResource(R.drawable.database);
            } else if (category.contains("Graphics Design")) {
                imageView.setImageResource(R.drawable.graphics);
            }
            else if (category.contains("Programming")) {
                imageView.setImageResource(R.drawable.coding);
            }
            else if (category.contains("Web Technologies")) {
                imageView.setImageResource(R.drawable.web);
            }
            else if (category.contains("Cloud Computing")) {
                imageView.setImageResource(R.drawable.cloud);
            }
            else if (category.contains("Computer Science")) {
                imageView.setImageResource(R.drawable.cs);
            }
            else {
                imageView.setImageResource(R.drawable.book);
            }


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(getActivity(), PlayQuiz.class);
                    myIntent.putExtra("catId", ""+id);
                    myIntent.putExtra("category", category);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(myIntent);

                }
            });


            if(i%2==0)
            {
                Animation LeftToRight = AnimationUtils.loadAnimation(getContext(),R.anim.left_to_right);
                cardView.startAnimation(LeftToRight);
            }
            else {
                Animation RightToLeft = AnimationUtils.loadAnimation(getContext(),R.anim.right_to_left);
                cardView.startAnimation(RightToLeft);
            }

            return myView;
        }
    }



    //
    private void getCategory()
    {
        arrayList = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        String url="https://appservice272.000webhostapp.com/app/getCategory.php";
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.GONE);
                Log.d("response",""+response);
                for (int i=0;i<response.length();i++)
                {
                    try {
                        JSONObject object = response.getJSONObject(i);

                        int id = object.getInt("id");
                        String ctg = object.getString("category");

                        hashMap = new HashMap<>();
                        hashMap.put("id",""+id);
                        hashMap.put("category",ctg);
                        arrayList.add(hashMap);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

            }
        });


        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(arrayRequest);
    }
}