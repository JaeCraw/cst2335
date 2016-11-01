package com.example.jae.lab1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    //Declare variables for Listview, editText, and Button
    public ListView listView;
    public EditText messageBox;
    public Button sendButton;
    public ArrayList<String> list;
    public ChatAdapter messageAdapter;
    public ChatDatabaseHelper cDH;
    public SQLiteDatabase db;
    protected static final String ACTIVITY_NAME = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        //initialize variables
        listView = (ListView) findViewById(R.id.listView1);
        messageBox = (EditText) findViewById(R.id.messageBox);
        sendButton = (Button) findViewById(R.id.SendButton);
        list = new ArrayList<String>();

        cDH = new ChatDatabaseHelper( this );
        db = cDH.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + ChatDatabaseHelper.TABLE_NAME, null);

        c.moveToFirst();

        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                list.add(messageBox.getText().toString());

                ContentValues cValues = new ContentValues();
                cValues.put(ChatDatabaseHelper.KEY_MESSAGE, messageBox.getText().toString());
                db.insert(ChatDatabaseHelper.TABLE_NAME, "NullPlaceholder", cValues);
                messageAdapter.notifyDataSetChanged();  //restarts the process of getCount() / getView()
                //Clear the textbox
                messageBox.setText("");
            }
        });

        while(!c.isAfterLast()){
            Log.i(ACTIVITY_NAME, "Cursor's column count=" + c.getColumnCount());
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + c.getString(c.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));

            String rowValue = c.getString(c.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
            list.add(rowValue);
            messageAdapter.notifyDataSetChanged();
            c.moveToNext();
        }

    }

    public void onDestroy(){
        super.onDestroy();
        db.close();
    }

    public class ChatAdapter extends ArrayAdapter<String>{

        public ChatAdapter(Context ctx){
            super(ctx, 0);
        }

        public int getCount() { return list.size(); }

        public String getItem(int position){
            return list.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){

            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

            View result = null;
            if(position%2 ==0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }



    }

}


