package com.medjay.trashapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.medjay.trashapp.Home;
import com.medjay.trashapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Connection extends Fragment {

    View view;

    TextInputLayout _email;
    TextInputLayout _password;
    Button _connect;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_connection, container, false);

        _email=view.findViewById(R.id.til_email);
        _password=view.findViewById(R.id.til_password);
        _connect=view.findViewById(R.id.btn_login);

        _connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_email.getEditText().getText().toString().equals("tp@gmail.com") &&_password.getEditText().getText().toString().equals("123") ){
                    Bundle bundle=new Bundle();
                    bundle.putString("email",_email.getEditText().getText().toString());
                    startActivity(new Intent(getContext(), Home.class).putExtras(bundle));
                    getActivity().finish();
                }else {
                    if (!_email.getEditText().getText().toString().equals("tp@gmail.com")){
                        _email.getEditText().setError("check email");
                    }

                    if (!_password.getEditText().getText().toString().equals("123")){
                        _password.getEditText().setError("check password");
                    }
                }
            }
        });



        return view;
    }
}
