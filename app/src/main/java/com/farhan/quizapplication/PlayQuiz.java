package com.farhan.quizapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayQuiz extends AppCompatActivity {

    LinearLayout optionA,optionB,optionC,optionD,quizRunning,afterQuiz,optionLayout;
    TextView countQuestion,totalQuestion,question,tv1,tv2,tv3,tv4,total;
    TextView countCorrect,countWrong,countRespond,countSkip,score;
    Button nextButton,skipButton,home;
    CheckBox cb1,cb2,cb3,cb4;
    LottieAnimationView animationView,animationView1;
    ProgressBar progressBar;
    ImageView cancel;
    HashMap<String,String> hashMap;
    ArrayList<HashMap<String,String>> arrayList;
    RequestQueue queue;

    int skip=0,correct=0,wrong=0,i=0;
    String ans="",finalAns="";
    String category,catId;

    public interface QuestionCallback {
        void onQuestionsReceived(ArrayList<HashMap<String, String>> questions);
        void onQuestionsError(String errorMessage);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_quiz);

        Bundle bundle = getIntent().getExtras();
         category = bundle.getString("category");
         catId = bundle.getString("catId");


        progressBar = findViewById(R.id.progressBar);
        cancel = findViewById(R.id.cancel);
        quizRunning = findViewById(R.id.quizRunning);
        afterQuiz = findViewById(R.id.afterQuiz);
        optionLayout = findViewById(R.id.optionLayout);

        quizRunning.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        countQuestion = findViewById(R.id.countQuestion);
        totalQuestion = findViewById(R.id.totalQuestion);
        question = findViewById(R.id.question);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        nextButton = findViewById(R.id.nextButton);
        skipButton = findViewById(R.id.skipButton);
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);

        //////////////////
        animationView = findViewById(R.id.animationView);
        animationView1 = findViewById(R.id.animationView1);
        score = findViewById(R.id.score);
        countCorrect = findViewById(R.id.countCorrect);
        countRespond = findViewById(R.id.countRespond);
        countWrong = findViewById(R.id.countWrong);
        countSkip = findViewById(R.id.countSkip);
        total = findViewById(R.id.total);
        home = findViewById(R.id.home);


        ////////////
        getQuestion(catId, new QuestionCallback() {
            @Override
            public void onQuestionsReceived(ArrayList<HashMap<String, String>> questions) {
                Log.d("response", "arrayList outside response function: " + questions);

//                for (i = 0; i < questions.size(); ) {

                setQuestion(questions);

                    skipButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cancel.setVisibility(View.GONE);
                            ++skip;i++;
                            if(i>=10)
                            {
                                quizRunning.setVisibility(View.GONE);
                                afterQuiz.setVisibility(View.VISIBLE);
                                getScore(questions);
                            }
                            else
                            {
                                setQuestion(questions);
                            }
                        }
                    });

                    optionA.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            nextButton.setVisibility(View.VISIBLE);
                            cb1.setChecked(true);
                            cb2.setChecked(false);
                            cb3.setChecked(false);
                            cb4.setChecked(false);
                            ans=tv1.getText().toString();
                        }
                    });
                    optionB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            nextButton.setVisibility(View.VISIBLE);
                            cb1.setChecked(false);
                            cb2.setChecked(true);
                            cb3.setChecked(false);
                            cb4.setChecked(false);
                            ans=tv2.getText().toString();
                        }
                    });
                    optionC.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            nextButton.setVisibility(View.VISIBLE);
                            cb1.setChecked(false);
                            cb2.setChecked(false);
                            cb3.setChecked(true);
                            cb4.setChecked(false);
                            ans=tv3.getText().toString();
                        }
                    });
                    optionD.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            nextButton.setVisibility(View.VISIBLE);
                            cb1.setChecked(false);
                            cb2.setChecked(false);
                            cb3.setChecked(false);
                            cb4.setChecked(true);
                            ans=tv4.getText().toString();
                        }
                    });

                    nextButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cancel.setVisibility(View.GONE);
                            if(ans.equals(finalAns))
                            {
                                ++correct;
                            }
                            else {
                                ++wrong;
                            }
                            i++;

                            if(i>=10)
                            {
                                quizRunning.setVisibility(View.GONE);
                                afterQuiz.setVisibility(View.VISIBLE);
                                getScore(questions);

                            }
                            else {
                                setQuestion(questions);
                            }
                        }
                    });


                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertDialog.Builder(PlayQuiz.this)
                                    .setTitle("Exit")
                                    .setMessage("Are you sure you want to cancel this test?")
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
                                            finish();
                                        }
                                    })
                                    .setIcon(R.drawable.warning)
                                    .show();
                        }
                    });



                home.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PlayQuiz.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });

//                }
                //end of for loop

            }

            @Override
            public void onQuestionsError(String errorMessage) {
                Log.e("response", "Failed to fetch questions: " + errorMessage);
            }
        });


    }

    private void getScore(ArrayList<HashMap<String, String>> questions)
    {
        if(correct>=8 && correct<10)
        {
            animationView1.setVisibility(View.VISIBLE);
            animationView.setAnimation(R.raw.better);
            animationView.playAnimation();
        }
        else if(correct >= 5 && correct < 8)
        {
            animationView1.setVisibility(View.VISIBLE);
            animationView.setAnimation(R.raw.better);
            animationView.playAnimation();
        }
        else if (correct>0 && correct <5)
        {
            animationView1.setVisibility(View.VISIBLE);
            animationView.setAnimation(R.raw.good);
            animationView.playAnimation();
        }
        else if(correct == 10)
        {
            animationView1.setVisibility(View.VISIBLE);
            animationView.setAnimation(R.raw.allcorrect);
            animationView.playAnimation();
        }
        else if (correct == 0)
        {
            animationView1.setVisibility(View.GONE);
            animationView.setAnimation(R.raw.lose_all);
            animationView.setPadding(10,60,10,40);
            animationView.playAnimation();
        }
        Log.d("response","ans: "+ans);
        Log.d("response","Respond: "+(questions.size()-skip)+"correct: "+correct+" wrong: "+wrong+" skip: "+skip);
        score.append(""+(correct*10));
        total.append(""+questions.size());
        countSkip.append(""+skip);
        countRespond.append(""+(questions.size()-skip));
        countCorrect.append(""+correct);
        countWrong.append(""+wrong);

        updateScore(catId,(correct*10));
    }
    private void setQuestion(ArrayList<HashMap<String, String>> questions)
    {
        Log.d("response","ans: "+ans);
        Log.d("response","Respond: "+(questions.size()-skip)+"correct: "+correct+" wrong: "+wrong+" skip: "+skip);
        nextButton.setVisibility(View.GONE);

        hashMap = new HashMap<>();
        hashMap = arrayList.get(i);
        String qs = hashMap.get("question");
        String op = hashMap.get("options");
        finalAns = hashMap.get("answer");


        countQuestion.setText(""+(i+1));
        totalQuestion.setText(""+questions.size());

        question.setText(qs);

        String ar[] = op.split("##");
        String op1,op2,op3,op4;
        if(ar.length==2)
        {
            optionA.setVisibility(View.VISIBLE);
            optionB.setVisibility(View.VISIBLE);
            optionC.setVisibility(View.GONE);
            optionD.setVisibility(View.GONE);
            op1 = ar[0];
            op2 = ar[1];
            cb1.setChecked(false);
            cb2.setChecked(false);
            tv1.setText(op1);
            tv2.setText(op2);
        }
        else if(ar.length==3)
        {
            optionA.setVisibility(View.VISIBLE);
            optionB.setVisibility(View.VISIBLE);
            optionC.setVisibility(View.VISIBLE);
            optionD.setVisibility(View.GONE);
            op1 = ar[0];
            op2 = ar[1];
            op3 = ar[2];
            cb1.setChecked(false);
            cb2.setChecked(false);
            cb3.setChecked(false);
            tv1.setText(op1);
            tv2.setText(op2);
            tv3.setText(op3);
        }
        else if(ar.length==4)
        {
            optionA.setVisibility(View.VISIBLE);
            optionB.setVisibility(View.VISIBLE);
            optionC.setVisibility(View.VISIBLE);
            optionD.setVisibility(View.VISIBLE);

             op1 = ar[0];
             op2 = ar[1];
             op3 = ar[2];
             op4 = ar[3];

            cb1.setChecked(false);
            cb2.setChecked(false);
            cb3.setChecked(false);
            cb4.setChecked(false);
            tv1.setText(op1);
            tv2.setText(op2);
            tv3.setText(op3);
            tv4.setText(op4);
        }


        Animation FadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        optionLayout.startAnimation(FadeIn);
        question.startAnimation(FadeIn);

    }

    public void getQuestion(String catId, QuestionCallback callback)
    {
        arrayList=new ArrayList<>();
        String url="https://appservice272.000webhostapp.com/app/getQuestion.php?cid="+catId;
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.GONE);
                quizRunning.setVisibility(View.VISIBLE);

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
                        else {
                            String qs = object.getString("question");
                            String op = object.getString("options");
                            String answer = object.getString("answer");

                            hashMap = new HashMap<>();
                            hashMap.put("question",qs);
                            hashMap.put("options",op);
                            hashMap.put("answer",answer);
                            arrayList.add(hashMap);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }



                    callback.onQuestionsReceived(arrayList);
                    Log.d("response", "arrayList in response function: " + arrayList);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                callback.onQuestionsError(error.getMessage());
            }
        });

        queue = Volley.newRequestQueue(PlayQuiz.this);
        queue.add(arrayRequest);

    }

    private void updateScore(String catId,int score)
    {
        String url="https://appservice272.000webhostapp.com/app/updateScore.php?uid="+MainActivity.Userid+"&cid="+catId+"&score="+score;
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String res = response.getString("result");
                    if(!res.equals("success"))
                    {
                        Toast.makeText(PlayQuiz.this, "Score can't be updated", Toast.LENGTH_SHORT).show();
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
        queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(objectRequest);
    }

}