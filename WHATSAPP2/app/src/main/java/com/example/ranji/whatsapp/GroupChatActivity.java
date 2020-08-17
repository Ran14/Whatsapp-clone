package com.example.ranji.whatsapp;

import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {

        private ImageButton sendButton;
        private ScrollView scrlview;
        private EditText inputMessege;
        private TextView displaymess;
        private Toolbar mToolbar;
        private String Currentgroupname,CurrentUserId,CurrentUserName,CurrentDate,CurrentTime;
        private FirebaseAuth mAuth;
        private DatabaseReference UsersRef,groupNameref,GroupMessegeKeyRef;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_group_chat);
                Currentgroupname=getIntent().getExtras().get("groupname").toString();
                Toast.makeText(GroupChatActivity.this,Currentgroupname,Toast.LENGTH_SHORT).show();

                mAuth=FirebaseAuth.getInstance();
                CurrentUserId=mAuth.getCurrentUser().getUid();
                UsersRef=FirebaseDatabase.getInstance().getReference().child("Users");
                groupNameref=FirebaseDatabase.getInstance().getReference().child("Groups").child(Currentgroupname);


                initializeFields();
                getUserInfo();
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SaveMessegeToDatabase();

                    inputMessege.setText("");
                    scrlview.fullScroll(ScrollView.FOCUS_DOWN);

                }
            });

        }

        @Override
        protected void onStart()
        {
                super.onStart();

                groupNameref.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot,String s)
                        {
                                if(dataSnapshot.exists())
                                {
                                        sendMesseges(dataSnapshot);
                                }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot,String s) {
                                if(dataSnapshot.exists())
                                {
                                        sendMesseges(dataSnapshot);
                                }
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                });

        }



        private void initializeFields()
        {
                scrlview=(ScrollView)findViewById(R.id.my_scroll_view);
                sendButton=(ImageButton)findViewById(R.id.sentMessegeButton);
                inputMessege=(EditText)findViewById(R.id.text_input_message);
                displaymess=(TextView)findViewById(R.id.group_chat_text_display);
                mToolbar=(Toolbar)findViewById(R.id.group_chat);
                setSupportActionBar(mToolbar);
                getSupportActionBar().setTitle(Currentgroupname);
        }

        private void getUserInfo()
        {
                UsersRef.child( CurrentUserId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                                if(dataSnapshot.exists())
                                {
                                        CurrentUserName=dataSnapshot.child("name").getValue().toString();

                                }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                });
        }
        private void SaveMessegeToDatabase()
        {
                String Messege=inputMessege.getText().toString();
                String messegeKey=groupNameref.push().getKey();

                if(TextUtils.isEmpty(Messege))
                {

                        Toast.makeText(GroupChatActivity.this,"Please write Your messege first",Toast.LENGTH_SHORT).show();
                }
                else
                {
                        Calendar calforDate =Calendar.getInstance();
                        SimpleDateFormat CurrentDateFormat=new SimpleDateFormat("MMM dd, YYYY");
                        CurrentDate=CurrentDateFormat.format(calforDate.getTime());

                        Calendar calforTime =Calendar.getInstance();
                        SimpleDateFormat CurrentTimeFormat=new SimpleDateFormat("hhh mm: a");
                        CurrentTime=CurrentTimeFormat.format(calforTime.getTime());
                        HashMap<String,Object> groupMessegeKey=new HashMap<>();
                        groupNameref.updateChildren(groupMessegeKey);
                        GroupMessegeKeyRef=groupNameref.child(messegeKey);
                        HashMap<String,Object>messegeInfoMap=new HashMap<>();
                        messegeInfoMap.put("name",CurrentUserName);
                        messegeInfoMap.put("messege",Messege);
                        messegeInfoMap.put("date",CurrentDate);
                        messegeInfoMap.put("time",CurrentTime);
                        GroupMessegeKeyRef.updateChildren(messegeInfoMap);
                }

        }
        private void sendMesseges(DataSnapshot dataSnapshot)
        {
                Iterator iterator=dataSnapshot.getChildren().iterator();
                while (iterator.hasNext())
                {
                        String ChatDate=(String)((DataSnapshot)iterator.next()).getValue();
                        String ChatMessege=(String)((DataSnapshot)iterator.next()).getValue();
                        String ChatName=(String)((DataSnapshot)iterator.next()).getValue();
                        String ChatTime=(String)((DataSnapshot)iterator.next()).getValue();
                        displaymess.append(ChatName + ":\n" +ChatMessege + "\n"+ChatTime + "    "+ChatDate + "\n\n\n");

                        scrlview.fullScroll(ScrollView.FOCUS_DOWN);


                }
        }
}
