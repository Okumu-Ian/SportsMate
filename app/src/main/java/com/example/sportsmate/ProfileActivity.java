package com.example.sportsmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputEditText full_name;
    private TextInputEditText email;
    private TextInputEditText phone;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        auth = FirebaseAuth.getInstance();
        initUI();
    }

    private void initUI(){
        user = auth.getCurrentUser();
        if(user != null){
            full_name = findViewById(R.id.edt_profile_full_name);
            email = findViewById(R.id.edt_profile_email);
            TextInputEditText password = findViewById(R.id.edt_profile_password);
            phone = findViewById(R.id.edt_profile_phone);
            AppCompatButton btn_update = findViewById(R.id.btn_update);

            //set values of edit texts
            String existing_pass = getSharedPreferences("PASS_STORAGE",MODE_PRIVATE).getString("PASS","PASSWORD");
            final String existing_phone = getSharedPreferences("PASS_STORAGE",MODE_PRIVATE).getString("PHONE","PHONE");
            full_name.setText(user.getDisplayName());
            email.setText(user.getEmail());
            password.setText(existing_pass);
            phone.setText(existing_phone);

            password.setEnabled(false);

            btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       if(Objects.requireNonNull(full_name.getText()).toString().equals(user.getDisplayName())
                               || Objects.requireNonNull(email.getText()).toString().equals(user.getEmail())
                               || Objects.requireNonNull(phone.getText()).toString().equals(existing_phone)){
                           Snackbar.make(view,"Nothing to update!!",Snackbar.LENGTH_LONG).show();
                       }else{
                           updateProfile(view,email.getText().toString(), phone.getText().toString(), full_name.getText().toString());
                       }
                    }
                });
        }else{
            startActivity(new Intent(this,loginActivity.class));
        }
    }

    private void updateProfile(final View view, String a_email, String a_phone, String a_fullname){
        user.updateEmail(a_email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().
                setDisplayName(a_fullname)
                .build();
        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Snackbar.make(view,"Successfully Updated.",Snackbar.LENGTH_LONG).show();
            }
        });
        getSharedPreferences("PASS_STORAGE",MODE_PRIVATE).edit().putString("PHONE",a_phone).apply();


    }
}
