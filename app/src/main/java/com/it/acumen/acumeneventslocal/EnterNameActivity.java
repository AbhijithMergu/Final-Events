package com.it.acumen.acumeneventslocal;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        final EditText editText = (EditText) findViewById(R.id.name);
        Button b = (Button) findViewById(R.id.enter_name_button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",name);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }
}
