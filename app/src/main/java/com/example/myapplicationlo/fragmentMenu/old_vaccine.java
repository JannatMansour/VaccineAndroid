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

import com.example.myapplicationlo.DrawNav;
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

public class old_vaccine extends Fragment {


    GetOldVaccineAPI[] GetOldVaccine;
    ArrayList<DataForOldVaccine> dataShow;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataShow = new ArrayList();

        OkHttpClient client;
        String url = "http://192.168.0.149/oldVaccine.php";
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
                GetOldVaccine = gson.fromJson(response.body().string(), GetOldVaccineAPI[].class);



                if(GetOldVaccine==null){
                    dataShow.add(new DataForOldVaccine("", "لا يوجد بيانات", ""));

                }else {



                    for (int i = 0; i < GetOldVaccine.length; i++) {
                        dataShow.add(new DataForOldVaccine(GetOldVaccine[i].type, GetOldVaccine[i].number, GetOldVaccine[i].date));
                    }
                }



            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }


        });

    }




    public class GetOldVaccineAPI {
        String type;
        String number;
        String date;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_old_vaccine, container, false);
        ListView listView = contentView.findViewById(R.id.old_ListView);

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
        ArrayList<DataForOldVaccine> dataVals=new ArrayList();


        private TextView Type;
        private TextView Number;
        private TextView Date;

        Context context=null;
        CustomAdapter(ArrayList<DataForOldVaccine> dataVals ,Context context) {
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
            View layoutName = inflater.inflate(R.layout.old_custom_vaccine, null);

            Type = (TextView) layoutName.findViewById(R.id.type_old);
            Number = (TextView) layoutName.findViewById(R.id.number_old);
            Date = (TextView) layoutName.findViewById(R.id.date_old);


            Type.setText(dataVals.get(i).Type);
            Number.setText(dataVals.get(i).Number);
            Date.setText(dataVals.get(i).Date);
            return layoutName;
        }


    }

}
