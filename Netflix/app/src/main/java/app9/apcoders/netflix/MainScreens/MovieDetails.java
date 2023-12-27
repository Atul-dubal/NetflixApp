package app9.apcoders.netflix.MainScreens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app9.apcoders.netflix.Adapters.CommentsRecyclerViewAdapter;
import app9.apcoders.netflix.Modal.CommentsModal;
import app9.apcoders.netflix.R;

public class MovieDetails extends AppCompatActivity {

    ImageView movieimage;
    ImageButton sendcomment;
    RecyclerView commentRecyclerView;
    TextView moviename;
    LinearLayout Play;
    TextInputEditText sendcommenttext;
    String name, image, fileurl, moviesid;
    Date commentDate;
    ArrayList<CommentsModal> commentsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        getSupportActionBar().hide();
        movieimage = findViewById(R.id.imagedeatils);
        commentRecyclerView = findViewById(R.id.commentRecyclerView);
        sendcomment = findViewById(R.id.sendcomment);
        sendcommenttext = findViewById(R.id.sendcommenttext);

        moviename = findViewById(R.id.moviename);
        Play = findViewById(R.id.playbutton);
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        Calendar c = Calendar.getInstance();
        commentDate = c.getTime();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        commentRecyclerView.setLayoutManager(linearLayoutManager);

        CommentsRecyclerViewAdapter adapter = new CommentsRecyclerViewAdapter(this, commentsList);
        commentRecyclerView.setAdapter(adapter);

        sendcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commenttextedittext = sendcommenttext.getText().toString();
                if (commenttextedittext != null) {
                    commentsList.add(new CommentsModal("Atul Dubal Admin", commenttextedittext, commentDate));
                    adapter.notifyDataSetChanged();
                    sendcommenttext.setText("");

                } else {
                    Toast.makeText(MovieDetails.this, "Please Write Something.", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Please turn on your internet connection to continue.");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    recreate();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);
        } else {


            moviesid = getIntent().getStringExtra("movieId");
            name = getIntent().getStringExtra("movieName");
            image = getIntent().getStringExtra("movieImageUrl");
            fileurl = getIntent().getStringExtra("movieFile");
            Glide.with(this).load(image).into(movieimage);
            moviename.setText(name);
            loadcomments();
            Play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MovieDetails.this, VideoPlayer.class);
                    i.putExtra("url", fileurl);
                    startActivity(i);
                }
            });


        }

    }

    private void loadcomments() {


        commentsList.add(new CommentsModal("Atul Dubal", "Nice Movie", commentDate));
        commentsList.add(new CommentsModal("Amit Dubal", " Good", commentDate));
        commentsList.add(new CommentsModal("Vishal Dubal", "I like this movie", commentDate));


    }
}