package app9.apcoders.netflix.Activities;

import static java.util.Calendar.MONTH;
import static java.util.Calendar.getInstance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
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

public class Payment_Gateway extends AppCompatActivity implements PaymentResultListener {

    AppCompatButton changebutton, startmembershipbutton;
    CheckBox isagree;
    FirebaseAuth firebaseAuth;

    FirebaseFirestore firebaseFirestore;
    TextInputEditText firstnameedittext, lastnameedittext, contactnoedittext;
    TextView plancostformattextview, plannametextview;
    Date today, validate;
    ProgressDialog progressDialog;

    String PlanName, PlanCost, PlanCostFormat, email, password, firstname, lastname, contactnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);
        getSupportActionBar().hide();
        changebutton = findViewById(R.id.changebuttonpaymentGAteway);
        startmembershipbutton = findViewById(R.id.startmembershipbutton);
        firstnameedittext = findViewById(R.id.firstname);
        isagree = findViewById(R.id.isagree);
        lastnameedittext = findViewById(R.id.lastname);
        plancostformattextview = findViewById(R.id.plancostformattextview);
        plannametextview = findViewById(R.id.plannametextview);
        contactnoedittext = findViewById(R.id.contactnumber);
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();

        Checkout.preload(this);

        Intent i = getIntent();
        PlanName = i.getStringExtra("PlanName");
        PlanCost = i.getStringExtra("PlanCost");
        PlanCostFormat = i.getStringExtra("PlanCostFormat");
        email = i.getStringExtra("EmailId");
        password = i.getStringExtra("Password");

        Calendar c = getInstance();
        today = c.getTime();
        c.add(MONTH, 1);
        validate = c.getTime();


        Toast.makeText(Payment_Gateway.this, "" + PlanName + "\n" + PlanCost + "\n" + PlanCostFormat, Toast.LENGTH_SHORT).show();

        plancostformattextview.setText(PlanCostFormat);
        plannametextview.setText(PlanName);
        changebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Payment_Gateway.this, ChoosePlanActivity.class));
            }
        });

        startmembershipbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstname = firstnameedittext.getText().toString();
                lastname = lastnameedittext.getText().toString();
                contactnumber = contactnoedittext.getText().toString();
                if (firstname.length() != 0 && lastname.length() != 0 && contactnumber.length() == 10 && isagree.isChecked()) {
                    startPayment();
                } else {
                    if (firstname.length() == 0) {
                        firstnameedittext.setError("Field cannot be empty");
                    }
                    if (lastname.length() == 0) {
                        lastnameedittext.setError("Field cannot be empty");
                    }
                    if (contactnumber.length() != 10) {
                        contactnoedittext.setError("Length of Number must be 10 digits");
                    }
                    if (isagree.isChecked() == false) {
                        isagree.setError("Please Accept Terms & Conditions.");
                    }
                }

            }

            private void startPayment() {
                progressDialog = new ProgressDialog(Payment_Gateway.this);
                progressDialog.setMessage("Loading ...");
                progressDialog.show();
                Toast.makeText(Payment_Gateway.this, "ok", Toast.LENGTH_SHORT).show();
                Checkout checkout = new Checkout();


                try {
                    JSONObject options = new JSONObject();
                    options.put("name", firstname + " " + lastname);
                    options.put("description", "Netflix " + PlanName + " Payment");
                    options.put("send_sms_hash", true);
                    options.put("allow_rotation", true);
                    double totalcost = Double.parseDouble(PlanCost);
                    totalcost = totalcost * 100;
                    options.put("currency", "INR");
                    options.put("amount", totalcost);

                    JSONObject prefill = new JSONObject();
                    prefill.put("email", email);
                    prefill.put("contact", contactnumber);

                    options.put("prefill", prefill);

                    checkout.open(Payment_Gateway.this, options);
                } catch (Exception e) {
                    Toast.makeText(Payment_Gateway.this, "Problem To open Payment Desk", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onPaymentSuccess(String s) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String uid = firebaseAuth.getCurrentUser().getUid();

                DocumentReference documentReference = firebaseFirestore.collection("Users").document(uid);
                HashMap<String, Object> Data = new HashMap<>();
                Data.put("Email", email);
                Data.put("FirstName", firstname);
                Data.put("LastName", lastname);
                Data.put("PlanName", PlanName);
                Data.put("PlanCost", PlanCost);
                Data.put("ContactNumber", contactnumber);
                Data.put("ValidDate", validate);

                documentReference.set(Data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Payment_Gateway.this, "Payment Done", Toast.LENGTH_LONG).show();
                            progressDialog.cancel();
                            startActivity(new Intent(Payment_Gateway.this, MainScreen.class));

                            finishAffinity();
                        } else {
                            progressDialog.cancel();
                            Toast.makeText(Payment_Gateway.this, "Subscription Not Added please Contact Our Customer.IF Your Amount Is Deducted.", Toast.LENGTH_LONG).show();
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
        });

    }

    @Override
    public void onPaymentError(int i, String s) {
        progressDialog.cancel();
        Toast.makeText(Payment_Gateway.this, "Payment Failed", Toast.LENGTH_LONG).show();
    }
}