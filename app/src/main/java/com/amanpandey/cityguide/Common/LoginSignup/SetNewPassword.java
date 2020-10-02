package com.amanpandey.cityguide.Common.LoginSignup;

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
import android.widget.Button;

import com.amanpandey.cityguide.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNewPassword extends AppCompatActivity {

    //bvariables
    TextInputLayout newPassword,confirmPassword;
    Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_set_new_password);

        //Hooks
        newPassword = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.confirm_password);
        updateBtn = findViewById(R.id.set_new_password_btn);

    }

    /*
            Update Users
            password on
            Button CLick
         */
    public void setNewPasswordBtn(View view){
        //Check Internet Connection
        if(!isConnected(this)){
            showCustomDialog();
        }

        //validate username and password
        if (!validatePassword() | !validateConfirmPassword()) {
            return;
        }

        //Get data from fields
        String _newPassword = newPassword.getEditText().getText().toString().trim();
        String _phoneNumber = getIntent().getStringExtra("phoneNo");
        //Update Data in Firebase and in Sessions
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(_phoneNumber).child("password").setValue(_newPassword);

        startActivity(new Intent(getApplicationContext(),SuccessMessage.class));
        finish();
    }



    ////////////////////////////////////////
    private boolean isConnected(SetNewPassword setNewPassword) {
        ConnectivityManager connectivityManager = (ConnectivityManager) setNewPassword.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(wifiConn != null && wifiConn.isConnected() || mobileConn != null && mobileConn.isConnected()){
            return true;
        }else{
            return false;
        }
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SetNewPassword.this);
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

    private boolean validatePassword() {
        String val = newPassword.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            newPassword.setError("Field can not be empty");
            return false;
        } else if (val.length()<4) {
            newPassword.setError("Password should contain 4 characters!");
            return false;
        } else {
            newPassword.setError(null);
            newPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String newValue = newPassword.getEditText().getText().toString().trim();
        String confirmValue = confirmPassword.getEditText().getText().toString().trim();
        if(newValue.equals(confirmValue)){

            return true;
        }else{
            return false;
        }
    }

}