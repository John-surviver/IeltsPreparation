package com.devghost.ieltspreparation.Listening;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.annotation.SuppressLint;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devghost.ieltspreparation.Question;
import com.devghost.ieltspreparation.R;
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


public class ListeningFrag extends Fragment {

   View view;
   public static String TITLE = "";

    public static  String QUESTION_URL = "";
    public static String AUDIO_URL = "";

    public static String ADV = "";
    public static int coin = 0;
    private final List<Question> questions = new ArrayList<>();
    private final List<Question> cachedQuestions = new ArrayList<>();
    private int currentQuestion = 0;

    int score = 0;
    int wrong = 0;

    TextView show_score;
    ZoomageView q_pic;
    LinearLayout linearLayout;
    LottieAnimationView lottieAnimationView;
    //firebase

    String email;
    int POINTS = 0;
    int totalPoints;

    String loggedEmail;

    // Declare a MediaPlayer object reference
    MediaPlayer mp,mp2,mediaPlayer;
    private RadioButton selectedRadioButton;

    ImageButton playbtn;
    SeekBar seekBar;
    private boolean isSeeking = false;
    TextView time_remaining,total_time,song_title;

    //----------------------------------------------------
    //list view for upcoming questions
    ListView listView;
    ListAdapter listAdapter;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    HashMap <String,String> hashMap;

    //----------------------------------------------------



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_listening, container, false);

        //assign IDs
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
        String url = "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/set_1_adv.json";

        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
          //  progressBar.setVisibility(View.GONE);
            try {

                for(int x= 0 ; x<response.length(); x++){
                    JSONObject jsonObject = response.getJSONObject(x);
                    String question = jsonObject.getString("q");
                    String option_a = jsonObject.getString("a");
                    String option_b = jsonObject.getString("b");
                    String option_c = jsonObject.getString("c");
                    String option_d = jsonObject.getString("d");



                    hashMap = new HashMap<>();
                    hashMap.put("q",question);
                    hashMap.put("a",option_a);
                    hashMap.put("b",option_b);
                    hashMap.put("c",option_c);
                    hashMap.put("d",option_d);
                    arrayList.add(hashMap);


                }
                listAdapter = new ListAdapter();
                listView.setAdapter(listAdapter);

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
        String currentPositionStr = String.format("%02d:%02d", minutes, seconds);

        // Update the TextView with the current position
        // Convert milliseconds to minutes and seconds for total duration
        int totalMinutes = totalDuration / (1000 * 60);
        int totalSeconds = (totalDuration / 1000) % 60;
        String totalDurationStr = String.format("%02d:%02d", totalMinutes, totalSeconds);

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
                    .load(link)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.download)
                    .into(q_pic);
        }

        Button nextButton = view.findViewById(R.id.next_button);
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
                    if (mp != null && mp2 != null) {
                        mp.start();
                    }
                } else {
                    wrong++;

                    linearLayout.setVisibility(View.GONE);
                    lottieAnimationView.setAnimation(R.raw.wrong);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    if (mp != null && mp2 != null) {
                        mp2.start();
                    }

                    if(wrong==3){
                       /* new AlertDialog.Builder(requireContext())
                                .setTitle("Game Over")
                                .setMessage("Play again")
                                .create()
                                .show();*/

                        showScore();
                        new Handler().postDelayed(this::goBack, 2000);

                    }
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


    //create base adapter

    private class ListAdapter extends BaseAdapter {


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
        public View getView(int position, View view, ViewGroup viewGroup) {
            LayoutInflater layoutInflater = (LayoutInflater) requireActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View myView = layoutInflater.inflate(R.layout.up_coming_q_design,viewGroup,false);


            TextView Question = myView.findViewById(R.id.q_name);
            TextView Option_A = myView.findViewById(R.id.q_op1);
            TextView Option_B = myView.findViewById(R.id.q_op2);
            TextView Option_C = myView.findViewById(R.id.q_op3);
            TextView Option_D = myView.findViewById(R.id.q_op4);
           // LinearLayout linearLayout = myView.findViewById(R.id.upComingLay);



            HashMap<String,String> hashMap= arrayList.get(position) ;
            String Q = hashMap.get("q");
            String A = hashMap.get("a");
            String B = hashMap.get("b");
            String C = hashMap.get("c");
            String D = hashMap.get("d");

            Question.setText(Q);
            Option_A.setText(A);
            Option_B.setText(B);
            Option_C.setText(C);
            Option_D.setText(D);



            return myView;
        }
    }
}