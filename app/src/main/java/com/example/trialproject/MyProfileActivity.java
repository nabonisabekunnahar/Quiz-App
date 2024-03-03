package com.example.trialproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyProfileActivity extends AppCompatActivity {

    private EditText name,email,rollNumber;
    private LinearLayout editB;
    private Button cancelB,saveB;
    private TextView profileText;

    private LinearLayout button_layout;
    private Dialog progressDialog;
    private TextView dialogText;

    private String nameStr,rollStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name=findViewById(R.id.mp_name);
        email=findViewById(R.id.mp_email);
        rollNumber=findViewById(R.id.mp_roll);
        editB=findViewById(R.id.editB);
        cancelB=findViewById(R.id.cancelB);
        saveB=findViewById(R.id.saveB);
        profileText=findViewById(R.id.profile_text);
        button_layout=findViewById(R.id.button_layout);


        progressDialog=new Dialog(MyProfileActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText=progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Updating data...");

        disableEditing();

        editB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableEditing();
            }
        });

        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableEditing();
            }
        });


        saveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate())
                    saveData();
            }
        });

    }

    private void disableEditing()
    {
         name.setEnabled(false);
         email.setEnabled(false);
         rollNumber.setEnabled(false);

         button_layout.setVisibility(View.GONE);

         name.setText(DbQuery.myProfile.getName());
         email.setText(DbQuery.myProfile.getEmail());
         if (DbQuery.myProfile.getRoll() != null)
             rollNumber.setText(DbQuery.myProfile.getRoll());


          String profileName=DbQuery.myProfile.getName();
          profileText.setText(profileName.toUpperCase().substring(0,1));

    }

    private void enableEditing()
    {
        name.setEnabled(true);
        //email.setEnabled(true);
        rollNumber.setEnabled(true);

        button_layout.setVisibility(View.VISIBLE);


    }

    private boolean validate()
    {
       nameStr=name.getText().toString();
       rollStr=rollNumber.toString();

       if (nameStr.isEmpty())
       {
           name.setError("Name can not be empty");
           return false;
       }

       if (! rollStr.isEmpty())
       {
           if (! (rollStr.length() == 7 ) && (TextUtils.isDigitsOnly(rollStr)))
           {
               rollNumber.setError("Enter valid roll number");
               return false;
           }

       }

       return true;

    }


    private void saveData()
    {
       progressDialog.show();

       if (rollStr.isEmpty())
          rollStr=null;

       DbQuery.saveProfileData(nameStr, rollStr, new MyCompleteListener() {
           @Override
           public void onSuccess() {
               Toast.makeText(MyProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

               disableEditing();
               progressDialog.dismiss();

           }

           @Override
           public void onFailure() {

               Toast.makeText(MyProfileActivity.this, "Something went wrong ! PLease try again later !", Toast.LENGTH_SHORT).show();
               progressDialog.dismiss();
           }
       });

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            MyProfileActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}