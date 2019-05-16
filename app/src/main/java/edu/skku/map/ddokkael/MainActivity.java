package edu.skku.map.ddokkael;

import android.content.ClipboardManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    ClipBoard cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cb = new ClipBoard();

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                cb.cm = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                cb.initialize();
                cb.searchDatafromClipBoard();
                TextView textView = (TextView)findViewById(R.id.text);
                if(cb.isExist){
                    textView.setText(cb.parseToString());
                } else{
                    textView.setText("no data");
                }
            }
        });
    }
}
