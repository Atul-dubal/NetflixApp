package app9.apcoders.netflix.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import app9.apcoders.netflix.R;

public class StepThree extends AppCompatActivity {

    LinearLayout paymentlinearlayout;
    String PlanName, PlanCost, PlanCostFormat, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_three);
        getSupportActionBar().hide();

        paymentlinearlayout = findViewById(R.id.paymentLinearLayout);

        Intent i = getIntent();
        PlanName = i.getStringExtra("PlanName");
        PlanCost = i.getStringExtra("PlanCost");
        PlanCostFormat = i.getStringExtra("PlanCostFormat");
        email = i.getStringExtra("EmailId");
        password = i.getStringExtra("Password");


        Toast.makeText(StepThree.this, ""+PlanName+"\n"+PlanCost+"\n"+PlanCostFormat, Toast.LENGTH_SHORT).show();

        paymentlinearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StepThree.this, Payment_Gateway.class);
                i.putExtra("PlanName", PlanName);
                i.putExtra("PlanCost", PlanCost);
                i.putExtra("PlanCostFormat", PlanCostFormat);
                i.putExtra("EmailId", email);
                i.putExtra("Password", password);
                startActivity(i);
            }
        });
    }
}