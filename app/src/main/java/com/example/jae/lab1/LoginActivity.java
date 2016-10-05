package com.example.jae.lab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "LoginActivity";

    EditText emailInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME, "in onCreate");

        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        emailInput = (EditText) findViewById(R.id.userLoginBox);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "in onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "in onStart");

        SharedPreferences prefs = getSharedPreferences("DefaultEmail", Context.MODE_PRIVATE);
        emailInput.setText(prefs.getString("DefaultEmail", "email@domain.com"));

    }

    public void buttonClick (View v){
        SharedPreferences prefs = getSharedPreferences("DefaultEmail", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("DefaultEmail", emailInput.getText().toString());
        editor.commit();

        Intent intent = new Intent(LoginActivity.this, StartActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onPause() {

        super.onPause();
        Log.i(ACTIVITY_NAME, "in onPause");


    }

    @Override
    protected void onStop() {

        super.onStop();
        Log.i(ACTIVITY_NAME, "in onStop");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Log.i(ACTIVITY_NAME, "in onDestroy");
    }
}
