package com.example.jae.lab1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.example.jae.lab1.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Messages. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MessageDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MessageListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    //Declare variables for Listview, editText, and Button
    public ListView listView;
    public EditText messageBox;
    public Button sendButton;
    public ArrayList<String> list;
    public ChatAdapter messageAdapter;
    public ChatDatabaseHelper cDH;
    public SQLiteDatabase db;
    protected static final String ACTIVITY_NAME = "MessageListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        //setContentView(R.layout.activity_chat_window);

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


        if (findViewById(R.id.message_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MessageDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        MessageDetailFragment fragment = new MessageDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.message_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MessageDetailActivity.class);
                        intent.putExtra(MessageDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    public class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx){
            super(ctx, 0);
        }

        public int getCount() { return list.size(); }

        public String getItem(int position){
            return list.get(position);
        }

        public View getView(int position, View convertView, final ViewGroup parent){

            LayoutInflater inflater = MessageListActivity.this.getLayoutInflater();

            View result = null;
            if(position%2 ==0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView)result.findViewById(R.id.message_text);


            final String messageText = getItem(position);
            message.setText(messageText);

            result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle(); //remember for final project
                        arguments.putString(MessageDetailFragment.ARG_ITEM_ID, messageText);
                        MessageDetailFragment fragment = new MessageDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.message_detail_container, fragment)//replaces fragment
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MessageDetailActivity.class);
                        intent.putExtra(MessageDetailFragment.ARG_ITEM_ID, messageText);

                        context.startActivity(intent);
                    }
                }
            });

            return result;
        }



    }

}
