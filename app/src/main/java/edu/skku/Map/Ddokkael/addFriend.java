package edu.skku.Map.Ddokkael;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

public class addFriend extends AppCompatActivity {
    private DatabaseReference mPostReference;

    private String name1, name2;

    EditText name1Et, name2Et;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        name1Et = (EditText)findViewById(R.id.name1Et);
        name2Et = (EditText)findViewById(R.id.name2Et);
        btn = (Button)findViewById(R.id.buttonFriend);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name1 = name1Et.getText().toString();
                name2 = name2Et.getText().toString();

                if(name1.length()*name2.length()==0){
                    Toast.makeText(addFriend.this, "Data is missing", Toast.LENGTH_SHORT).show();
                } else{
                    if(mPostReference.equalTo(name1)!=null&&mPostReference.equalTo(name2)!=null){
                        postFirebaseDatabase(true);
                    }
                }
            }
        });
    }
    public void postFirebaseDatabase(boolean add){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        Map<String, Object> postValues2 = null;
        if(add){
            FirebaseName name = new FirebaseName(name1);
            postValues = name.toMap();
            FirebaseName name_ = new FirebaseName(name2);
            postValues2 = name_.toMap();
        }
        childUpdates.put("/Person_List/"+name2+"/Friend_List/"+name1, postValues);
        childUpdates.put("/Person_List/"+name1+"/Friend_List/"+name2, postValues2);
        mPostReference.updateChildren(childUpdates);
        Toast.makeText(addFriend.this, "add complete", Toast.LENGTH_SHORT).show();
        clearET();

    }
    void clearET(){
        name1Et.setText("");
        name2Et.setText("");
        name1="";
        name2="";
    }
}
