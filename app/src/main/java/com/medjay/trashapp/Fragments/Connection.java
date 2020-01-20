package com.medjay.trashapp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.medjay.trashapp.Home;
import com.medjay.trashapp.Network.RetrofitBuilder;
import com.medjay.trashapp.Network.WebServerIntf;
import com.medjay.trashapp.R;
import com.medjay.trashapp.Welcome;
import com.medjay.trashapp.entities.Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Connection extends Fragment {

    View view;

    TextInputLayout _email;
    TextInputLayout _password;
    Button _connect;
    LoginButton _connectFacebook;
    TextView textView;

    CallbackManager callbackManager;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_connection, container, false);

        final RelativeLayout coordinatorLayout=(RelativeLayout) view.findViewById(R.id.rl);
        _email = view.findViewById(R.id.til_email);
        _password = view.findViewById(R.id.til_password);
        _connect = view.findViewById(R.id.login);

        preferences=getActivity().getSharedPreferences("prefs",MODE_PRIVATE);
        editor=preferences.edit();

        final WebServerIntf webServerIntf= RetrofitBuilder.getRetrofitInstance().create(WebServerIntf.class);
        _connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call <Client> call=webServerIntf.login(_email.getEditText().getText().toString(),
                        _password.getEditText().getText().toString());

                call.enqueue(new Callback<Client>() {
                    @Override
                    public void onResponse(Call<Client> call, Response<Client> response) {
                        Client client=response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(client);
                        editor.putString("client", json);
                        editor.apply();

                        startActivity(new Intent(getActivity(), Home.class));
                        getActivity().finish();
                    }

                    @Override
                    public void onFailure(Call<Client> call, Throwable t) {
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Username or Password maybe wrong!", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                });
            }
        });

        return view;
    }

}