package com.medjay.trashapp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.medjay.trashapp.Network.RetrofitBuilder;
import com.medjay.trashapp.Network.WebServerIntf;
import com.medjay.trashapp.R;
import com.medjay.trashapp.Welcome;
import com.medjay.trashapp.adapter_item_profil;
import com.medjay.trashapp.adapter_items;
import com.medjay.trashapp.entities.Challenge;
import com.medjay.trashapp.entities.Client;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Profile extends Fragment {

    adapter_item_profil adapterItems;
    List<Challenge> challengesList;
    ListView _lisListView;

    private View view;
    Client  client;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_profile, container, false);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.non_client);
        RelativeLayout relativeLayoutClient = (RelativeLayout) view.findViewById(R.id.client);

        preferences=getActivity().getSharedPreferences("prefs",MODE_PRIVATE);
        editor=preferences.edit();

        Gson gson = new Gson();
        String json = getActivity().getSharedPreferences("prefs",0).getString("client", null);
        if(json==null){
            relativeLayout.setVisibility(View.VISIBLE);
            relativeLayoutClient.setVisibility(View.GONE);

        }else {
            client= gson.fromJson(json, Client.class);
            relativeLayout.setVisibility(View.GONE);
            relativeLayoutClient.setVisibility(View.VISIBLE);

            TextView username = (TextView) view.findViewById(R.id.username);
            username.setText(client.getUser_name());

            TextView last_first= (TextView) view.findViewById(R.id.last_first);
            last_first.setText(client.getFirst_name()+" "+client.getLast_name());

            TextView phone = (TextView) view.findViewById(R.id.phone);
            phone.setText(client.getPhone_number());
        }


        FloatingActionButton logoff=view.findViewById(R.id.logoff);
        logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("client", null);
                editor.apply();

                startActivity(new Intent(getActivity(), Welcome.class));
                getActivity().finish();
            }
        });

        getMyChallenges();

        return view;
    }

    private void getMyChallenges() {
       //adapter_items=new adapter_item_profil(getActivity(),R.layout.list_item,)
    }


}
