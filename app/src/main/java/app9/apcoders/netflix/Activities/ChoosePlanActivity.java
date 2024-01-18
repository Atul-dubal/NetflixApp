package app9.apcoders.netflix.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import app9.apcoders.netflix.R;

public class ChoosePlanActivity extends AppCompatActivity {

    TextView signintext;
    AppCompatButton continuebutton;
    RadioButton radioButtonBasic, radioButtonStandard, radioButtonPremium;

    String PlanName,PlanCost,PlanCostFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_plan);
        getSupportActionBar().hide();
        signintext = findViewById(R.id.signinstepOne);
        continuebutton = findViewById(R.id.continuebutton);
        radioButtonBasic = findViewById(R.id.radiobuttonbasic);
        radioButtonStandard = findViewById(R.id.radiobuttonstandard);
        radioButtonPremium = findViewById(R.id.radiobuttonpremium);

        radioButtonBasic.setOnCheckedChangeListener(new Radio_Check());
        radioButtonStandard.setOnCheckedChangeListener(new Radio_Check());
        radioButtonPremium.setOnCheckedChangeListener(new Radio_Check());

        radioButtonPremium.setChecked(true);

        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ChoosePlanActivity.this, FinishUpActivity.class);
                i.putExtra("Planname",PlanName);
                i.putExtra("Plancost",PlanCost);
                i.putExtra("Plancostformat",PlanCostFormat);
                startActivity(i);
            }
        });

        signintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChoosePlanActivity.this, SigninActivity.class));
            }
        });
    }

    private class Radio_Check implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if(isChecked){
                if(buttonView.getId() == R.id.radiobuttonbasic){
                    PlanName="Basic";
                    PlanCost="349";
                    PlanCostFormat="₹ 749/month";
                    radioButtonStandard.setChecked(false);
                    radioButtonPremium.setChecked(false);
                }
                if(buttonView.getId()== R.id.radiobuttonstandard){
                    PlanName="Standard";
                    PlanCost="549";
                    PlanCostFormat="₹ 549/month";
                    radioButtonBasic.setChecked(false);
                    radioButtonPremium.setChecked(false);
                }
                if(buttonView.getId() == R.id.radiobuttonpremium){
                    PlanName="Premium";
                    PlanCost="749";
                    PlanCostFormat="₹ 749/month";
                    radioButtonStandard.setChecked(false);
                    radioButtonBasic.setChecked(false);
                }
            }
        }
    }
}