package com.example.ardasatata.testmap2;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private ImageView buttonSignup;
    private ProgressDialog progressDialog;
    private TextView textViewSignin;
    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    private String email,password;

    DatabaseReference userDatabase;

    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = (EditText) findViewById(R.id.registerEmail);
        editTextPassword = (EditText) findViewById(R.id.registerPassword);

        buttonSignup = (ImageView) findViewById(R.id.registerSignIn);
        textViewSignin = (TextView) findViewById(R.id.registerLogin);

        progressDialog = new ProgressDialog(RegisterActivity.this);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){
            // user is already logged in
            // open profile activity
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            //close this activity
            finish();

        }

        //attaching listener to button
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                if(TextUtils.isEmpty(email)){

                    Toast.makeText(RegisterActivity.this,"Please enter email", Toast.LENGTH_LONG).show();

                }
                else if(TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this,"Please enter password",Toast.LENGTH_LONG).show();

                }
                else if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                }
                else {


                    registerUser(email,password);


                }
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                    userDatabase = FirebaseDatabase.getInstance().getReference("users");

                    FirebaseUser FireUser = FirebaseAuth.getInstance().getCurrentUser();

                    String userId = FireUser.getUid();

                    User user1 = new User(userId,"",email);

                    userDatabase.child(userId).setValue(user1);
                }
                else {
                    Toast.makeText(getApplicationContext(), "no id got", Toast.LENGTH_SHORT).show();
                }

            }
        };



        textViewSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

    }

    public void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener); //firebaseAuth is of class FirebaseAuth
    }

    private void registerUser(String email,String password){

        //getting email and password from edit texts
        // String email = editTextEmail.getText().toString().trim();
        // String password  = editTextPassword.getText().toString().trim();

        //checking if email and passwords are empty


        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //display some message here
                            Toast.makeText(RegisterActivity.this,"Successfully registered",Toast.LENGTH_LONG).show();
                        }else{
                            //display some message here
                            Toast.makeText(RegisterActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }
}
