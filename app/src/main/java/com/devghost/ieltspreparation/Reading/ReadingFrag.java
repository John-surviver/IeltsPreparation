package com.devghost.ieltspreparation.Reading;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devghost.ieltspreparation.Models.DatabaseHelper;
import com.devghost.ieltspreparation.Home;
import com.devghost.ieltspreparation.Models.Question;
import com.devghost.ieltspreparation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.jsibbold.zoomage.ZoomageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    LinearLayout linearLayout,readingLay;
    LottieAnimationView lottieAnimationView;
    //firebase

    int POINTS = 0;
    View view;
    MediaPlayer mp,mp2;
    ScrollView scrollView;
    public static String PASSAGE = "";

    private DatabaseHelper databaseHelper;

    //--------------------------------------------

    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private int totalPoints;

    private String loggedEmail;


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
        readingLay=view.findViewById(R.id.ReadingLay);
        scrollView=view.findViewById(R.id.scrollView2);
        mp = MediaPlayer.create(requireContext(), R.raw.rightanswer);
        mp2 = MediaPlayer.create(requireContext(), R.raw.wronganswer);
        databaseHelper = new DatabaseHelper(requireContext());
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (currentUser == null) {
            Toast.makeText(requireContext(), "Login to Save the Points", Toast.LENGTH_SHORT).show();
        }
        else {
            loggedEmail = currentUser.getEmail();
            getPoints();
        }


        //load questions
        if (!cachedQuestions.isEmpty()) {
            displayQuestion();
        } else {
            loadQuestions();
        }

        title_name_tv.setText(TITLE);
        main_Q_tv.setText(PASSAGE);

        return view;
    }




    private void loadQuestions() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, QUESTION_URL, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("questions");

                        if (jsonArray.length() == 0) {
                            // Show toast for "Coming soon"
                            new AlertDialog.Builder(requireContext())
                                    .setTitle("Coming Soon")
                                    .setMessage("Please wait few Days")
                                    .create()
                                    .show();

                        } else {
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
                                Question question = new Question(questionText, answerOptions, correctAnswer, picLink);
                                questions.add(question);


                            }
                            cachedQuestions.addAll(questions);
                            // Shuffle the questions list
                            // Collections.shuffle(cachedQuestions);
                            displayQuestion();
                        }
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
                    readingLay.setVisibility(View.GONE);
                    scrollView.setVisibility(View.GONE);
                    lottieAnimationView.setAnimation(R.raw.correct);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    if (mp != null && mp2 != null) {
                        mp.start();
                    }
                } else {
                    wrong++;

                    String correctAns = question.getCorrectAnswer();

                    Toast.makeText(requireContext(), "Correct Answer Is :" + correctAns, Toast.LENGTH_LONG).show();

                    readingLay.setVisibility(View.GONE);
                    scrollView.setVisibility(View.GONE);
                    lottieAnimationView.setAnimation(R.raw.wrong);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    if (mp != null && mp2 != null) {
                        mp2.start();
                    }

                }
                show_score.setText(MessageFormat.format("Points: {0}", POINTS));

                new Handler().postDelayed(() -> {
                    readingLay.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.VISIBLE);
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

    private void showScore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Test Result");
       // builder.setMessage("Score: " + score + "/" + cachedQuestions.size());

        // Inflate the layout containing the LottieAnimationView
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.lottie_view, null);
        LottieAnimationView lottieView = view.findViewById(R.id.lottie_view);
        TextView textView = view.findViewById(R.id.show_score_tv);

        builder.setView(view);

        textView.setText(MessageFormat.format("Score: {0}/{1}", score, cachedQuestions.size()));


        // Save the scores to the database
        int[] Scores = databaseHelper.getScores();

        Scores[0]+=Scores[0]+2;

        saveScores(Scores);

        if (currentUser != null) {
            POINTS += 50;
            updatePoints();
        }






        AlertDialog dialog = builder.create();
        dialog.show();


        // Start the Lottie animation
        lottieView.setVisibility(View.VISIBLE);

        lottieView.setOnClickListener(v -> {
            dialog.dismiss();
            FragmentManager fragment = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragment.beginTransaction();
            fragmentTransaction.replace(R.id.mainLay,new ReadingMenu());
            fragmentTransaction.commit();
        });
    }


    private void saveScores(int[] scores) {
        databaseHelper.saveScores(scores);
    }

    private void getPoints() {
        // Check if points are already cached
        if (totalPoints != 0) {
            POINTS += totalPoints;
            return;
        }

        db.collection("Users").document(loggedEmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String pointsString = documentSnapshot.getString("points");
                    totalPoints = Integer.parseInt(pointsString);
                    POINTS += totalPoints;
                }
            } else {
                Toast.makeText(getActivity(), "Failed to retrieve points", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to retrieve points", Toast.LENGTH_SHORT).show());
    }

    private void updatePoints() {
        String updatedPoints = String.valueOf(POINTS);
        Map<String, Object> data = new HashMap<>();
        data.put("points", updatedPoints);

        DocumentReference userRef = db.collection("Users").document(loggedEmail);

        WriteBatch batch = db.batch();
        batch.update(userRef, data);
        batch.commit()
                .addOnSuccessListener(unused -> {
                    totalPoints = POINTS; // Update the cached points
                    Toast.makeText(getActivity(), "Points Added " , Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to update points", Toast.LENGTH_SHORT).show());
    }

}