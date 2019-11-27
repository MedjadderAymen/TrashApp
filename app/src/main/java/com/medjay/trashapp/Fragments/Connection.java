package com.medjay.trashapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.textfield.TextInputLayout;
import com.medjay.trashapp.Home;
import com.medjay.trashapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Connection extends Fragment {

    View view;

    TextInputLayout _email;
    TextInputLayout _password;
    Button _connect;
    LoginButton _connectFacebook;
    TextView textView;

    CallbackManager callbackManager;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_connection, container, false);

        _email = view.findViewById(R.id.til_email);
        _password = view.findViewById(R.id.til_password);
        _connect = view.findViewById(R.id.btn_login);
        _connectFacebook = view.findViewById(R.id.btn_facebook);
        textView=view.findViewById(R.id.signinto);

        callbackManager = CallbackManager.Factory.create();
        _connectFacebook.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));

        _connectFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accesToken = loginResult.getAccessToken().getToken();

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("response",response.toString());
                        getData(object);
                    }
                });

                Bundle params=new Bundle();
                params.putString("fields","id,email,birthday,friends");
                graphRequest.setParameters(params);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        //if already logged in



        _connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_email.getEditText().getText().toString().equals("tp@gmail.com") && _password.getEditText().getText().toString().equals("123")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("email", _email.getEditText().getText().toString());
                    startActivity(new Intent(getContext(), Home.class).putExtras(bundle));
                    getActivity().finish();
                } else {
                    if (!_email.getEditText().getText().toString().equals("tp@gmail.com")) {
                        _email.getEditText().setError("check email");
                    }

                    if (!_password.getEditText().getText().toString().equals("123")) {
                        _password.getEditText().setError("check password");
                    }
                }
            }
        });


        _connectFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    private void getData(JSONObject obj) {

        try {
            URL profileçpic=new URL("https://graph.facebook.con/"+obj.getString("id")+"/picture?width=250&height=50");
            textView.setText(obj.getString("email"));
            Toast.makeText(getContext(),"email is "+obj.getString("email"),Toast.LENGTH_LONG);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
