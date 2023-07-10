package com.devghost.ieltspreparation.Activities;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.devghost.ieltspreparation.Home;
import com.devghost.ieltspreparation.InfoFrag;
import com.devghost.ieltspreparation.LoadScoreFrag;
import com.devghost.ieltspreparation.R;
import com.devghost.ieltspreparation.RankingFrag;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_UPDATE = 101;

    BottomNavigationView bottomNavigationView;
    private AdView mAdView;

    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;

    int BANNER_AD_CLICK_COUNT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        loadHomeFrag();
        loadScoreFrag();
        assignIds();
       // loadBannerAd();
        checkForAppUpdate();

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

    private void assignIds() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        mAdView = findViewById(R.id.adView);
    }

    private void loadHomeFrag() {
        FragmentManager fragment = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragment.beginTransaction();
        fragmentTransaction.replace(R.id.mainLay, new Home());
        fragmentTransaction.commit();
    }

    private void loadScoreFrag() {
        FragmentManager fragment = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragment.beginTransaction();
        fragmentTransaction.replace(R.id.scoreLay, new LoadScoreFrag());
        fragmentTransaction.commit();
    }

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
}
