package app9.apcoders.netflix.MainScreens;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import app9.apcoders.netflix.Activities.SigninActivity;
import app9.apcoders.netflix.R;

public class Settings extends AppCompatActivity {

    EditText newpassword;
    TextView email, plan, date;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser user;
    DocumentReference reference;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    Button resetpassword, signout;
    String Uid, emailstring, planstring;
    Date validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.SettingsBottomnavView);

        firebaseAuth = FirebaseAuth.getInstance();
        newpassword = findViewById(R.id.resetpasswordedittext);
        resetpassword = findViewById(R.id.resetpasswordbutton);
        signout = findViewById(R.id.signoutbutton);
        email = findViewById(R.id.emailsettings);
        plan = findViewById(R.id.plansettings);
        date = findViewById(R.id.datesettings);
        user = firebaseAuth.getInstance().getCurrentUser();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressDialog = new ProgressDialog(Settings.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        if (firebaseAuth.getCurrentUser() != null) {
            Uid = firebaseAuth.getCurrentUser().getUid();
            reference = firebaseFirestore.collection("Users").document(Uid);
            reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    validate = documentSnapshot.getDate("ValidDate");
                    emailstring = documentSnapshot.getString("Email");
                    planstring = documentSnapshot.getString("PlanCost");
                    email.setText(emailstring);
                    plan.setText("₹ " + planstring + "/mo.");
                    date.setText(validate + "");
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressDialog.cancel();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseNetworkException) {
                        Toast.makeText(getApplicationContext(), "NO internet connection", Toast.LENGTH_SHORT).show();

                    }
                    Toast.makeText(getApplicationContext(), "Error data not fetched", Toast.LENGTH_SHORT).show();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressDialog.cancel();
                }
            });

        }


        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.home) {
                    startActivity(new Intent(Settings.this, MainScreen.class));

                } else if (id == R.id.search) {
                    startActivity(new Intent(Settings.this, Search.class));

                } else if (id == R.id.settings) {

                } else {

                }
                return false;
            }
        });


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder signout = new AlertDialog.Builder(view.getContext());
                signout.setTitle("Do you really want to signout ?");
                signout.setMessage("Press YES to signout");
                signout.setCancelable(false);
                signout.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Intent x = new Intent(Settings.this, SigninActivity.class);
                        startActivity(x);
                        finish();

                    }
                });
                signout.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                signout.create().show();
            }
        });

        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(Settings.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                if (newpassword.getText().toString().length() > 7) {
                    firebaseAuth.signInWithEmailAndPassword(emailstring, newpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                EditText changepassword = new EditText(view.getContext());
                                AlertDialog.Builder updatepassword = new AlertDialog.Builder(view.getContext());
                                updatepassword.setTitle("Update Password?");
                                updatepassword.setCancelable(false);
                                changepassword.setHint("New password");
                                changepassword.setSingleLine();
                                updatepassword.setView(changepassword);
                                updatepassword.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        progressDialog.show();
                                        String newpasswordstring = changepassword.getText().toString();
                                        if (newpasswordstring.length() > 7) {
                                            user.updatePassword(newpasswordstring).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(), "Password Updated", Toast.LENGTH_SHORT).show();
                                                    newpassword.setText("");
                                                    progressDialog.cancel();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                    if (e instanceof FirebaseNetworkException) {
                                                        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                                                        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                                                        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
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
                                                        }

                                                    }
                                                    Toast.makeText(getApplicationContext(), "Password not updated", Toast.LENGTH_SHORT).show();
                                                    progressDialog.cancel();
                                                }
                                            });

                                        } else {
                                            progressDialog.cancel();
                                            Toast.makeText(getApplicationContext(), "Password to short please retry ", Toast.LENGTH_SHORT).show();

                                        }


                                    }
                                });
                                updatepassword.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        newpassword.setText("");
                                        progressDialog.cancel();
                                    }
                                });
                                updatepassword.create().show();
                            }

                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ConnectivityManager connectivityManager = null;
                            if (e instanceof FirebaseNetworkException) {
                            }
                            connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                            if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
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
                            }
                            if (e instanceof FirebaseAuthInvalidCredentialsException)
                                newpassword.setError("Incorrect password");
                            else
                                newpassword.setError("Incorrect password");
                            progressDialog.cancel();

                        }
                    });

                } else {
                    newpassword.setError("Password to short");
                    progressDialog.cancel();
                }

            }
        });
    }
}