package com.medjay.trashapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.medjay.trashapp.Fragments.Challenges;
import com.medjay.trashapp.Fragments.Maps;
import com.medjay.trashapp.Fragments.OverView;
import com.medjay.trashapp.Fragments.Review;
import com.medjay.trashapp.Network.RetrofitBuilder;
import com.medjay.trashapp.Network.WebServerIntf;
import com.medjay.trashapp.entities.Challenge;
import com.medjay.trashapp.entities.Photo;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class Challenge_details extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    int challenge_id;
    WebServerIntf serverIntf;
    Challenge challenge;

    List<Photo> photoList;
    Bundle bundleFrag;

    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_details);

        Bundle bundle = getIntent().getExtras();
        challenge_id=bundle.getInt("challenge_id");
        photoList=new ArrayList<>();
        serverIntf= RetrofitBuilder.getRetrofitInstance().create(WebServerIntf.class);

        getChallenge();

        tabLayout=findViewById(R.id.tabs);
        viewPager=findViewById(R.id.container);

        adapter=new ViewPagerAdapter(getSupportFragmentManager());


    }

    private void getChallenge() {
        Call<Challenge> call=serverIntf.getChallenge(challenge_id);
        call.enqueue(new Callback<Challenge>() {
            @Override
            public void onResponse(Call<Challenge> call, Response<Challenge> response) {
                challenge=response.body();
                photoList.addAll(challenge.getPhoto());

                bundleFrag = new Bundle();
                bundleFrag.putSerializable("challenge", challenge);

                // set Fragmentclass Arguments
                OverView fragobjOv = new OverView();
                fragobjOv.setArguments(bundleFrag);
                adapter.addFragment(fragobjOv,"OverView");

                // set Fragmentclass Arguments
                Review fragobjRv = new Review();
                fragobjRv.setArguments(bundleFrag);
                adapter.addFragment(fragobjRv,"Review");

                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);

                SliderInitialser();
            }

            @Override
            public void onFailure(Call<Challenge> call, Throwable t) {
                Log.i("error: ",t.getMessage());
            }
        });
    }

    private void SliderInitialser() {
        SliderView sliderView = findViewById(R.id.imageSlider);

        SliderAdapterExample adapter = new SliderAdapterExample(getApplicationContext(),photoList);

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(R.color.colorPrimary);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(6); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

}
