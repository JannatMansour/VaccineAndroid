package com.example.myapplicationlo.fragmentMenu;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplicationlo.DrawNav;
import com.example.myapplicationlo.R;


public class fragment_Vaccines extends Fragment {
   

    private Button oldBtnVarible;
    private Button newBtnVarible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.fragment__vaccines, null);
        oldBtnVarible = root.findViewById(R.id.oldBtn);
        newBtnVarible = root.findViewById(R.id.newBtn);

        oldBtnVarible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//open fragment old vaccine

                FragmentTransaction ft = getParentFragmentManager().beginTransaction();

                ft.replace(R.id.nav_host_fragment_content_draw_nav, old_vaccine.class,null);
                ft.addToBackStack(null);
                ft.setReorderingAllowed(true);
                ft.commit();
            };

        });

        newBtnVarible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//open fragment new vaccine

                FragmentTransaction ft = getParentFragmentManager().beginTransaction();

                ft.replace(R.id.nav_host_fragment_content_draw_nav, new_vaccine.class,null);
                ft.addToBackStack(null);
                ft.setReorderingAllowed(true);
                ft.commit();

            };

        });
        return  root;
    }



}