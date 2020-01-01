package com.medjay.trashapp;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.medjay.trashapp.Network.RetrofitBuilder;
import com.medjay.trashapp.Network.WebServerIntf;
import com.medjay.trashapp.entities.Hello;

import java.util.List;

public class RetrofitActivity extends AppCompatActivity {

    WebServerIntf serverIntf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        serverIntf=RetrofitBuilder.getRetrofitInstance().create(WebServerIntf.class);
        Call<List<Hello>> call = serverIntf.getHelloWorld();

        call.enqueue(new Callback<List<Hello>>() {
            @Override
            public void onResponse(Call<List<Hello>> call, Response<List<Hello>> response) {
                TextView _t=findViewById(R.id.t);
                _t.setText(response.body().get(1).getMsg());
                Toast.makeText(getApplicationContext()," Response is: "+response.body().get(0).getMsg(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<List<Hello>> call, Throwable t) {
                TextView _t=findViewById(R.id.t);
                _t.setText(t.getMessage());
            }
        });

    }
}
