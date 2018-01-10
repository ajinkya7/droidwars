package com.example.ajinkya.droidwars;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;

/**
 * Created by Ajinkya on 09-12-2017.
 */

public class Feed extends AppCompatActivity {
     private Button buttonSignout;
     private Button buttonSend;
     private EditText editText;

    private FirebaseListAdapter<ChatMessage> adapter;
     private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);
        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();

        buttonSignout = (Button) findViewById(R.id.buttonSignout);
        buttonSend = (Button) findViewById(R.id.buttonSend);

        displayChatMessages();

        buttonSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(Feed.this, MainActivity.class));
                finish();
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText = (EditText) findViewById(R.id.editText);
                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(new ChatMessage(editText.getText().toString(),
                                FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getEmail()));

                editText.setText("");


            }
        });



    }

    private void displayChatMessages()  {

        final ListView listOfMessages = (ListView)findViewById(R.id.list);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);



                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd/MM/yy [HH:mm]",
                        model.getMessageTime()));



            }
        };

        listOfMessages.setAdapter(adapter);
    }
}
