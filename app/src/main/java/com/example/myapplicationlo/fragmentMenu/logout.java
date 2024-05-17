package com.example.myapplicationlo.fragmentMenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplicationlo.DrawNav;
import com.example.myapplicationlo.MainActivity;
import com.example.myapplicationlo.R;
import com.google.firebase.messaging.FirebaseMessaging;

public class logout extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prfs = this.getActivity().getSharedPreferences("StoreIdPatient", Context.MODE_PRIVATE);
        String IdPatient = prfs.getString("IdPatient","");
        FirebaseMessaging.getInstance().unsubscribeFromTopic(IdPatient);

        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("StoreIdPatient", Context.MODE_PRIVATE).edit();
        editor.putString("IdPatient", "");
        editor.putString("FullNamePatient", "");
        editor.apply();



        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);





    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logout, container, false);
    }
}