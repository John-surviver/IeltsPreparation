package com.devghost.ieltspreparation.Listening;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devghost.ieltspreparation.Models.DatabaseHelper;
import com.devghost.ieltspreparation.Models.Question;
import com.devghost.ieltspreparation.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ListeningFrag extends Fragment {

   View view;
   public static String TITLE = "";

    public static  String QUESTION_URL = "";
    public static String AUDIO_URL = "";
    public static String PIC_LINK = "";

    public static String ADV = "";
    public static int coin = 0;
    private final List<Question> questions = new ArrayList<>();
    private final List<Question> cachedQuestions = new ArrayList<>();
    private int currentQuestion = 0;

    int score = 0;
    int wrong = 0;

    TextView show_score;
    ZoomageView q_pic;
    ScrollView linearLayout;
    LottieAnimationView lottieAnimationView;
    //firebase

    int POINTS = 0;
    int ADS = 0;

    // Declare a MediaPlayer object reference
    MediaPlayer mp,mp2,mediaPlayer;


    ImageButton playbtn;
    SeekBar seekBar;
    private boolean isSeeking = false;
    TextView time_remaining,total_time,song_title; //checkTv

    //----------------------------------------------------
    //list view for upcoming questions
    ListView listView;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    HashMap <String,String> hashMap;

    int NextQuestion=1;
    //----------------------------------------------------

    private DatabaseHelper databaseHelper;

    //---------------------------------------------------

    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private int totalPoints;
    private int totalAds;

    private String loggedEmail;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_listening, container, false);

        //assign IDs
        assignIds();

        loadFullscreenAd();


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

        //audio player check
        mediaPlayer = new MediaPlayer();
        playbtn.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                playbtn.setImageResource(R.drawable.ic_play);
                pauseAudio();
            } else {
                playbtn.setImageResource(R.drawable.pause);
                playAudio();
            }

        });

        // audio player seekbar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeeking = false;
            }
        });

        //what happens when audio ends
        mediaPlayer.setOnCompletionListener(mp -> {
            releaseMediaPlayer();
            seekBar.setProgress(0);
            playbtn.setImageResource(R.drawable.ic_play);
        });


        song_title.setText(TITLE);

        // get the json from server
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = ADV;

        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
          //  progressBar.setVisibility(View.GONE);
            try {

                for(int x= 0 ; x<response.length(); x++){
                    JSONObject jsonObject = response.getJSONObject(x);
                    String question = jsonObject.getString("NextQuestion");

                    hashMap = new HashMap<>();
                    hashMap.put("NextQuestion",question);
                    arrayList.add(hashMap);




                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
          //  progressBar.setVisibility(View.GONE);
        });

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);


        return view;
    }

    private void assignIds() {
        playbtn=view.findViewById(R.id.play_pause_button);
        seekBar=view.findViewById(R.id.seekbar);
        q_pic=view.findViewById(R.id.q_pic);
        linearLayout=view.findViewById(R.id.quizLay);
        lottieAnimationView=view.findViewById(R.id.wrong_anim);
        show_score=view.findViewById(R.id.score_tv);
        time_remaining=view.findViewById(R.id.time_remaining);
        total_time=view.findViewById(R.id.total_time);
        song_title=view.findViewById(R.id.song_title);
        listView=view.findViewById(R.id.upComingList);
        databaseHelper = new DatabaseHelper(requireContext());
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }


    //----------------------------------------------
    //Audio Player
    private int lastPlayedPosition = 0; // Variable to store the last played position

    private void pauseAudio() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            lastPlayedPosition = mediaPlayer.getCurrentPosition(); // Store the current position
        }
    }

    private void playAudio() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build());

            mediaPlayer.setDataSource(AUDIO_URL);
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(mp -> {
                mediaPlayer.seekTo(lastPlayedPosition); // Set the last played position
                mediaPlayer.start();
                updateSeekBar();
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error loading audio", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateSeekBar() {
        if (mediaPlayer != null) {
            seekBar.setMax(mediaPlayer.getDuration());

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (!isSeeking && mediaPlayer != null && mediaPlayer.isPlaying()) {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                    seekBar.postDelayed(this, 1000);

                    // Start the timer and update the TextView
                    updateTimer();
                }
            };

            seekBar.postDelayed(runnable, 0);
        }
    }

    private void updateTimer() {
        int currentPosition = mediaPlayer != null ? mediaPlayer.getCurrentPosition() : 0;
        int totalDuration = mediaPlayer != null ? mediaPlayer.getDuration() : 0;

        // Convert milliseconds to minutes and seconds
        int minutes = currentPosition / (1000 * 60);
        int seconds = (currentPosition / 1000) % 60;
        String currentPositionStr = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);


        // Update the TextView with the current position
// Convert milliseconds to minutes and seconds for total duration
        int totalMinutes = totalDuration / (1000 * 60);
        int totalSeconds = (totalDuration / 1000) % 60;
        String totalDurationStr = String.format(Locale.getDefault(), "%02d:%02d", totalMinutes, totalSeconds);

        // Update the TextViews with the current position and total duration
        time_remaining.setText(currentPositionStr);
        total_time.setText(totalDurationStr);
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    //-----------------------------------------------




    //load Questions
    private void loadQuestions() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, QUESTION_URL, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("questions");

                        if (jsonArray.length() == 0) {
                            // Show toast for "Coming soon"
                            new AlertDialog.Builder(requireContext())
                                    .setTitle("Coming Soon")
                                    .setMessage("Please wait few days")
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
        TextView questionTextView = view.findViewById(R.id.question_text_view);
        RadioButton answerRadioButton1 = view.findViewById(R.id.answer_radio_button_1);
        RadioButton answerRadioButton2 = view.findViewById(R.id.answer_radio_button_2);
        RadioButton answerRadioButton3 = view.findViewById(R.id.answer_radio_button_3);
        RadioButton answerRadioButton4 = view.findViewById(R.id.answer_radio_button_4);

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
                    .load(PIC_LINK)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.download)
                    .into(q_pic);
        }

        Button nextButton = view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(v -> {

            NextQuestion++;

            if(NextQuestion >= arrayList.size())
            {
                NextQuestion=1;
            }
            HashMap<String, String> firstElement;
            if (!arrayList.isEmpty()) {

                firstElement = arrayList.get(NextQuestion);

                // You can now access the values in the HashMap
                // Use the question variable as needed
                //checkTv.setText(MessageFormat.format("{0}", firstElement));
            }




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
                    if (mp != null && mp2 != null) {
                        mp.start();
                    }
                } else {
                    wrong++;

                    String wrongAns= question.getCorrectAnswer();
                    Toast.makeText(requireContext(), "Correct Answer is "+wrongAns, Toast.LENGTH_LONG).show();

                    linearLayout.setVisibility(View.GONE);
                    lottieAnimationView.setAnimation(R.raw.wrong);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    if (mp != null && mp2 != null) {
                        mp2.start();
                    }

                    //write code for wrong answer if needed

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
                }, 1000);
            } else {
                Toast.makeText(requireContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void showScore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Test Result");
        //builder.setMessage("Score: " + score + "/" + cachedQuestions.size());

        // Inflate the layout containing the LottieAnimationView
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.lottie_view, null);
        LottieAnimationView lottieView = view.findViewById(R.id.lottie_view);
        TextView textView = view.findViewById(R.id.show_score_tv);


        builder.setView(view);

        textView.setText(MessageFormat.format("score: {0}/{1}", score, cachedQuestions.size()));

        // Save the scores to the database

        // Save the scores to the database
        int[] Scores = databaseHelper.getScores();

        Scores[1]+=Scores[1]+2;

        saveScores(Scores);

        if (firebaseAuth.getCurrentUser() == null) {
            Toast.makeText(requireContext(), "Login for Loading Points", Toast.LENGTH_SHORT).show();
        }
        else{
            POINTS += 50;
            updatePoints();
        }


        //-----------------------------------

        AlertDialog dialog = builder.create();
        dialog.show();

        // Start the Lottie animation
        lottieView.setVisibility(View.VISIBLE);

        lottieView.setOnClickListener(v -> {

            dialog.dismiss();

            if (mInterstitialAd != null) {
                mInterstitialAd.show(requireActivity());
                ADS+=1;
                updatePoints();

            } else {


                FragmentManager fragment = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragment.beginTransaction();
                fragmentTransaction.add(R.id.mainLay,new ListeningMenu());
                fragmentTransaction.commit();
            }

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources
        if (mp != null) {
            mp.release();
        }
        if (mp2 != null) {
            mp2.release();
        }
        releaseMediaPlayer();
    }

    private void saveScores(int[] scores) {
        databaseHelper.saveScores(scores);
    }

    //create base adapter

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
                    String  adString = documentSnapshot.getString("ad");
                    totalPoints = Integer.parseInt(pointsString);
                    totalAds = Integer.parseInt(adString);
                    POINTS += totalPoints;
                    ADS+=totalAds;
                }
            } else {
                Toast.makeText(getActivity(), "Failed to retrieve points", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to retrieve points", Toast.LENGTH_SHORT).show());
    }

    private void updatePoints() {
        String updatedPoints = String.valueOf(POINTS);
        String updatedAds = String.valueOf(ADS);
        Map<String, Object> data = new HashMap<>();
        data.put("points", updatedPoints);
        data.put("ad", updatedAds);

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



    // loadFullscreenAd method starts here.....
    InterstitialAd mInterstitialAd;

    private void loadFullscreenAd(){

        //Requesting for a fullscreen Ad
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(requireContext(),getString(R.string.full_screen_ad), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;

                        //Fullscreen callback || Requesting again when an ad is shown already
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                //User dismissed the previous ad. So we are requesting a new ad here
                                loadFullscreenAd();
                                FragmentManager fragment = requireActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction=fragment.beginTransaction();
                                fragmentTransaction.add(R.id.mainLay,new ListeningMenu());
                                fragmentTransaction.commit();


                            }
                        }); // FullScreen Callback Ends here
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                }); // FullScreen Callback Ends here
    }
    // loadFullscreenAd method ENDS  here..... >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



    // then call it


}