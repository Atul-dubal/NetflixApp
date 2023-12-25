package app9.apcoders.netflix.MainScreens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import app9.apcoders.netflix.R;

public class MovieDetails extends AppCompatActivity {

    ImageView movieimage;
    TextView moviename;
    Button Play;
    String name, image, fileurl, moviesid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        getSupportActionBar().hide();
        movieimage = findViewById(R.id.imagedeatils);
        moviename = findViewById(R.id.moviename);
        Play = findViewById(R.id.playbutton);
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
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
}