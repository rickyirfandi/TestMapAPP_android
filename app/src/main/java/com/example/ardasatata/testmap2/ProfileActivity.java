package com.example.ardasatata.testmap2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference dataUser;
    private User user;

    EditText ETname;
    EditText ETemail;
    EditText ETalamat;

    Button BTsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        dataUser = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid());

        ETname = findViewById(R.id.profileName);
        ETemail = findViewById(R.id.profileEmail);
        ETalamat = findViewById(R.id.profileAlamat);
        BTsave = findViewById(R.id.profileButtonSave);

        if (firebaseAuth.getCurrentUser() == null) {
            // user is already logged in
            // open profile activity
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            //close this activity
            finish();

        }

        dataUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                ETname.setText(user.getName());
                ETemail.setText(user.getEmail());
                ETalamat.setText(user.getAlamat());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        BTsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataUser.child("name").setValue(ETname.getText().toString());
                dataUser.child("email").setValue(ETemail.getText().toString());
                dataUser.child("alamat").setValue(ETalamat.getText().toString());

                Toast toast = Toast.makeText(getApplicationContext(), "Profile Saved", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }
}
