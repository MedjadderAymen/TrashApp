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

import com.bumptech.glide.Glide;
import com.medjay.trashapp.Challenge_details;
import com.medjay.trashapp.MyHelperSQLite;
import com.medjay.trashapp.Network.RetrofitBuilder;
import com.medjay.trashapp.Network.WebServerIntf;
import com.medjay.trashapp.R;
import com.medjay.trashapp.adapter_item_sql;
import com.medjay.trashapp.adapter_items;
import com.medjay.trashapp.entities.Challenge;
import com.medjay.trashapp.entities.ChallengeSQL;
import com.medjay.trashapp.entities.Photo;

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
                //------------------------- challenge to local data base----------------------------
                saveChallengesSQL(challengesList);

                //----------------------------------------------------------------------------------
            }

            @Override
            public void onFailure(Call<List<Challenge>> call, Throwable t) {
                Log.d("error: ",t.getMessage());
                generateDataListSQL();
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

    private void generateDataListSQL() {
        MyHelperSQLite myHelperSQLite=new MyHelperSQLite(getContext());

        List<ChallengeSQL> challenges=myHelperSQLite.getChallenges();
        adapter_item_sql adapterItemsSQL = new adapter_item_sql(getActivity(),R.layout.item_challenge,challenges);
        _lisListView.setAdapter(adapterItemsSQL);
    }

    private void saveChallengesSQL(List<Challenge> challengesList) {
        MyHelperSQLite myHelperSQLite=new MyHelperSQLite(getContext());
        myHelperSQLite.DeleteChallenges();
        for (int i=0;i<challengesList.size();i++){

            if (i==10){
                break;
            }else {

                List<Photo> photos = new ArrayList<>();
                photos.addAll(challengesList.get(i).getPhoto());

                ChallengeSQL challengeSQL=new ChallengeSQL(challengesList.get(i).getId_challenge(),
                        challengesList.get(i).getCreation_date(),
                        challengesList.get(i).getState(),
                        challengesList.get(i).getStarting_date(),
                        challengesList.get(i).getEnding_date(),
                        challengesList.get(i).getLatitude(),
                        challengesList.get(i).getLongitude(),
                        challengesList.get(i).getStreet(),
                        challengesList.get(i).getCity(),
                        challengesList.get(i).getZip_code(),
                        challengesList.get(i).getCountry(),
                        challengesList.get(i).getOwner().getUser_name(),
                        photos.get(0).getPath());
                myHelperSQLite.CreateChallenge(challengeSQL);
            }

        }
    }

    private void generateDataList(List<Challenge> challengesList) {

        adapterItems = new adapter_items(getActivity(),R.layout.item_challenge,challengesList);
        _lisListView.setAdapter(adapterItems);
    }


}
