package app9.apcoders.netflix.Activities;

import static java.util.Calendar.getInstance;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

import app9.apcoders.netflix.MainScreens.MainScreen;
import app9.apcoders.netflix.R;

public class SigninActivity extends AppCompatActivity {

    TextView forgotpasswordsignin, signuptextviewsignin;
    AppCompatButton signinbutton;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String Firefirstname, Firelastname, Firecontactnumber, Fireemail, Fireuid;
    ProgressBar progressBar;

    Date valid_date, today;
    String resetemail;
    TextInputEditText emailsignin, passwordsignin;
    Boolean W = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getSupportActionBar().hide();
        forgotpasswordsignin = findViewById(R.id.forgotpasswordsignin);
        signuptextviewsignin = findViewById(R.id.signuptextsignin);
        signinbutton = findViewById(R.id.signinbutton);
        emailsignin = findViewById(R.id.emailsignin);
        passwordsignin = findViewById(R.id.passwordsignin);
        progressBar = findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        progressBar.setVisibility(View.GONE);
        Calendar c = getInstance();
        today = c.getTime();
        forgotPassword();
        signup();
        signin();


    }

    private void signin() {
        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (emailsignin.getText().toString().length() > 8 && emailsignin.getText().toString().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$") && passwordsignin.getText().toString().length() > 7) {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(emailsignin.getText().toString(), passwordsignin.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = firebaseAuth.getCurrentUser().getUid();
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                if (firebaseUser != null) {

                                    DocumentReference documentReference = firebaseFirestore.collection("Users").document(uid);
                                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            valid_date = documentSnapshot.getDate("ValidDate");
                                            Fireemail = documentSnapshot.getString("Email");
                                            Firefirstname = documentSnapshot.getString("FirstName");
                                            Firelastname = documentSnapshot.getString("LastName");
                                            Firecontactnumber = documentSnapshot.getString("ContactNumber");
                                            Fireuid = uid;
                                            if (valid_date.compareTo(today) >= 0) {
                                                startActivity(new Intent(SigninActivity.this, MainScreen.class));
                                                finish();


                                            } else {
                                                Intent i = new Intent(SigninActivity.this, PaymentOverdue.class);
                                                i.putExtra("Email", Fireemail);
                                                i.putExtra("FirstName", Firefirstname);
                                                i.putExtra("LastName", Firelastname);
                                                i.putExtra("ContactNumber", Firecontactnumber);
                                                i.putExtra("UID", Fireuid);
                                                startActivity(i);
                                                finish();


                                            }
                                        }
                                    });


                                }
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            if (e instanceof FirebaseNetworkException) {
                                Toast.makeText(SigninActivity.this, "Please Turn On Internet", Toast.LENGTH_LONG).show();
                            }
                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(SigninActivity.this, "Invalid Credientials", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {
                    progressBar.setVisibility(View.GONE);

                    if (emailsignin.getText().toString().length() <= 8 || !emailsignin.getText().toString().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
                        emailsignin.setError("Enter Valid Email Id");

                    }
                    if (passwordsignin.getText().toString().length() < 8) {
                        passwordsignin.setError("Password Too Short");
                    }
                    if (emailsignin.getText().toString().length() == 0) {
                        emailsignin.setError("Field cannot be empty");
                    }
                    if (passwordsignin.getText().toString().length() == 0) {
                        passwordsignin.setError("Field cannot be empty");
                    }
                }


            }
        });
    }

    private void signup() {
        signuptextviewsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SigninActivity.this, SwipeScreen.class));
            }
        });

    }

    private void forgotPassword() {

        forgotpasswordsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetemail = emailsignin.getText().toString();
                if (resetemail.length() > 8 && resetemail.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
                    W = true;
                } else {
                    Toast.makeText(getApplicationContext(), "please enter email to reset password", Toast.LENGTH_SHORT).show();
                }


                if (W) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SigninActivity.this);
                    builder.setTitle("Reset Password.");
                    builder.setMessage("Click Yes To Get Password Reset Link");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            firebaseAuth.sendPasswordResetEmail(resetemail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "Password Reset Link Is Send", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    if (e instanceof FirebaseNetworkException) {
                                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                                    }
                                    if (e instanceof FirebaseAuthInvalidUserException) {

                                        Toast.makeText(getApplicationContext(), "User Not Found Please Check Email", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }


                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });


    }
}