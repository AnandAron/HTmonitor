package com.example.malyf.ht_monitor;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        EditText et= (EditText) findViewById(R.id.editTextIp);
        et.setText(p.getString("SERVER_IP","192.168.43.194:5000"));
    }

    public void commitIP(View view) {
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        EditText et= (EditText) findViewById(R.id.editTextIp);

        if(!et.getText().toString().equals("")) {

            p.edit().putString("SERVER_IP", et.getText().toString()).apply();
            MainActivity.sip=et.getText().toString();
        }
        else{
            Toast.makeText(this,"Invalid IP",Toast.LENGTH_SHORT).show();
        }
    }
}
