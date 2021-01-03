package com.azhar.otpproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPCatchActivity extends AppCompatActivity {

    EditText edtOtp;
    Button btnSubmit;
    String verificationId;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_catch);

        edtOtp = findViewById(R.id.otpEt);
        btnSubmit = findViewById(R.id.submitBtn);
        fAuth = FirebaseAuth.getInstance();

        String number = getIntent().getStringExtra("mobileno");
        sendOtpCode(number);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtOtp.getText().toString())){
                    Toast.makeText(OTPCatchActivity.this,"Enter Otp",Toast.LENGTH_LONG).show();
                }else {
                    verifyOtpCode(edtOtp.getText().toString());
                }
            }
        });
    }

    private void verifyOtpCode(String otpCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,otpCode);
        signInProcess(credential);

    }

    private void signInProcess(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OTPCatchActivity.this,"err"+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = fAuth.getCurrentUser();
                Toast.makeText(OTPCatchActivity.this,"Successfully"+"\n"+user.getUid() , Toast.LENGTH_LONG).show();

            }
        });
    }

    private void sendOtpCode(String number) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
                .setPhoneNumber("+88"+number)
                .setActivity(this)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        String sms = phoneAuthCredential.getSmsCode();
                        if (sms != null){
                            verifyOtpCode(sms);
                            edtOtp.setText(sms);
                        }
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(OTPCatchActivity.this,"Otp error"+e.getMessage(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}