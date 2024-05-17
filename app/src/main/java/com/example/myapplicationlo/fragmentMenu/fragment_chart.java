package com.example.myapplicationlo.fragmentMenu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.myapplicationlo.DrawNav;
import com.example.myapplicationlo.MainActivity;
import com.example.myapplicationlo.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class fragment_chart extends Fragment {



    LineChartAgeWeight[] LineC;
    ArrayList<Entry> dataVals;
    ArrayList<Entry> dataHigh;



    @Override
    public void onCreate(Bundle savedInstanceState) {




        dataVals = new ArrayList<Entry>();
        dataHigh = new ArrayList<Entry>();

        super.onCreate(savedInstanceState);



        OkHttpClient client;
        String url = "http://192.168.0.149/digram.php";
        client = new OkHttpClient();

        SharedPreferences prfs = this.getActivity().getSharedPreferences("StoreIdPatient", Context.MODE_PRIVATE);
        String IdPatient = prfs.getString("IdPatient","");
        FirebaseMessaging.getInstance().subscribeToTopic(IdPatient);

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
                dataVals.clear();
                dataHigh.clear();
                LineC = gson.fromJson(response.body().string(), LineChartAgeWeight[].class);

                if(LineC==null){
                    dataVals.add(new Entry(0, 0));
                    dataHigh.add(new Entry(0, 0));
                }else {
                    dataHigh.add(new Entry(3.5f, 49.15f));
                    dataHigh.add(new Entry(5.2f, 50.0f));
                    dataHigh.add(new Entry(6.4f, 64.0f));
                    dataHigh.add(new Entry(10.0f, 74.84f));
                    dataHigh.add(new Entry(11.0f, 80.0f));

                    for(int i=0;i<LineC.length;i++) {
                        dataVals.add(new Entry(LineC[i].Weight, LineC[i].High));

                    }
                }




            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }


        });



    }
    public class LineChartAgeWeight {
        float Age;
        float Weight;
        float High;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.fragment_chart, null);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        LineChart mpLineChart = (LineChart)root.findViewById(R.id.line_chart);

        try {
            Thread.sleep(200);

        } catch (InterruptedException e) {
            e.printStackTrace();


        }

        LineDataSet  lineDataSet1=new LineDataSet(dataVals,"Current value");
        LineDataSet  lineDataSet2=new LineDataSet(dataHigh,"Standard");

        lineDataSet1.setColor(Color.RED);
        lineDataSet2.setColor(Color.BLUE);

        Description description =new Description();
        description.setText("X=Weight , Y=High");
        description.setTextSize(15);
        mpLineChart.setDescription(description);

        Legend legend= mpLineChart.getLegend();
        legend.setTextSize(15);

        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);

        LineData data = new LineData(dataSets);
        mpLineChart.setData(data);
        mpLineChart.invalidate();




        return root;

    }
}