package com.example.jae.lab1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        //initialize variables
        listView = (ListView) findViewById(R.id.listView1);
        messageBox = (EditText) findViewById(R.id.messageBox);
        sendButton = (Button) findViewById(R.id.SendButton);
        list = new ArrayList<String>();


        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list.add(messageBox.getText().toString());

                messageAdapter.notifyDataSetChanged();  //restarts the process of getCount() / getView()
                //Clear the textbox
                messageBox.setText("");
            }
        });

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


