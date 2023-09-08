package com.farhan.quizapplication;

import android.graphics.Typeface;
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
import android.widget.LinearLayout;
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

public class Leaderboard extends Fragment {

    ProgressBar progressBar;
    LinearLayout board;
    GridView gridView;
    HashMap<String,String> hashMap;
    ArrayList<HashMap<String,String>> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        progressBar = myView.findViewById(R.id.progressBar);
        board = myView.findViewById(R.id.board);
        gridView = myView.findViewById(R.id.gridView);
        board.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        getData();

        MyAdapter adapter = new MyAdapter();
        gridView.setAdapter(adapter);

        return myView;
    }


    private class MyAdapter extends BaseAdapter{

        ImageView topRank;
        TextView rank,name,avgScore;
        LinearLayout rankingLayout;
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
            View myView = inflater.inflate(R.layout.leaderboard_ranking,viewGroup,false);

            topRank = myView.findViewById(R.id.topRank);
            rank = myView.findViewById(R.id.rank);
            name = myView.findViewById(R.id.name);
            rankingLayout = myView.findViewById(R.id.rankingLayout);
            avgScore = myView.findViewById(R.id.avgScore);

            hashMap = new HashMap<>();
            hashMap = arrayList.get(i);

            int id = Integer.parseInt(MainActivity.Userid);
            int uid = Integer.parseInt(hashMap.get("uid"));
            String fullName = hashMap.get("fullName");
            String score = hashMap.get("avgScore");
//            Log.d("response","Name: "+fullName+" Score: "+score);
//            name.setTypeface(Typeface.DEFAULT_BOLD);

            rank.setText(""+(i+1));
            name.setText(fullName);
            avgScore.setText(""+score);

            if(i==0 && Integer.parseInt(score)>0)
            {
                rank.setVisibility(View.GONE);
                topRank.setVisibility(View.VISIBLE);
                topRank.setImageResource(R.drawable.gold);
            }
            else if(i==1 && Integer.parseInt(score)>0)
            {
                rank.setVisibility(View.GONE);
                topRank.setVisibility(View.VISIBLE);
                topRank.setImageResource(R.drawable.silver);
            }
            else if(i==2 && Integer.parseInt(score)>0)
            {
                rank.setVisibility(View.GONE);
                topRank.setVisibility(View.VISIBLE);
                topRank.setImageResource(R.drawable.bronze);
            }
            else
            {
                rank.setVisibility(View.VISIBLE);
                topRank.setVisibility(View.GONE);
            }

            if(uid==id)
            {
                rank.setTypeface(Typeface.DEFAULT_BOLD);
                name.setTypeface(Typeface.DEFAULT_BOLD);
                avgScore.setTypeface(Typeface.DEFAULT_BOLD);
            }


            Animation FadeIn = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
            rankingLayout.startAnimation(FadeIn);
            return myView;
        }
    }

    private void getData()
    {
        arrayList = new ArrayList<>();
        String url="https://appservice272.000webhostapp.com/app/leaderBoard.php";
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.GONE);
                board.setVisibility(View.VISIBLE);

                Log.d("response",""+response);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        if(i==0)
                        {
                            String res = object.getString("result");
                            if(res.equals("success"))
                            {
                                continue;
                            }
                            else
                            {
                                break;
                            }
                        }
                        else
                        {
                            int uid = object.getInt("uid");
                            String name = object.getString("name");
                            String score = object.getString("score");

                            hashMap = new HashMap<>();
                            hashMap.put("uid",""+uid);
                            hashMap.put("fullName",""+name);
                            hashMap.put("avgScore",""+score);
                            arrayList.add(hashMap);
                        }
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