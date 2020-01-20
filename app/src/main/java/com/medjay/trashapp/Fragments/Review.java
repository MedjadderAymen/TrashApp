package com.medjay.trashapp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.medjay.trashapp.Comment_adapter;
import com.medjay.trashapp.Network.RetrofitBuilder;
import com.medjay.trashapp.Network.WebServerIntf;
import com.medjay.trashapp.R;
import com.medjay.trashapp.entities.Challenge;
import com.medjay.trashapp.entities.Client;
import com.medjay.trashapp.entities.Comment;
import com.medjay.trashapp.entities.Note;

import java.nio.file.WatchEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class Review extends Fragment {

    Challenge challenge;
    View view;

    ListView listComment;
    Comment_adapter comment_adapter;
    List<Comment> commentList;
    List<Note> notes;
    TextView chal_note;

    WebServerIntf serverIntf;

    Client client;

    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view= inflater.inflate(R.layout.fragment_review, container, false);

        final Gson gson = new Gson();
        final String json = getActivity().getSharedPreferences("prefs",0).getString("client", null);

        if(json!=null){
            client= gson.fromJson(json, Client.class);
            Toast.makeText(getContext(),"user id "+client.getId_user(),Toast.LENGTH_SHORT).show();
        }


        serverIntf= RetrofitBuilder.getRetrofitInstance().create(WebServerIntf.class);

        final RelativeLayout coordinatorLayout=view.findViewById(R.id.coordinatorLayout);

        challenge=(Challenge)getArguments().getSerializable("challenge");

        chal_note=(TextView) view.findViewById(R.id.chal_note);
        notes=new ArrayList<>();
        notes.addAll(challenge.getNote());


      if (!notes.isEmpty()){
          int rate=0;
          for (int i=0;i<notes.size();i++){
              rate=rate+notes.get(i).getNote_value();
          }

          chal_note.setText((rate/notes.size())+"/5 ("+notes.size()+")");
      }


      getParticipents();


        listComment= view.findViewById(R.id.comment_list);

        commentList=new ArrayList<>();
        commentList.addAll(challenge.getComment());
        comment_adapter=new Comment_adapter(getActivity(),R.layout.comment_item,commentList);
        listComment.setAdapter(comment_adapter);

        FloatingActionButton sendFloatingActionButton = view.findViewById(R.id.send);
        sendFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(json==null){
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "You must login", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }else {

                    sendComment();
                }
            }
        });

        RatingBar ratingBar=view.findViewById(R.id.rating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar,final float v, boolean b) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you rate this challenge "+v+" on 5?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(json==null){
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "You must login", Snackbar.LENGTH_LONG);

                            snackbar.show();
                        }else {
                            saveNote(v);
                        }
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog=builder.create();
                alertDialog.show();
            }
        });

        return view;
    }

    private void getParticipents() {

        Call<Set<Client>> clientCall=serverIntf.getParticipents(challenge.getId_challenge());
        clientCall.enqueue(new Callback<Set<Client>>() {
            @Override
            public void onResponse(Call<Set<Client>> call, Response<Set<Client>> response) {
                final TextView chal_participents=(TextView) view.findViewById(R.id.chal_participants);

                List<Client> clients=new ArrayList<>();
                clients.addAll(response.body());
                chal_participents.setText(clients.size()+"");
            }

            @Override
            public void onFailure(Call<Set<Client>> call, Throwable t) {

            }
        });

    }

    private void saveNote(float v) {

        Note note=new Note((int)v,client);
        Call<Note> noteCall=serverIntf.PostNote(note,challenge.getId_challenge(),client.getId_user());
        noteCall.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if (!notes.isEmpty()){
                    int rate=0;
                    for (int i=0;i<notes.size();i++){
                        rate=rate+notes.get(i).getId_note();
                    }

                    chal_note.setText((rate/notes.size())+"/5 ("+notes.size()+")");
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendComment() {
        DateFormat df= DateFormat.getDateInstance(DateFormat.SHORT);
        Date birthday = null;
        try {
            birthday = df.parse("2020-01-03 21:54");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final EditText commentEditText = (EditText) view.findViewById(R.id.comment);

        Comment comment=new Comment(1,commentEditText.getText().toString(),birthday,true,client);

        Call<Comment> commentCall=serverIntf.PostComment(comment,client.getId_user(),challenge.getId_challenge());
        commentCall.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                commentEditText.setText("");

                Toast.makeText(getActivity(),response.body()+"",Toast.LENGTH_SHORT).show();

                commentList.add(response.body());
                comment_adapter=new Comment_adapter(getActivity(),R.layout.comment_item,commentList);
                listComment.setAdapter(comment_adapter);
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }


}
