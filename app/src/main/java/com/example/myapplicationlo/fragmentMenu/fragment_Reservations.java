package com.example.myapplicationlo.fragmentMenu;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplicationlo.R;
import com.github.mikephil.charting.data.Entry;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class fragment_Reservations extends Fragment {






    GetReservationsAPI[] GetReserv;
    ArrayList<DataForReservation> dataShow;





    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {



        dataShow = new ArrayList();

        super.onCreate(savedInstanceState);

        OkHttpClient client;
        String url = "http://192.168.0.149/ShowreServations.php";
        client = new OkHttpClient();

        SharedPreferences prfs = this.getActivity().getSharedPreferences("StoreIdPatient", Context.MODE_PRIVATE);
        String IdPatient = prfs.getString("IdPatient", "");

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", IdPatient)
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
                dataShow.clear();
                GetReserv = gson.fromJson(response.body().string(), GetReservationsAPI[].class);


                if(GetReserv==null){
                    dataShow.add(new DataForReservation("", "لا يوجد بيانات", ""));

                }else {

                    for (int i = 0; i < GetReserv.length; i++) {
                        dataShow.add(new DataForReservation(GetReserv[i].FullName, GetReserv[i].Specialization, GetReserv[i].data_reservations));


                    }
                }




            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }


        });
    }



    public class GetReservationsAPI {
        String FullName;
        String Specialization;
        String data_reservations;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View contentView = inflater.inflate(R.layout.fragment__reservations, container, false);
        ListView listView = contentView.findViewById(R.id.res_List_View);

        try {
            Thread.sleep(200);

        } catch (InterruptedException e) {
            e.printStackTrace();


        }

        CustomAdapter listAdapter = new CustomAdapter(dataShow,getContext());
        listView.setAdapter(listAdapter);
        return contentView;

    }

    class CustomAdapter extends BaseAdapter {
        ArrayList<DataForReservation> dataVals=new ArrayList();


        private TextView Name;
        private TextView Specialization;
        private TextView Date;

        Context context=null;
        CustomAdapter(ArrayList<DataForReservation> dataVals ,Context context) {
            this.context = context;
            this.dataVals=dataVals;
        }

        @Override
        public int getCount() {
            return dataVals.size();
        }

        @Override
        public Object getItem(int i) {
            return dataVals.get(i);
        }

        @Override
        public long getItemId(int i) {
            return dataVals.get(i).hashCode();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layoutName = inflater.inflate(R.layout.reservation_custom_layout, null);

            Name = (TextView) layoutName.findViewById(R.id.Name);
            Specialization = (TextView) layoutName.findViewById(R.id.Specialization);
            Date = (TextView) layoutName.findViewById(R.id.date_res);


            Name.setText(dataVals.get(i).name);
            Specialization.setText(dataVals.get(i).Specialization);
            Date.setText(dataVals.get(i).Date);
            return layoutName;
        }


    }
}