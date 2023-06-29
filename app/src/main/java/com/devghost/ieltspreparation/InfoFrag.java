package com.devghost.ieltspreparation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.devghost.ieltspreparation.Models.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoFrag extends Fragment implements View.OnClickListener {

    View view;
    CircularProgressBar progressBar1,progressBar2,progressBar3,progressBar4,progressBar5;
    LinearLayout signUpOrLoginBtn;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    TextView textView,p1,p2,p3,p4,p5;
    ImageView settings;

    //---------------------------------

    TextView userName_tv;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    String loggedEmail;

    CircleImageView profilepic;

    Bitmap bitmap;
    private ActivityResultLauncher<Intent> galleryLauncher;

    Context context;
    private DatabaseHelper databaseHelper ;

    Button login;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }
    //-------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_info, container, false);

        assignIds();
        loadProgress();

        if(mAuth.getCurrentUser()==null){
            signUpOrLoginBtn.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
        else{
            loggedEmail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
            loadName();
        }

        signUpOrLoginBtn.setOnClickListener(this);
        settings.setOnClickListener(this);
        login.setOnClickListener(this);

        // To load data
        loadImageBitmap(getContext());

        // Initialize the ActivityResultLauncher
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImage = data.getData();
                            try {
                                // Resize and compress the selected image
                                Bitmap resizedBitmap = resizeBitmap(selectedImage); // Resize to 300x300 or adjust as needed
                                byte[] compressedImage = compressBitmap(resizedBitmap); // Compress to 50KB or adjust as needed

                                // Upload the compressed image to Firebase Storage
                                StorageReference imageRef = storageRef.child("profile_images/" + loggedEmail + ".jpg");
                                UploadTask uploadTask = imageRef.putBytes(compressedImage);
                                uploadTask.addOnSuccessListener(taskSnapshot -> new AlertDialog.Builder(requireContext())
                                        .setTitle("")
                                        .setMessage("Uploaded Successfully")
                                        .create()
                                        .show()).addOnFailureListener(e -> Toast.makeText(requireContext(), "Failed to upload profile picture", Toast.LENGTH_SHORT).show());

                                // Display the selected image in the ImageView
                                bitmap = resizedBitmap;
                                profilepic.setImageBitmap(bitmap);

                                // Save the selected image to internal storage
                                saveImage(requireContext(), bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        profilepic.setOnClickListener(v -> {

            if(mAuth.getCurrentUser()==null){
                new AlertDialog.Builder(requireContext())
                        .setTitle("Login Please")
                        .setMessage("To save your picture\nYou must Login")
                        .create()
                        .show();
            }
           else{
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                galleryLauncher.launch(galleryIntent);
            }

        });

        return view;
    }

    private void assignIds() {
        progressBar1=view.findViewById(R.id.circularProgressBar);
        progressBar2=view.findViewById(R.id.circularProgressBar1);
        progressBar3=view.findViewById(R.id.circularProgressBar2);
        progressBar4=view.findViewById(R.id.circularProgressBar4);
        progressBar5=view.findViewById(R.id.circularProgressBar5);
        signUpOrLoginBtn=view.findViewById(R.id.linearLayout4);
        textView=view.findViewById(R.id.textView6);
        p1=view.findViewById(R.id.p1);
        p2=view.findViewById(R.id.p2);
        p3=view.findViewById(R.id.p3);
        p4=view.findViewById(R.id.p4);
        p5=view.findViewById(R.id.p5);
        settings=view.findViewById(R.id.settings_id);
        login=view.findViewById(R.id.login_signUp_btn);

        userName_tv = view.findViewById(R.id.userName_tv);
        profilepic = view.findViewById(R.id.profile_image);
    }

    private void loadProgress() {
        loadScores();
    }

    private void loadScores() {
        int[] scores = databaseHelper.getScores();

        int score_1 = (scores[0]); //reading
        int score_2 = (scores[1]); //listening
        int score_3 = (scores[2]); //speaking
        int score_4 = (scores[3]); //writing
        int score_5 = (scores[4]); //grammar


        if(score_1>100){
            score_1=100;
            p1.setText(MessageFormat.format("{0}%", score_1));
        }
        else {
            p1.setText(MessageFormat.format("{0}%", score_1));
        }
        if(score_2>100){
            score_2=100;
            p2.setText(MessageFormat.format("{0}%", score_2)); //listening
        }
        else {
            p2.setText(MessageFormat.format("{0}%", score_2)); //listening
        }
       if(score_3>100){
           score_3=100;
           p3.setText(MessageFormat.format("{0}%", score_3));
       }
       if(score_4>100){
           score_4=100;
           p4.setText(MessageFormat.format("{0}%", score_4));  //writing
       }
       else {
           p4.setText(MessageFormat.format("{0}%", score_4));  //writing
       }
       if(score_5>100){
           score_5=100;
           p5.setText(MessageFormat.format("{0}%", score_5)); //grammar
       }
       else {
           p5.setText(MessageFormat.format("{0}%", score_5)); //grammar
       }



        p5.setText(MessageFormat.format("{0}%", score_5)); //grammar

        float score1 = Float.parseFloat(String.valueOf(score_1));
        float score2 = Float.parseFloat(String.valueOf(score_2));
        float score3 = Float.parseFloat(String.valueOf(score_3));
        float score4 = Float.parseFloat(String.valueOf(score_4));
        float score5 = Float.parseFloat(String.valueOf(score_5));

        progressBar1.setProgress(score1);
        progressBar1.setProgressMax(100);

        progressBar2.setProgress(score2);
        progressBar2.setProgressMax(100);

        progressBar3.setProgress(score3);
        progressBar3.setProgressMax(100);

        progressBar4.setProgress(score4);
        progressBar4.setProgressMax(100);

        progressBar5.setProgress(score5);
        progressBar5.setProgressMax(100);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.login_signUp_btn){
            startActivity(new Intent(requireContext(),LoginOrSignUp.class));
        }
        else if (v.getId()==R.id.settings_id) {
            FragmentManager fragment = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragment.beginTransaction();
            fragmentTransaction.replace(R.id.mainLay,new ContactDev());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        }

    private void loadName() {
        db.collection("Users").document(loggedEmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    String userName = document.getString("UserName");
                    userName_tv.setText(userName);
                }
            } else {
                Toast.makeText(requireContext(), "Failed to get user name", Toast.LENGTH_SHORT).show();
            }
        });

    }




    private void saveImage(Context context, Bitmap bitmap) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = context.openFileOutput("profilepic" + "." + "jpg", Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadImageBitmap(Context context) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = context.openFileInput("profilepic" + "." + "jpg");
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            profilepic.setImageBitmap(bitmap);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap resizeBitmap(Uri imageUri) throws IOException {
        // Load the image from URI
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Calculate the new dimensions
        float ratio = Math.min((float) 300 / width, (float) 300 / height);
        int newWidth = (int) (width * ratio);
        int newHeight = (int) (height * ratio);

        // Resize the bitmap
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    private byte[] compressBitmap(Bitmap bitmap) {
        // Calculate the compression quality based on the desired maximum size
        int quality = 100;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        byte[] compressedImage = outputStream.toByteArray();

        while (compressedImage.length / 1024 > 50) {
            // Reduce the quality by 10% until the size is within the desired limit
            quality -= 10;
            outputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            compressedImage = outputStream.toByteArray();
        }

        return compressedImage;
    }

}