package app9.apcoders.netflix.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import app9.apcoders.netflix.R;

public class StepTwo extends AppCompatActivity {
    TextView signin;
    TextInputEditText emailedittext, passwordedittext;
    ProgressBar progressBar;
    AppCompatButton continuebuttonsteptwo;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String PlanName, PlanCost, PlanCostFormat, email, password;
    Boolean X;
    Boolean Y = false;

    Date today, validate;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_two);
        getSupportActionBar().hide();
        signin = findViewById(R.id.signinstepOne);
        continuebuttonsteptwo = findViewById(R.id.continuebuttonsteptwo);
        emailedittext = findViewById(R.id.emailsteptwo);
        passwordedittext = findViewById(R.id.passwordsteptwo);
        progressBar = findViewById(R.id.progressBarsteptwo);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        Intent i = getIntent();
        PlanName = i.getStringExtra("Planname");
        PlanCost = i.getStringExtra("Plancost");
        PlanCostFormat = i.getStringExtra("Plancostformat");


        Toast.makeText(StepTwo.this, "" + PlanName + "\n" + PlanCost + "\n" + PlanCostFormat, Toast.LENGTH_SHORT).show();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StepTwo.this, SigninActivity.class));
                finish();
            }
        });
        continuebuttonsteptwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(StepTwo.this);
                progressDialog.setMessage("Loading ...");
                progressDialog.show();
                emailedittext.setError(null);
                passwordedittext.setError(null);
                X = true;
                progressBar.setVisibility(View.VISIBLE);
                email = emailedittext.getText().toString();
                password = passwordedittext.getText().toString();
                if (email.length() > 8 && email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$") && password.length() > 7) {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                String uid = firebaseAuth.getCurrentUser().getUid();
                                X = false;
                                emailedittext.setError("User Already Register. Login Now");
                                progressBar.setVisibility(View.GONE);
                                progressDialog.cancel();

                            } else {
                                if (isEmailExits(email)) {
                                    X = false;
                                    emailedittext.setError("Email Already Registerd");
                                    progressBar.setVisibility(View.GONE);
                                    progressDialog.cancel();
                                }
                                if (task.getException() instanceof FirebaseNetworkException) {
                                    X = false;
                                    Toast.makeText(StepTwo.this, "Please Turn On The Internet", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    progressDialog.cancel();
                                }
                            }
                            if (X) {
                                Intent i = new Intent(StepTwo.this, StepThree.class);
                                i.putExtra("PlanName", PlanName);
                                i.putExtra("PlanCost", PlanCost);
                                i.putExtra("PlanCostFormat", PlanCostFormat);
                                i.putExtra("EmailId", email);
                                i.putExtra("Password", password);
                                startActivity(i);
                                progressBar.setVisibility(View.GONE);
                                progressDialog.cancel();
                            }

                        }
                    });

                } else {
                    progressDialog.cancel();
                    if (email.length() < 8 || !email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
                        emailedittext.setError("Enter Valid Email Id");
                        progressBar.setVisibility(View.GONE);
                    }
                    if (password.length() < 8) {

                        passwordedittext.setError("Password Too Short");
                        progressBar.setVisibility(View.GONE);
                    }
                    if (email.length() == 0) {
                        emailedittext.setError("Field cannot be empty ");
                        progressBar.setVisibility(View.GONE);
                    }
                    if (password.length() == 0) {
                        passwordedittext.setError("Field cannot be empty");
                        progressBar.setVisibility(View.GONE);

                    }
                }
            }

        });


    }

    private boolean isEmailExits(String email) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (!task.isSuccessful())
                                {
                                    try
                                    {
                                        throw task.getException();
                                    }
                                    // if user enters wrong email.
                                    catch (FirebaseAuthWeakPasswordException weakPassword)
                                    {
                                        Log.d("TAG", "onComplete: weak_password");

                                        // TODO: take your actions!
                                    }
                                    // if user enters wrong password.
                                    catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                                    {
                                        Log.d("TAG", "onComplete: malformed_email");

                                        // TODO: Take your action
                                    }
                                    catch (FirebaseAuthUserCollisionException existEmail)
                                    {
                                        Log.d("TAG", "onComplete: exist_email");
                                        Y= true;

                                        // TODO: Take your action
                                    }
                                    catch (Exception e)
                                    {
                                        Log.d("TAG", "onComplete: " + e.getMessage());
                                    }
                                }
                            }
                        }
                );
        return  Y;
    }
}