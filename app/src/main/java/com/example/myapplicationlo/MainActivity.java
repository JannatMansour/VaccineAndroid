package com.example.myapplicationlo;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import android.app.MediaRouteButton;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplicationlo.fragmentMenu.fragment_Reservations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private EditText Edit1;
    private EditText Edit2;
    String id;
    String password;
    private OkHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Edit1=(EditText)findViewById(R.id.E1);
        Edit2=(EditText)findViewById(R.id.E2);
        login = (Button) findViewById(R.id.getBtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {LOGIN ();}

        });

        SharedPreferences prfs = getSharedPreferences("StoreIdPatient", Context.MODE_PRIVATE);
        String IdPatient = prfs.getString("IdPatient","");
        if(IdPatient!=""){
            Intent myIntent = new Intent(MainActivity.this, DrawNav.class);

            MainActivity.this.startActivity(myIntent);

        }

    }



    @Override
    public void onBackPressed() {

            super.onBackPressed();
            finish();

    }
    private void LOGIN() {


        id=Edit1.getText().toString();
        password=Edit2.getText().toString();
        String url = "http://192.168.0.149/config.php";
        client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", id)
                .addFormDataPart("password",password)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);




        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                Gson gson = new Gson();
                Success[] check = gson.fromJson(response.body().string(), Success[].class);
                if(check[0].id !=null){

                    //Store id Patient
                    SharedPreferences.Editor editor = getSharedPreferences("StoreIdPatient", MODE_PRIVATE).edit();
                    editor.putString("IdPatient", id);
                    editor.putString("FullNamePatient", check[0].FullName);
                    editor.apply();



                    //open draw navgation
                    Intent myIntent = new Intent(MainActivity.this, DrawNav.class);

                    MainActivity.this.startActivity(myIntent);
                }else {

                    runOnUiThread(() -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        builder.setCancelable(true);
                        builder.setTitle("Warning Message");
                        builder.setMessage("Wrong in your ID or password ");



                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.show();
                    });

                }


            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setCancelable(true);
                    builder.setTitle("Warning Message");
                    builder.setMessage("Problem with server");



                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                });
            }


        });
    }
   public class Success{
       String id;
       String password;
       String FullName;
       String gender;
    }
}
