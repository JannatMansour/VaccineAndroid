package com.example.myapplicationlo.fragmentMenu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplicationlo.R;
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


public class new_vaccine extends Fragment {

    GetNewVaccineAPI[] GetNewVaccine;
    ArrayList<DataForNewVaccine> dataShow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataShow = new ArrayList();

        OkHttpClient client;
        String url = "http://192.168.0.149/newVaccine.php";
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
                GetNewVaccine = gson.fromJson(response.body().string(), GetNewVaccineAPI[].class);


                if(GetNewVaccine==null ||GetNewVaccine.length==0){
                    dataShow.add(new DataForNewVaccine("", "لا يوجد بيانات", ""));

                }else {


                    for (int i = 0; i < GetNewVaccine.length; i++) {
                        dataShow.add(new DataForNewVaccine(GetNewVaccine[i].type, GetNewVaccine[i].number, GetNewVaccine[i].date));
                    }
                }



            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }


        });

    }
    public class GetNewVaccineAPI {
        String type;
        String number;
        String date;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View contentView = inflater.inflate(R.layout.fragment_new_vaccine, container, false);
        ListView listView = contentView.findViewById(R.id.new_ListView);

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
        ArrayList<DataForNewVaccine> dataVals=new ArrayList();


        private TextView Type;
        private TextView Number;
        private TextView Date;

        Context context=null;
        CustomAdapter(ArrayList<DataForNewVaccine> dataVals ,Context context) {
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
            View layoutName = inflater.inflate(R.layout.new_custom_vaccine, null);

            Type = (TextView) layoutName.findViewById(R.id.type_new);
            Number = (TextView) layoutName.findViewById(R.id.number_new);
            Date = (TextView) layoutName.findViewById(R.id.date_new);


            Type.setText(dataVals.get(i).Type);
            Number.setText(dataVals.get(i).Number);
            Date.setText(dataVals.get(i).Date);
            return layoutName;
        }


    }
}