package com.devghost.ieltspreparation.Reading;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devghost.ieltspreparation.Question;
import com.devghost.ieltspreparation.R;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


public class ReadingFrag extends Fragment {

    public static String TITLE = "";

    public static  String QUESTION_URL = "";

    public static int coin = 0;
    private final List<Question> questions = new ArrayList<>();
    private final List<Question> cachedQuestions = new ArrayList<>();
    private int currentQuestion = 0;

    int score = 0;
    int wrong = 0;

    TextView show_score,main_Q_tv,title_name_tv;
    ZoomageView q_pic;
    LinearLayout linearLayout;
    LottieAnimationView lottieAnimationView;
    //firebase

    String email;
    int POINTS = 0;
    int totalPoints;

    String loggedEmail;

    View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reading, container, false);


        q_pic=view.findViewById(R.id.q_pic2);
        linearLayout=view.findViewById(R.id.quizLay2);
        lottieAnimationView=view.findViewById(R.id.wrong_anim2);
        show_score=view.findViewById(R.id.score_tv2);
        main_Q_tv=view.findViewById(R.id.main_Q_tv);
        title_name_tv=view.findViewById(R.id.title_name_tv);



        //load questions
        if (!cachedQuestions.isEmpty()) {
            displayQuestion();
        } else {
            loadQuestions();
        }

        title_name_tv.setText(TITLE);

        return view;
    }

    private void loadQuestions() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, QUESTION_URL, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("questions");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String questionText = jsonObject.getString("question");
                            JSONArray answerOptionsArray = jsonObject.getJSONArray("answerOptions");
                            String[] answerOptions = new String[answerOptionsArray.length()];
                            for (int j = 0; j < answerOptionsArray.length(); j++) {
                                answerOptions[j] = answerOptionsArray.getString(j);
                            }
                            String correctAnswer = jsonObject.getString("correctAnswer");
                            String picLink = jsonObject.getString("link");
                            String mainQ = jsonObject.getString("main_q");
                            Question question = new Question(questionText, answerOptions, correctAnswer, picLink);
                            questions.add(question);

                            main_Q_tv.setText(mainQ);
                        }
                        cachedQuestions.addAll(questions);
                        // Shuffle the questions list
                        // Collections.shuffle(cachedQuestions);
                        displayQuestion();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error loading questions from the server
                    Toast.makeText(requireContext(), "Failed to load questions. Check your network connection.", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(requireContext()).add(request);
    }

    private void displayQuestion() {
        //assign ids
        TextView questionTextView = view.findViewById(R.id.question_text_view2);
        RadioButton answerRadioButton1 = view.findViewById(R.id.answer_radio_button_1_1);
        RadioButton answerRadioButton2 = view.findViewById(R.id.answer_radio_button_2_2);
        RadioButton answerRadioButton3 = view.findViewById(R.id.answer_radio_button_3_3);
        RadioButton answerRadioButton4 = view.findViewById(R.id.answer_radio_button_4_4);

        Question question = cachedQuestions.get(currentQuestion);

        questionTextView.setText(question.getQuestion());

        answerRadioButton1.setText(question.getAnswerOptions()[0]);
        answerRadioButton2.setText(question.getAnswerOptions()[1]);
        answerRadioButton3.setText(question.getAnswerOptions()[2]);
        answerRadioButton4.setText(question.getAnswerOptions()[3]);

        String link = question.getPicLink();
        if (link.equals("0")) {
            q_pic.setVisibility(View.GONE);
        } else {
            q_pic.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(link)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.download)
                    .into(q_pic);
        }

        Button nextButton = view.findViewById(R.id.next_button2);
        nextButton.setOnClickListener(v -> {
            if (answerRadioButton1.isChecked() || answerRadioButton2.isChecked() ||
                    answerRadioButton3.isChecked() || answerRadioButton4.isChecked()) {
                String selectedAnswer = "";
                if (answerRadioButton1.isChecked()) {
                    selectedAnswer = answerRadioButton1.getText().toString();
                } else if (answerRadioButton2.isChecked()) {
                    selectedAnswer = answerRadioButton2.getText().toString();
                } else if (answerRadioButton3.isChecked()) {
                    selectedAnswer = answerRadioButton3.getText().toString();
                } else if (answerRadioButton4.isChecked()) {
                    selectedAnswer = answerRadioButton4.getText().toString();
                }
                if (selectedAnswer.equals(question.getCorrectAnswer())) {
                    score++;
                    POINTS += coin;
                    //  updatePoints();
                    linearLayout.setVisibility(View.GONE);
                    lottieAnimationView.setAnimation(R.raw.correct);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                   /* if (mp != null && mp2 != null) {
                        mp.start();
                    }*/
                } else {
                    wrong++;

                    linearLayout.setVisibility(View.GONE);
                    lottieAnimationView.setAnimation(R.raw.wrong);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                   /* if (mp != null && mp2 != null) {
                        mp2.start();
                    }*/

                   /* if(wrong==3){
                       *//* new AlertDialog.Builder(requireContext())
                                .setTitle("Game Over")
                                .setMessage("Play again")
                                .create()
                                .show();*//*

                        showScore();
                        new Handler().postDelayed(this::goBack, 2000);

                    }*/
                }
                show_score.setText(MessageFormat.format("Points: {0}", POINTS));

                new Handler().postDelayed(() -> {
                    linearLayout.setVisibility(View.VISIBLE);
                    lottieAnimationView.setVisibility(View.GONE);
                    currentQuestion++;
                    if (currentQuestion < cachedQuestions.size()) {
                        displayQuestion();
                        // clearRadioButtons();
                    } else {
                        showScore();
                    }
                }, 2000);
            } else {
                Toast.makeText(requireContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goBack() {
        requireActivity().onBackPressed();
    }

    private void showScore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Quiz Result");
        builder.setMessage("Score: " + score + "/" + cachedQuestions.size());
        builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}