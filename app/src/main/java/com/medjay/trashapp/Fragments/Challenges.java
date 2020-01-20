package com.medjay.trashapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.medjay.trashapp.Challenge_details;
import com.medjay.trashapp.Network.RetrofitBuilder;
import com.medjay.trashapp.Network.WebServerIntf;
import com.medjay.trashapp.R;
import com.medjay.trashapp.adapter_items;
import com.medjay.trashapp.entities.Challenge;

import java.util.*;


public class Challenges extends Fragment {

    adapter_items adapterItems;
    List<Challenge> challengesList;
    ListView _lisListView;
    ProgressDialog progressDoalog;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_list, container, false);

        _lisListView= view.findViewById(R.id.LV);

        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        WebServerIntf serverIntf= RetrofitBuilder.getRetrofitInstance().create(WebServerIntf.class);
        Call<List<Challenge>> call = serverIntf.getChallenges();

        call.enqueue(new Callback<List<Challenge>>() {
            @Override
            public void onResponse(Call<List<Challenge>> call, retrofit2.Response<List<Challenge>> response) {
                challengesList=response.body();
                generateDataList(challengesList);
                progressDoalog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Challenge>> call, Throwable t) {
                Log.d("error: ",t.getMessage());
                progressDoalog.dismiss();
            }
        });

        _lisListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), Challenge_details.class);
                Bundle bundle=new Bundle();
                bundle.putInt("challenge_id",challengesList.get(i).getId_challenge());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }

    private void generateDataList(List<Challenge> challengesList) {

        adapterItems = new adapter_items(getActivity(),R.layout.item_challenge,challengesList);
        _lisListView.setAdapter(adapterItems);
    }


}
