package com.example.ranji.whatsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {
      Button createacc;
      EditText username,password;
      TextView alreadyhave;
      FirebaseAuth mAuth;
      ProgressDialog loadingBar;
      DatabaseReference rootref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance();
        rootref=FirebaseDatabase.getInstance().getReference();
        initializefields();
        alreadyhave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
             senduserToLoginActivity();
            }
        });
       createacc.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               CreatenewAccount();
           }
       });
    }

    private void CreatenewAccount() {
        String Email = username.getText().toString();
        String pass = password.getText().toString();

        if (TextUtils.isEmpty(Email)) {
            Toast.makeText(RegisterActivity.this, "Enter Username", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(RegisterActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
        }

        else
            {
                loadingBar.setTitle("Creating new Account");
                loadingBar.setMessage("Pleease Wait,while creating new Account for you");
                loadingBar.setCanceledOnTouchOutside(true);
                loadingBar.show();
         mAuth.createUserWithEmailAndPassword(Email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {

                 if(task.isSuccessful())
                 {
                     String currentuserId=mAuth.getCurrentUser().getUid();
                     rootref.child("child").child(currentuserId).setValue("");
                     sendUserToMainActivity();
                     Toast.makeText(RegisterActivity.this,"Account Created Successfully",Toast.LENGTH_SHORT).show();
                     loadingBar.dismiss();
                 }
                 else{
                     String messege=task.getException().toString();
                     Toast.makeText(RegisterActivity.this,"Error"+messege,Toast.LENGTH_SHORT).show();
                     loadingBar.dismiss();

                 }
             }
         });
        }
    }
    private void initializefields()
    {
        createacc=(Button)findViewById(R.id.btn_createAcc);
        username=(EditText)findViewById(R.id.etEmail);
        password=(EditText)findViewById(R.id.etPass);
        alreadyhave=(TextView)findViewById(R.id.tv_already);
         loadingBar=new ProgressDialog(this);
    }

    private void senduserToLoginActivity()
    {
        Intent loginIntent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(loginIntent);
    }

    private void sendUserToMainActivity()
    {
        Intent mainIntent=new Intent(RegisterActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
