

package com.example.ranji.whatsapp;

import android.app.ProgressDialog;
import android.content . Intent ;
import  android.support . annotation.NonNull ;
import android.support .v7.app.AppCompatActivity ;
        import android.os.Bundle;
        import android.text . TextUtils;
        import android.view.View;
        import android.widget . Button;
        import android.widget . EditText ;
        import android.widget . Toast ;
        import com.google.android.gms.tasks.OnCompleteListener ;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.FirebaseException;
        import com.google.firebase.auth.AuthResult ;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
        import com.google.firebase.auth.PhoneAuthCredential ;
        import com.google.firebase.auth.PhoneAuthProvider ;
        import java.util . concurrent . TimeUnit ;
        public class PhoneLoginActivity extends AppCompatActivity
        {
        private Button SendVerificationCodeButton,VerifyButton;
        private EditText InputPhoneNumber,InputVerificationCode;
        private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
        private FirebaseAuth mAuth;
        private ProgressDialog loadingBar ;
        private String mVerificationId;
        private PhoneAuthProvider.ForceResendingToken mResendToken;
@Override
protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_phone_login) ;
        mAuth=FirebaseAuth.getInstance();
        SendVerificationCodeButton=(Button)findViewById(R.id.send_ver_code_btn) ;
        VerifyButton=(Button)findViewById(R.id.send_verify_btn) ;
        InputPhoneNumber=(EditText )findViewById(R.id.phone_input) ;
        InputVerificationCode=(EditText )findViewById( R. id.verification_code_input) ;
        loadingBar=new ProgressDialog(this) ;
        SendVerificationCodeButton.setOnClickListener(new View.OnClickListener(){
      @Override
          public void onClick(View view)
        {
        String phoneNumber=InputPhoneNumber.getText().toString();
        if(TextUtils.isEmpty (phoneNumber))
        {
        Toast.makeText ( PhoneLoginActivity.this, " Please enter your phone number first . . . " , Toast.LENGTH_SHORT).show();
        }
        else
        {
        loadingBar . setTitle( " PhoneVerification" ) ;
        loadingBar . setMessage( " please wait , while we are authenticating your phone. . . " ) ;
        loadingBar . setCanceledOnTouchOutside(false) ;
        loadingBar . show( ) ;
        PhoneAuthProvider.getInstance().verifyPhoneNumber (
        phoneNumber ,
        60,
        TimeUnit.SECONDS,
        PhoneLoginActivity.this,
        callbacks);
        }
        }
        });
        VerifyButton.setOnClickListener (new View.OnClickListener(){
@Override
public void onClick(View view)
        {
        SendVerificationCodeButton.setVisibility ( View.INVISIBLE) ;
        InputPhoneNumber . setVisibility ( View.INVISIBLE) ;
        String verificationCode=InputVerificationCode.getText().toString();
        if(TextUtils.isEmpty (verificationCode))
        {
        Toast.makeText( PhoneLoginActivity.this," Please write verificationcode first . . . " , Toast . LENGTH_SHORT).show();
        }
        else
        {
        loadingBar.setTitle("VerificationCode" );
        loadingBar.setMessage("please wait , while we are verifying verification code. . . " );
        loadingBar.setCanceledOnTouchOutside(false) ;
        loadingBar.show() ;
        PhoneAuthCredential credential =PhoneAuthProvider.getCredential(mVerificationId,verificationCode) ;
        signInWithPhoneAuthCredential(credential) ;
        }
        }
        } ) ;
        callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
      @Override
      public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
        {
        signInWithPhoneAuthCredential(phoneAuthCredential) ;
        }
    @Override
      public void onVerificationFailed(FirebaseException e)
        {
        loadingBar.dismiss();
        Toast.makeText(PhoneLoginActivity.this,"Invalid PhoneNumber, Please enter correct phonenumber with your country code. . . " , Toast . LENGTH_SHORT) . show( ) ;
        SendVerificationCodeButton.setVisibility(View.VISIBLE) ;
        InputPhoneNumber.setVisibility(View.VISIBLE) ;
        VerifyButton.setVisibility(View.INVISIBLE) ;
        InputVerificationCode.setVisibility(View.INVISIBLE) ;
        }
        public void onCodeSent(String verificationId,
        PhoneAuthProvider.ForceResendingToken token)
        {
        mVerificationId=verificationId;
        mResendToken=token;
        loadingBar.dismiss() ;
        Toast.makeText(PhoneLoginActivity.this, " Code has been sent , pleasecheck and verify " ,Toast.LENGTH_SHORT).show();
        SendVerificationCodeButton.setVisibility(View.INVISIBLE) ;
        InputPhoneNumber.setVisibility(View.INVISIBLE) ;
        VerifyButton.setVisibility(View.VISIBLE) ;
        InputVerificationCode.setVisibility(View.VISIBLE) ;
        }
        } ;
        }
        private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
        .addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){
@Override
public void onComplete( @NonNull Task<AuthResult >task){
        if(task.isSuccessful( ))
        {
        loadingBar.dismiss();
        Toast.makeText(PhoneLoginActivity.this," Congratulations,you are logged in successfully . . . " , Toast . LENGTH_SHORT).show( ) ;
        SendUserToMainActivity();
        }
        else
        {
        String message=task.getException().toString();
        Toast . makeText ( PhoneLoginActivity.this, " Error: "+message, Toast . LENGTH_SHORT).show();
        }
        }
        } ) ;
        }
        private void SendUserToMainActivity()
        {
        Intent mainIntent=new Intent( PhoneLoginActivity.this,MainActivity.class) ;
        startActivity ( mainIntent ) ;
        finish( ) ;
        }
        }