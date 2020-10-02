package com.amanpandey.cityguide.Common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amanpandey.cityguide.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgetPassword extends AppCompatActivity {

    //Variables
    ImageView screenIcon;
    TextView title,description;
    Button nextBtn;
    TextInputLayout phoneNumberTextField;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password);

        //Hooks
        screenIcon = findViewById(R.id.forget_password_icon);
        title = findViewById(R.id.forget_password_title);
        description = findViewById(R.id.forget_password_description);
        nextBtn = findViewById(R.id.forget_password_next_btn);
        phoneNumberTextField = findViewById(R.id.forget_phone_number);
        countryCodePicker = findViewById(R.id.forget_country_code_picker);

        //Animation Hook
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_animation);

        //Set animation to all the elements
        screenIcon.setAnimation(animation);
        title.setAnimation(animation);
        description.setAnimation(animation);
        phoneNumberTextField.setAnimation(animation);
        nextBtn.setAnimation(animation);
    }

    /*
    Call the OTP screen
     and pass phone Number
     for verification
    */

    public void verifyPhoneNumber(View view) {

        //Check Internet Connection
        if(!isConnected(this)){
            showCustomDialog();
        }

        //validate username and password
        if (!validateFields()) {
            return;
        }


        // get data

        String _completePhoneNumber = phoneNumberTextField.getEditText().getText().toString().trim();
        final String _phoneNo = "+" + countryCodePicker.getFullNumber() + _completePhoneNumber;
        //Database
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_phoneNo);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    phoneNumberTextField.setError(null);
                    phoneNumberTextField.setErrorEnabled(false);

                    Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                    intent.putExtra("phoneNo",_phoneNo);
                    intent.putExtra("whatToDo","updateData");
                    startActivity(intent);
                    finish();
                    }
                 else {
                    phoneNumberTextField.setError("No such user exists!");
                    phoneNumberTextField.requestFocus();
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ForgetPassword.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /////////////////////////////////////////////////////////////////////////////////////
    private boolean validateFields() {
    String _phoneNumber = phoneNumberTextField.getEditText().getText().toString().trim();
    if (_phoneNumber.isEmpty()) {
        phoneNumberTextField.setError("Phone number can not be empty");
        phoneNumberTextField.requestFocus();
        return false;
    }else {
        return true;
    }
}

    private boolean isConnected(ForgetPassword forgetPassword) {
        ConnectivityManager connectivityManager = (ConnectivityManager) forgetPassword.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(wifiConn != null && wifiConn.isConnected() || mobileConn != null && mobileConn.isConnected()){
            return true;
        }else{
            return false;
        }
    }

    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPassword.this);
        builder.setMessage("Please connect to the internet tp proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(),RetailerStartUpScreen.class));
                        finish();
                    }
                });
    }

    public void backPressed (View view){
        Intent intent = new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
    }
}