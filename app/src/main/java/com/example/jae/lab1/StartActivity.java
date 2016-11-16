package com.example.jae.lab1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "StartActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.i(ACTIVITY_NAME, "in onCreate");

        Button button = (Button) findViewById(R.id.button);
        Button startChat = (Button) findViewById(R.id.startChat);
        Button wButton = (Button) findViewById(R.id.weatherButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
                startActivityForResult(intent, 5);
            }
        });

        startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(ACTIVITY_NAME, "User clicked Start Chat");

                Intent intent = new Intent(StartActivity.this, MessageListActivity.class);
                startActivity(intent);

            }
        });

        wButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(ACTIVITY_NAME, "User clicked Check Forecast");

                Intent intent = new Intent(StartActivity.this, WeatherForecast.class);
                startActivity(intent);
            }
        });

    }

    protected void onActivityResult(int requestCode, int responseCode, Intent data){
        if (requestCode == 5){
            Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
        }
        if(responseCode == Activity.RESULT_OK){
            String messagePassed = data.getStringExtra("Response");
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(this, messagePassed, duration);
            toast.show();
        }
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
