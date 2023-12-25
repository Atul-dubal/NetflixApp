package app9.apcoders.netflix.Activities;

import static java.util.Calendar.MONTH;
import static java.util.Calendar.getInstance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import app9.apcoders.netflix.MainScreens.MainScreen;
import app9.apcoders.netflix.R;

public class PaymentOverdue extends AppCompatActivity implements PaymentResultListener {
    TextView signintext;
    AppCompatButton continuebutton;
    RadioButton radioButtonBasic, radioButtonStandard, radioButtonPremium;
    FirebaseAuth firebaseAuth;
    String Firefirstname, Firelastname, Firecontactnumber, Fireemail, Fireuid;
    FirebaseFirestore firebaseFirestore;

    String PlanName, PlanCost, PlanCostFormat;
    Date today, validate;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_overdue);
        getSupportActionBar().hide();
        signintext = findViewById(R.id.signinstepOne);
        continuebutton = findViewById(R.id.continuebuttonPaymentOverdue);
        radioButtonBasic = findViewById(R.id.radiobuttonbasic);
        radioButtonStandard = findViewById(R.id.radiobuttonstandard);
        radioButtonPremium = findViewById(R.id.radiobuttonpremium);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        radioButtonBasic.setOnCheckedChangeListener(new Radio_Check());
        radioButtonStandard.setOnCheckedChangeListener(new Radio_Check());
        radioButtonPremium.setOnCheckedChangeListener(new Radio_Check());

        Intent i = getIntent();
        Fireemail = i.getStringExtra("Email");
        Firefirstname = i.getStringExtra("FirstName");
        Firelastname = i.getStringExtra("LastName");
        Firecontactnumber = i.getStringExtra("ContactNumber");
        Fireuid = i.getStringExtra("UID");

        Calendar c = getInstance();
        today = c.getTime();
        c.add(MONTH, 1);
        validate = c.getTime();

        radioButtonPremium.setChecked(true);

        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();

            }
        });

        signintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentOverdue.this, SigninActivity.class));
            }
        });
    }

    private class Radio_Check implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (isChecked) {
                if (buttonView.getId() == R.id.radiobuttonbasic) {
                    PlanName = "Basic";
                    PlanCost = "349";
                    PlanCostFormat = "₹ 749/month";
                    radioButtonStandard.setChecked(false);
                    radioButtonPremium.setChecked(false);
                }
                if (buttonView.getId() == R.id.radiobuttonstandard) {
                    PlanName = "Standard";
                    PlanCost = "549";
                    PlanCostFormat = "₹ 549/month";
                    radioButtonBasic.setChecked(false);
                    radioButtonPremium.setChecked(false);
                }
                if (buttonView.getId() == R.id.radiobuttonpremium) {
                    PlanName = "Premium";
                    PlanCost = "749";
                    PlanCostFormat = "₹ 749/month";
                    radioButtonStandard.setChecked(false);
                    radioButtonBasic.setChecked(false);
                }
            }
        }
    }

    private void startPayment() {
        progressDialog = new ProgressDialog(PaymentOverdue.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        Checkout checkout = new Checkout();


        try {
            JSONObject options = new JSONObject();
            options.put("name", Firefirstname + " " + Firelastname);
            options.put("description", "Netflix " + PlanName + " Payment");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            double totalcost = Double.parseDouble(PlanCost);
            totalcost = totalcost * 100;
            options.put("currency", "INR");
            options.put("amount", totalcost);

            JSONObject prefill = new JSONObject();
            prefill.put("email", Fireemail);
            prefill.put("contact", Firecontactnumber);

            options.put("prefill", prefill);

            checkout.open(PaymentOverdue.this, options);
        } catch (Exception e) {

            Toast.makeText(PaymentOverdue.this, "Problem To open Payment Desk", Toast.LENGTH_LONG).show();
        }
    }


    public void onPaymentSuccess(String s) {


        DocumentReference documentReference = firebaseFirestore.collection("Users").document(Fireuid);
        HashMap<String, Object> Data = new HashMap<>();
        Data.put("Email", Fireemail);
        Data.put("FirstName", Firefirstname);
        Data.put("LastName", Firelastname);
        Data.put("PlanName", PlanName);
        Data.put("PlanCost", PlanCost);
        Data.put("ContactNumber", Firecontactnumber);
        Data.put("ValidDate", validate);

        documentReference.set(Data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PaymentOverdue.this, "Payment Done", Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    startActivity(new Intent(PaymentOverdue.this, MainScreen.class));
                    finish();
                } else {
                    progressDialog.cancel();
                    Toast.makeText(PaymentOverdue.this, "Subscription Not Added please Contact Our Customer.IF Your Amount Is Deducted.", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseNetworkException) {
                    progressDialog.cancel();
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();


                }
            }
        });

    }


    @Override
    public void onPaymentError(int i, String s) {
        progressDialog.cancel();
        Toast.makeText(PaymentOverdue.this, "Payment Failed", Toast.LENGTH_LONG).show();
    }

}


