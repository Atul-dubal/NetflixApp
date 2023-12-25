package app9.apcoders.netflix.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import app9.apcoders.netflix.R;

public class StepOne extends AppCompatActivity {
    TextView signintext;
    AppCompatButton seeplansbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_one);
        getSupportActionBar().hide();
        signintext = findViewById(R.id.signinstepOne);
        seeplansbutton= findViewById(R.id.seeplansStepOne);

        signintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StepOne.this, SigninActivity.class));
            }
        });
        seeplansbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StepOne.this, ChoosePlanActivity.class));
            }
        });
    }
}