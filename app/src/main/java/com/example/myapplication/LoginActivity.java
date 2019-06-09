package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    Button login, joinin;
    EditText edit_ID, edit_PW;
    String id, pw;

    Intent intent;

    CheckBox idCheck, loginCheck;

    private DatabaseReference mPostReference;

    SharedPreferences loginPref;
    SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        intent = new Intent(LoginActivity.this, JoininActivity.class);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        login = (Button)findViewById(R.id.button_login);
        joinin = (Button)findViewById(R.id.button_joinin);

        idCheck = (CheckBox)findViewById(R.id.idSave);
        loginCheck = (CheckBox)findViewById(R.id.maintainLogin);

        // 기존에 로그인한 경우 바로 MainActivity로 intent
        loginPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = loginPref.edit();

        edit_ID = (EditText)findViewById(R.id.editText_id);
        edit_PW = (EditText)findViewById(R.id.editText_pw);

        String defaultValue = loginPref.getString("pw", null);
        String defaultValue2 = loginPref.getString("id", null);
        if(defaultValue2!=null && defaultValue!=null){
            intent.putExtra("ID", defaultValue2);
            startActivity(intent);
            finish();
        } else if(defaultValue2 != null){
            edit_ID.setText(defaultValue2);
            idCheck.setChecked(true);
        }



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = edit_ID.getText().toString();
                pw = edit_PW.getText().toString();

                // 회원정보 확인
                check();
            }
        });

        joinin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // firebase에 유저 등록 후 MainActivity로 intent
                intent.putExtra("data", "Test popup");
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                int popup_add = data.getExtras().getInt("button");
                if(popup_add == 1){
                    Toast.makeText(LoginActivity.this, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show();
                    id = data.getExtras().getString("ID");

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("ID", id);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "회원가입를 취소했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void check(){
        mPostReference.child("list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isRight = false;
                for(DataSnapshot postSnapshot : dataSnapshot.child(getString(R.string.id)).getChildren()){
                    FirebaseID get = postSnapshot.getValue(FirebaseID.class);
                    if(get.id.equals(id) && get.pw.equals(pw)){
                        isRight = true;
                        break;
                    }
                }
                if(isRight){
                    // 정상적으로 로그인 된 경우 MainActivity로 intent
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    if(loginCheck.isChecked()){
                        editor.putString("id", id);
                        editor.putString("pw", pw);
                        editor.commit();
                    } else if(idCheck.isChecked()){
                        editor.putString("id", id);
                        editor.commit();
                    }
                    intent.putExtra("ID", id);
                    startActivity(intent);
                    finish();
                } else{
                    Toast.makeText(LoginActivity.this, "로그인에 실패하셨습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
