package com.example.myapplicationlo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplicationlo.fragmentMenu.fragment_Reservations;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationlo.databinding.ActivityDrawNavBinding;
import com.google.firebase.messaging.FirebaseMessaging;

public class DrawNav extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDrawNavBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDrawNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarDrawNav.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        //add freagment here (Id)

        SharedPreferences prfs = this.getSharedPreferences("StoreIdPatient", Context.MODE_PRIVATE);
        String IdPatient = prfs.getString("IdPatient", "");
        String FullNamePatient = prfs.getString("FullNamePatient", "");

        View headerView = navigationView.getHeaderView(0);

        TextView navUsername = (TextView) headerView.findViewById(R.id.idP);
        navUsername.setText("Your ID: "+IdPatient);

        TextView idUser = (TextView) headerView.findViewById(R.id.nameP);
        idUser.setText("Your name "+FullNamePatient);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_chart, R.id.nav_reservations, R.id.nav_Vaccines,R.id.nav_Logout)
                .setOpenableLayout(drawer)

                .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_draw_nav);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }



    @Override
    public boolean onSupportNavigateUp() {

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_draw_nav);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();


    }


}