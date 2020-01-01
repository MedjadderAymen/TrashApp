package com.medjay.trashapp.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.medjay.trashapp.R;
import com.medjay.trashapp.adapter_items;
import com.medjay.trashapp.entities.Challenge;

import java.util.*;


public class List extends Fragment {

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_list, container, false);

        ListView listView=view.findViewById(R.id.LV);

        java.util.List<Challenge> challengesList=new ArrayList<>();

        adapter_items adapterItems=new adapter_items(getActivity(),R.layout.challenge_item,challengesList);

        listView.setAdapter(adapterItems);

        return view;
    }


}
