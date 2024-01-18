package app9.apcoders.netflix.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

import app9.apcoders.netflix.MainScreens.MainScreen;
import app9.apcoders.netflix.R;

public class SplashScreenActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    Date today, Valid_Date;
    String UID, FireFirstName, FireLastName, FireContactNumber, FireEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        Calendar c = Calendar.getInstance();
        today = c.getTime();


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenActivity.this);
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


                    if (firebaseAuth.getCurrentUser() != null) {
                        UID = firebaseAuth.getCurrentUser().getUid();
                        try {


                            DocumentReference documentReference = firebaseFirestore.collection("Users").document(UID);

                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    FireFirstName = documentSnapshot.getString("FirstName");
                                    FireLastName = documentSnapshot.getString("LastName");
                                    FireContactNumber = documentSnapshot.getString("ContactNumber");
                                    FireEmail = documentSnapshot.getString("Email");
                                    Valid_Date = documentSnapshot.getDate("ValidDate");

                                    if (Valid_Date.compareTo(today) >= 0) {
                                        startActivity(new Intent(SplashScreenActivity.this, AppUpdateScreen.class));
                                        finish();
                                    } else {
                                        Intent i = new Intent(SplashScreenActivity.this, PaymentOverdue.class);
                                        i.putExtra("Email", FireEmail);
                                        i.putExtra("FirstName", FireFirstName);
                                        i.putExtra("LastName", FireLastName);
                                        i.putExtra("ContactNumber", FireContactNumber);
                                        i.putExtra("UID", UID);
                                        startActivity(i);
                                        finish();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (e instanceof FirebaseNetworkException) {
                                        Toast.makeText(SplashScreenActivity.this, "No Internet Connection.", Toast.LENGTH_LONG).show();
                                    } else {
                                        firebaseAuth.signOut();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            if (e instanceof FirebaseNetworkException) {
                                Toast.makeText(SplashScreenActivity.this, "No Internet Connection.", Toast.LENGTH_LONG).show();

                            }
                        }
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, SigninActivity.class));
                        finish();
                    }
                }
            }
        }, 1000);
    }
}