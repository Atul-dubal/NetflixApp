package app9.apcoders.netflix.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import app9.apcoders.netflix.MainScreens.MainScreen;
import app9.apcoders.netflix.R;

public class AppUpdateScreen extends AppCompatActivity {
    AppCompatButton nothanksbtn,updatebtn;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_update_card);
        getSupportActionBar().hide();
        nothanksbtn = findViewById(R.id.nothanksbtn);
        updatebtn = findViewById(R.id.updatebtn);
        nothanksbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppUpdateScreen.this, MainScreen.class));
                finish();
            }
        });

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppUpdateScreen.this, MainScreen.class));
                finish();
            }
        });
    }

}
