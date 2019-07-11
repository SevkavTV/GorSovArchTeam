package com.example.gorsovarch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    MainActivity a;
    EditText editLogin, editPassword;
    Button enter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        editLogin = (EditText)findViewById(R.id.editTextLogin);
        editPassword = (EditText)findViewById(R.id.editTextPassword);
        enter = (Button)findViewById(R.id.btnEnter);
        enter.setTextColor(getResources().getColor(R.color.DimGrey));
        editLogin.addTextChangedListener(loginTextWatcher);
        editPassword.addTextChangedListener(loginTextWatcher);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });
    }
    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String login = editLogin.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            if(login.isEmpty() || password.isEmpty())
                enter.setTextColor(getResources().getColor(R.color.DimGrey));
            else enter.setTextColor(getResources().getColor(R.color.White));
            enter.setEnabled(!login.isEmpty() && !password.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
            String login = editLogin.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            if(login.isEmpty() || password.isEmpty())
                enter.setTextColor(getResources().getColor(R.color.DimGrey));
            else enter.setTextColor(getResources().getColor(R.color.White));
            enter.setEnabled(!login.isEmpty() && !password.isEmpty());
        }
    };
}
