package com.devghost.ieltspreparation.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.devghost.ieltspreparation.ContactDev;
import com.devghost.ieltspreparation.Home;
import com.devghost.ieltspreparation.InfoFrag;
import com.devghost.ieltspreparation.LoginOrSignUp;
import com.devghost.ieltspreparation.Models.UserModel;
import com.devghost.ieltspreparation.R;
import com.devghost.ieltspreparation.RankingFrag;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_UPDATE = 101;

    BottomNavigationView bottomNavigationView;
    private AdView mAdView;

    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;

    int BANNER_AD_CLICK_COUNT = 0;

    //----------------------------
    DrawerLayout drawerLayout;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;

    //---------------------------
    //header Control
    View myView;


    //---------------------------
    CircleImageView profilepic;
    Bitmap bitmap;
    private ActivityResultLauncher<Intent> galleryLauncher;

    //-------------------------
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    TextView textView;

    //---------------------------------

    TextView userName_tv;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    String loggedEmail;

    //-----------------------------------

    //for Loading Score
    private static final String PREF_NAME = "UserPrefs";
    private static final String POINTS_KEY = "points";
    private SharedPreferences sharedPreferences;
    private TextView points_tv;
    private ListenerRegistration listenerRegistration;

    //-----------------------------------
    Menu menu;
    MenuItem menuItem,menuItem2;
    //-----------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        loadHomeFrag();
        assignIds();
        loadBannerAd();
        checkForAppUpdate();
        setProfilePic();
        loadBottomNav();
        loadNavigationDrawyer();
        loadImageBitmap(this);


        if(mAuth.getCurrentUser()==null){
            menuItem.setVisible(false);
            menuItem2.setVisible(true);

        }
        else{
            loggedEmail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
            loadName();
            menuItem.setVisible(true);
            menuItem2.setVisible(false);
        }



    }

    //------------------------------------------------
    //Navigation Drawyer
    private void loadNavigationDrawyer() {
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //setting listener
        nav.setNavigationItemSelectedListener(item -> {

            if(item.getItemId()==R.id.contact_dev_nav){
                ContactDeV();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            else if (item.getItemId()==R.id.pp_nav) {
                PrivacyPolicy();
            }
            else if (item.getItemId()==R.id.logout_nav) {
                LogOut();
            }
            else if (item.getItemId()==R.id.login_nav) {
               Login();
            }
            else if (item.getItemId()==R.id.rate_nav) {
                RateUs();
            }
            else if (item.getItemId()==R.id.recent_nav) {
                RecentUpdates();
            }

            return true;
        });
    }

    private void RecentUpdates() {
        FragmentManager fragment = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragment.beginTransaction();
        fragmentTransaction.replace(R.id.mainLay, new ContactDev());
        fragmentTransaction.commit();
    }
    //------------------------------------------------

    private void RateUs() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + this.getPackageName())));
        }
        catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/apps/details?id=" + this.getPackageName())));
        }
    }

    //------------------------------------------------
    //Login
    private void Login() {
        startActivity(new Intent(this,LoginOrSignUp.class));
    }

    //------------------------------------------------
    //Logout
    private void LogOut() {
        android.app.AlertDialog.Builder alertdialogbuilder;
        alertdialogbuilder = new android.app.AlertDialog.Builder(this);
        // alertdialogbuilder.setIcon(R.drawable.a);
        alertdialogbuilder.setTitle("Sign Out ?");
        alertdialogbuilder.setMessage("Do you want to log out ?");
        alertdialogbuilder.setCancelable(false);

        alertdialogbuilder.setPositiveButton("yes", (dialogInterface, i) -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginOrSignUp.class));
        });

        alertdialogbuilder.setNegativeButton("no", (dialogInterface, i) -> dialogInterface.cancel());

        android.app.AlertDialog alertDialog = alertdialogbuilder.create();
        alertDialog.show();
    }

    //------------------------------------------------
    //Privacy Policy

    private void PrivacyPolicy() {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(getString(R.string.pp_link)));
    }

    //------------------------------------------------

    //------------------------------------------------
    //Bottom Navigation
    private void loadBottomNav() {
        bottomNavigationView.setOnItemSelectedListener(item -> {

            for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
                MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
                menuItem.setTitleCondensed("");
                menuItem.setChecked(false);
            }

            item.setChecked(true);
            item.setTitleCondensed("Selected");

            if (item.getItemId() == R.id.home_nav) {
                FragmentManager fragment = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragment.beginTransaction();
                fragmentTransaction.replace(R.id.mainLay, new Home());
                fragmentTransaction.commit();
            } else if (item.getItemId() == R.id.info_nav) {
                FragmentManager fragment = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragment.beginTransaction();
                fragmentTransaction.replace(R.id.mainLay, new InfoFrag());
                fragmentTransaction.commit();
            } else if (item.getItemId() == R.id.rank_menu) {
                FragmentManager fragment = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragment.beginTransaction();
                fragmentTransaction.replace(R.id.mainLay, new RankingFrag());
                fragmentTransaction.commit();
            }

            return true;
        });
    }

    //------------------------------------------------


    //------------------------------------------------
    //set profile pic
    @SuppressLint("IntentReset")
    private void setProfilePic() {
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
                                uploadTask.addOnSuccessListener(taskSnapshot -> new android.app.AlertDialog.Builder(this)
                                        .setTitle("")
                                        .setMessage("Uploaded Successfully")
                                        .create()
                                        .show()).addOnFailureListener(e -> Toast.makeText(this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show());

                                // Display the selected image in the ImageView
                                bitmap = resizedBitmap;
                                profilepic.setImageBitmap(bitmap);

                                // Save the selected image to internal storage
                                saveImage(this, bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        profilepic.setOnClickListener(v -> {

            if(mAuth.getCurrentUser()==null){
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Login Please")
                        .setMessage("To save your picture\nYou must Login")
                        .create()
                        .show();
            }
            else{
                @SuppressLint("IntentReset") Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                galleryLauncher.launch(galleryIntent);
            }

        });
    }
    //-----------------------------------------------

    //------------------------------------------------
    //Contact Developer
    private void ContactDeV() {
        Toast.makeText(this, "Email Us , Select Your Email/Gmail App", Toast.LENGTH_LONG).show();
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"jyspark668@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Email From Ielts App");
        i.putExtra(Intent.EXTRA_TEXT   , "Write here");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    //------------------------------------------------
    //Assign IDs
    private void assignIds() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        mAdView = findViewById(R.id.adView);
        drawerLayout = findViewById(R.id.drawyer);
        nav = findViewById(R.id.nav);
        toolbar = findViewById(R.id.toolbar);
        myView=nav.getHeaderView(0);
        profilepic=myView.findViewById(R.id.profile_image);
        userName_tv = myView.findViewById(R.id.userName_tv);
        db = FirebaseFirestore.getInstance();
        sharedPreferences = this.getSharedPreferences(PREF_NAME, 0);
        points_tv = myView.findViewById(R.id.points_tv);
        menu=nav.getMenu();
        menuItem=menu.findItem(R.id.logout_nav);
        menuItem2= menu.findItem(R.id.login_nav);


        //set the Custom toolBar on ActionBar
        setSupportActionBar(toolbar);
    }
    //------------------------------------------------


    //------------------------------------------------
    //Load Home Fragment

    private void loadHomeFrag() {
        FragmentManager fragment = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragment.beginTransaction();
        fragmentTransaction.replace(R.id.mainLay, new Home());
        fragmentTransaction.commit();
    }

    //Home Fragment Ends here
    //------------------------------------------------

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            AlertDialog.Builder alertdialogbuilder;
            alertdialogbuilder = new AlertDialog.Builder(MainActivity.this);
            alertdialogbuilder.setTitle("Quit?");
            alertdialogbuilder.setMessage("Do you want to quit?");
            alertdialogbuilder.setCancelable(false);

            alertdialogbuilder.setPositiveButton("Yes", (dialogInterface, i) -> finish());

            alertdialogbuilder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());

            AlertDialog alertDialog = alertdialogbuilder.create();
            alertDialog.show();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    //------------------------------------------------
    //Banner ads starts here

    private void loadBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (BANNER_AD_CLICK_COUNT >= 1) {
                    if (mAdView != null) mAdView.setVisibility(View.GONE);
                } else {
                    if (mAdView != null) mAdView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that covers the screen.
            }

            @Override
            public void onAdClicked() {
                BANNER_AD_CLICK_COUNT++;

                if (BANNER_AD_CLICK_COUNT >= 1) {
                    if (mAdView != null) mAdView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return to the app after tapping on an ad.
            }
        });
    }

    //Banner ads end here
    //------------------------------------------------


    //------------------------------------------------
    //Auto Update Check Starts Here

    private void checkForAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);

        installStateUpdatedListener = installState -> {
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            }
        };

        appUpdateManager.registerListener(installStateUpdatedListener);
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.FLEXIBLE,
                            MainActivity.this,
                            REQUEST_CODE_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void popupSnackbarForCompleteUpdate() {
        Snackbar.make(findViewById(android.R.id.content),
                        "An update has been downloaded. Please restart the app to apply the update.",
                        Snackbar.LENGTH_INDEFINITE)
                .setAction("RESTART", view -> appUpdateManager.completeUpdate())
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode != RESULT_OK) {
                checkForAppUpdate();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (appUpdateManager != null && installStateUpdatedListener != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

    //Automatic update check ends here
    //------------------------------------------------


    //------------------------------------------------
    //Loading Name starts here
    private void loadName() {
        db.collection("Users").document(loggedEmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    String userName = document.getString("UserName");
                    userName_tv.setText(userName);
                }
            } else {
                Toast.makeText(this, "Failed to get user name", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Loading Name Ends here
    //------------------------------------------------


    //------------------------------------------------
    //Saving Photos Start here

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
            this.profilepic.setImageBitmap(bitmap);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap resizeBitmap(Uri imageUri) throws IOException {
        // Load the image from URI
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

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

    //Saving Photo Ends here
    //------------------------------------------------


    //-------------------------------------------------
    // For Loading Score
    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            fetchPoints();
            registerPointsListener();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterPointsListener();
    }

    private void fetchPoints() {
        int cachedPoints = sharedPreferences.getInt(POINTS_KEY, 0);
        points_tv.setText(MessageFormat.format("{0}", cachedPoints));

        db.collection("Users").document(loggedEmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("UserName");
                    String points = documentSnapshot.getString("points");
                    if (points != null) {
                        int pointsValue = Integer.parseInt(points);

                        UserModel userModel = new UserModel(name, pointsValue);
                        updatePoints(userModel);
                        cachePoints(pointsValue);


                    }
                }
            } else {
                showToast("Failed");
            }
        }).addOnFailureListener(e -> showToast("Failed"));
    }

    private void updatePoints(UserModel userModel) {
        int points = userModel.getPoints();
        points_tv.setText(MessageFormat.format("{0}", points));
    }

    private void cachePoints(int points) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(POINTS_KEY, points);
        editor.apply();
    }

    private void registerPointsListener() {
        listenerRegistration = db.collection("Users").document(loggedEmail).addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                showToast("Failed to fetch points");
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                String points = snapshot.getString("points");
                if (points != null) {
                    int pointsValue = Integer.parseInt(points);
                    UserModel userModel = new UserModel(snapshot.getString("UserName"), pointsValue);
                    updatePoints(userModel);
                    cachePoints(pointsValue);
                }
            }
        });
    }

    private void unregisterPointsListener() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //Loading Score Ends Here
    //---------------------------------------------

}
