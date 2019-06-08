package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class AddFriend extends Activity {
    private DatabaseReference mPostReference;

    EditText editText;
    Button button, addButton;
    TextView textView;
    ImageView imageView;

    String id;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_friend);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        mPostReference = FirebaseDatabase.getInstance().getReference();

        editText = (EditText)findViewById(R.id.findFriend);
        button = (Button)findViewById(R.id.findFriendBtn);
        textView = (TextView)findViewById(R.id.findFriendRst);
        imageView = (ImageView)findViewById(R.id.gB);
        addButton = (Button)findViewById(R.id.addfriend);

        addButton.setVisibility(View.GONE);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = editText.getText().toString();
                if(id.length()==0){
                    Toast.makeText(AddFriend.this, "바르게 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (id.equals(name)) {
                    textView.setText("본인입니다.");
                    addButton.setVisibility(View.GONE);
                    return;
                }
                check(0);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = textView.getText().toString();
                check(1);
            }
        });
    }

    public void check(final int check_case){
        mPostReference.child("list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(check_case == 0){
                    boolean isExist = false, isFriend = false;
                    for (DataSnapshot postSnapshot : dataSnapshot.child(getString(R.string.id)).getChildren()) {
                        FirebaseID get = postSnapshot.getValue(FirebaseID.class);
                        if (get.id.equals(id)) {
                            isExist = true;
                            break;
                        }
                    }
                    if (isExist) {
                        for (DataSnapshot postSnapshot : dataSnapshot.child(getString(R.string.person)).child(name).child(getString(R.string.friend)).getChildren()) {
                            FirebaseFriend get = postSnapshot.getValue(FirebaseFriend.class);
                            if (get.name.equals(id)) {
                                isFriend = true;
                                break;
                            }
                        }
                    }
                    if (!isExist) {
                        textView.setText("아이디가 존재하지 않습니다.");
                        addButton.setVisibility(View.GONE);
                    } else if (isFriend) {
                        textView.setText("이미 친구입니다.");
                        addButton.setVisibility(View.GONE);
                    } else {
                        textView.setText(id);
                        addButton.setVisibility(View.VISIBLE);
                    }
                }
                else if(check_case==1){
                    boolean isRequestedto = false, isRequestedfrom = false;
                    for(DataSnapshot postSnapshot : dataSnapshot.child(getString(R.string.request)).child(name).child("from").getChildren()){
                        FirebaseFriend get = postSnapshot.getValue(FirebaseFriend.class);
                        if(get.name.equals(id)){
                            isRequestedfrom = true;
                            postSnapshot.getRef().removeValue();
                            break;
                        }
                    }
                    if(isRequestedfrom){
                        for(DataSnapshot postSnapshot : dataSnapshot.child(getString(R.string.request)).child(id).child("to").getChildren()){
                            FirebaseFriend get = postSnapshot.getValue(FirebaseFriend.class);
                            if(get.name.equals(name)){
                                postSnapshot.getRef().removeValue();
                                break;
                            }
                        }
                        postFirebaseDatabase(1);
                        end();
                    } else{
                        for(DataSnapshot post : dataSnapshot.child(getString(R.string.request)).child(name).child("to").getChildren()){
                            FirebaseFriend get = post.getValue(FirebaseFriend.class);
                            if(get.name.equals(id)){
                                isRequestedto = true;
                                break;
                            }
                        }
                        if(isRequestedto){
                            Toast.makeText(AddFriend.this, "이미 요청을 보냈습니다.", Toast.LENGTH_SHORT).show();
                        } else{
                            postFirebaseDatabase(0);
                            end();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void postFirebaseDatabase(int pushing_case){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        Map<String, Object> postValues2 = null;
        String toastms = "";

        if(pushing_case==0){
            FirebaseFriend from = new FirebaseFriend(name);
            FirebaseFriend to = new FirebaseFriend(id);
            postValues = from.toMap();
            postValues2 = to.toMap();
            childUpdates.put("/list/"+getString(R.string.request) +"/"+name+"/to/"+id, postValues2);
            childUpdates.put("/list/"+getString(R.string.request)+"/"+id+"/from/"+name, postValues);
            toastms= "친구 요청을 보냈습니다.";
        } else if(pushing_case == 1){
            FirebaseFriend me = new FirebaseFriend(id);
            FirebaseFriend fr = new FirebaseFriend(name);
            postValues = me.toMap();
            postValues2 = fr.toMap();
            childUpdates.put("/list/"+getString(R.string.person)+"/"+id+"/"+getString(R.string.friend)+"/"+name, postValues2);
            childUpdates.put("/list/"+getString(R.string.person)+"/"+name+"/"+getString(R.string.friend)+"/"+id, postValues);
            toastms = "친구 요청을 수락하였습니다.";
        }
        mPostReference.updateChildren(childUpdates);
        Toast.makeText(AddFriend.this, toastms, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    public void end(){
        Intent intent = new Intent();
        intent.putExtra("button",0);
        setResult(RESULT_OK, intent);

        finish();
    }
}
