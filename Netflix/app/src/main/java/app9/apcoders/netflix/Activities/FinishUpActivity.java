package app9.apcoders.netflix.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import app9.apcoders.netflix.R;

public class FinishUpActivity extends AppCompatActivity {

    AppCompatButton continuebutton;
    TextView signin;
    String PlanName,PlanCost,PlanCostFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_up);
        getSupportActionBar().hide();
        continuebutton = findViewById(R.id.continuebuttonfinish);
        signin = findViewById(R.id.signinstepOne);


        Intent i = getIntent();
        PlanName = i.getStringExtra("Planname");
        PlanCost = i.getStringExtra("Plancost");
        PlanCostFormat= i.getStringExtra("Plancostformat");

        Toast.makeText(FinishUpActivity.this, ""+PlanName+"\n"+PlanCost+"\n"+PlanCostFormat, Toast.LENGTH_SHORT).show();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FinishUpActivity.this,SigninActivity.class));
                finish();
            }
        });

        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FinishUpActivity.this, StepTwo.class);
                i.putExtra("Planname",PlanName);
                i.putExtra("Plancost",PlanCost);
                i.putExtra("Plancostformat",PlanCostFormat);
                startActivity(i);
            }
        });

    }
}