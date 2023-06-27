package com.devghost.ieltspreparation.Activities;

import android.os.Bundle;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView btmnav;

    //-------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        loadHomeFrag();
        loadScoreFrag();
        assignIds();


        btmnav.setOnItemSelectedListener(item -> {

            if(item.getItemId()==R.id.home_nav){
                FragmentManager fragment = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragment.beginTransaction();
                fragmentTransaction.replace(R.id.mainLay,new Home());
                fragmentTransaction.commit();
            }
            else if(item.getItemId()==R.id.info_nav){
                FragmentManager fragment = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragment.beginTransaction();
                fragmentTransaction.replace(R.id.mainLay,new InfoFrag());
                fragmentTransaction.commit();
            }
            else if(item.getItemId()==R.id.rank_menu){
                FragmentManager fragment = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragment.beginTransaction();
                fragmentTransaction.replace(R.id.mainLay,new RankingFrag());
                fragmentTransaction.commit();
            }

            return true;
        });
    }

    private void assignIds() {
        btmnav=findViewById(R.id.bottomNavigationView);
    }

    private void loadHomeFrag() {
        FragmentManager fragment = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragment.beginTransaction();
        fragmentTransaction.replace(R.id.mainLay,new Home());
        fragmentTransaction.commit();
    }

    private void loadScoreFrag(){
        FragmentManager fragment = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragment.beginTransaction();
        fragmentTransaction.replace(R.id.scoreLay,new LoadScoreFrag());
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {


        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            //additional code
            AlertDialog.Builder alertdialogbuilder;
            alertdialogbuilder=new AlertDialog.Builder(MainActivity.this);
            // alertdialogbuilder.setIcon(R.drwable.exp);
            alertdialogbuilder.setTitle("Quit?");
            alertdialogbuilder.setMessage("Do u want to Quit?");
            alertdialogbuilder.setCancelable(false);

            alertdialogbuilder.setPositiveButton("yes", (dialogInterface, i) -> finish());

            alertdialogbuilder.setNegativeButton("no", (dialogInterface, i) -> dialogInterface.cancel());

            AlertDialog alertDialog = alertdialogbuilder.create();
            alertDialog.show();
        } else {
            getSupportFragmentManager().popBackStack();
        }


    }

    //------------------------------------------

}