package com.azhar.otpproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PhoneNoAuth extends AppCompatActivity {

    EditText number;
    Button btnSendOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_no_auth);

        number = findViewById(R.id.phoneEt);
        btnSendOtp = findViewById(R.id.sendOtpBtn);
        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(number.getText().toString())){
                    Toast.makeText(PhoneNoAuth.this, "Some filed are empty!!!", Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(PhoneNoAuth.this, OTPCatchActivity.class);
                    intent.putExtra("mobileno", number.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}