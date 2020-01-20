package com.medjay.trashapp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.medjay.trashapp.Home;
import com.medjay.trashapp.Network.RetrofitBuilder;
import com.medjay.trashapp.Network.WebServerIntf;
import com.medjay.trashapp.R;
import com.medjay.trashapp.Welcome;
import com.medjay.trashapp.entities.Client;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Inscription extends Fragment {

    View view;
    Date birthday;
    Button _inscription ;
    TextInputLayout lastname,firstname,username,password,birthdate,phonenumber;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inscription, container, false);

        preferences=getActivity().getSharedPreferences("prefs",MODE_PRIVATE);
        editor=preferences.edit();

        _inscription =view.findViewById(R.id.btn_signup);
        lastname =view.findViewById(R.id.til_lastname);
        firstname =view.findViewById(R.id.til_firstname);
        username =view.findViewById(R.id.til_username);
        password =view.findViewById(R.id.til_password);
        birthdate=view.findViewById(R.id.til_birthday);
        phonenumber =view.findViewById(R.id.til_phone);

        final WebServerIntf webServerIntf= RetrofitBuilder.getRetrofitInstance().create(WebServerIntf.class);

        DateFormat df= DateFormat.getDateInstance(DateFormat.SHORT);
        birthday = null;
        try {
            birthday = df.parse(birthdate.getEditText().getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        _inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DateFormat df= DateFormat.getDateInstance(DateFormat.SHORT);
                Date birthday = null;
                try {
                    birthday = df.parse("2020-01-03");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Client client=new Client(1,
                        lastname.getEditText().getText().toString(),
                        firstname.getEditText().getText().toString(),
                        username.getEditText().getText().toString(),
                        password.getEditText().getText().toString(),
                        birthday,
                        phonenumber.getEditText().getText().toString(),
                        Build.VERSION.RELEASE+"");

                Call<Client> call=webServerIntf.register(client);
                call.enqueue(new Callback<Client>() {
                    @Override
                    public void onResponse(Call<Client> call, Response<Client> response) {
                        Client client=response.body();
                        String ClientJSONString = new Gson().toJson(client);
                        editor.putString("client", ClientJSONString);
                        editor.apply();

                        startActivity(new Intent(getActivity(), Home.class));
                        getActivity().finish();
                    }

                    @Override
                    public void onFailure(Call<Client> call, Throwable t) {
                        Toast.makeText(getContext(),"reponse "+t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }

}
