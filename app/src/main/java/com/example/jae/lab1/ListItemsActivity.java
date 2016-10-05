package com.example.jae.lab1;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemsActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ListItemsActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    ImageButton imgButton;
    Switch swi;
    CheckBox chBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i(ACTIVITY_NAME, "in onCreate");

        imgButton = (ImageButton) findViewById(R.id.imageButton);
        swi = (Switch) findViewById(R.id.switch1);
        chBox = (CheckBox) findViewById(R.id.checkBox);

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });


        swi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                //CharSequence text = "Switch is On"; "Switch is Off"
                if(isChecked){
                    CharSequence text = getString(R.string.switch_on);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(ListItemsActivity.this, text, duration);
                    toast.show();
                }else{
                    CharSequence text = getString(R.string.switch_off);
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(ListItemsActivity.this, text, duration);
                    toast.show();
                }
            }
        });

        chBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.dialog_message) //Add a dialog message to strings.xml

                        .setTitle(R.string.dialog_title)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                Intent resultIntent = new Intent(  );
                                resultIntent.putExtra("Response", getString(R.string.result_messages));
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        })
                        .show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgButton.setImageBitmap(imageBitmap);
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
