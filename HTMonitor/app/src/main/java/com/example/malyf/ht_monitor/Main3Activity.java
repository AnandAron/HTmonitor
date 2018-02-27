package com.example.malyf.ht_monitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);



        try {
            JSONObject jsonObject = new JSONObject(MainActivity.data);
            String[] list= new String[jsonObject.length()+1];
            list[0]="Date                 Hum      Temp       Time";
            for(int i=0;i<jsonObject.length();i++){

                JSONObject curr  = jsonObject.getJSONObject(""+i);
                String info= curr.getString("date")+"    "+curr.getString("hum")+"           "+curr.getString("temp")+"            "+curr.getString("time");
                list[i+1]=info;


            }

            ListView lv=findViewById(R.id.listView);
            ArrayAdapter adapter=new ArrayAdapter<String>(this,R.layout.list_view,R.id.textView,list);
            lv.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
