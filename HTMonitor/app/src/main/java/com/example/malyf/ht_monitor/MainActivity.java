package com.example.malyf.ht_monitor;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.view.AsyncLayoutInflater;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public static String sip;
    public static String query;
    private DatePicker datePicker;
    private DatePicker datePicker2;
    private Calendar calendar;
    private Calendar calendar2;
    private TextView dateView;
    private TextView from;
    private TextView to;
    private int year, month, day,year2,month2,day2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sip=p.getString("SERVER_IP","192.168.43.194:5000");
        from = findViewById(R.id.editText);
        to =findViewById(R.id.editText2);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        year2 = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        month2 = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        day2 = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        showDate2(year, month+1, day);
    }
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }
    public void setDate2(View view) {
        showDialog(9999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        if (id == 9999) {
            return new DatePickerDialog(this,
                    myDateListener2, year2, month2, day2);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };
    private DatePickerDialog.OnDateSetListener myDateListener2 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate2(arg1, arg2+1, arg3);
                }
            };
    private void showDate(int year, int month, int day) {
        from.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
    private void showDate2(int year, int month, int day) {
        to.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    public void post(View view) throws ExecutionException, InterruptedException, JSONException {

        TextView et,et2;
        TextView tv= findViewById(R.id.response);
        et = findViewById(R.id.editText);
        et2 = findViewById(R.id.editText2);
        query="{\"from\":\""+et.getText().toString()+"\",\"to\":\""+et2.getText().toString()+"\"}";
        Log.d("################",query);
        String s=new PostTask().execute().get();
        Log.e("ReturnVal", s);
        tv.setText(s);
        String json = s;



            JSONObject obj = new JSONObject(json);
            Log.d("My App", ""+obj.length());
            GraphView graph = (GraphView) findViewById(R.id.graph);
            DataPoint[] humDataPoints= new DataPoint[obj.length()];
            DataPoint[] tempDataPoints= new DataPoint[obj.length()];
        //Date d=new Date();
        //Date d1= new Date(2018,02,01);
        //Date d2= new Date(2018,02,28);
            for(int i=0;i<obj.length();i++){
                JSONObject curr  = obj.getJSONObject(""+i);

                Log.d("My Apptesss", curr.getString("time"));
                Log.d("My Apptesss########)))", curr.getString("time").substring(3, 5));

                /*if(curr.getString("time").charAt(1)!=':') {
                    d = new Date(Integer.parseInt(curr.getString("date").substring(0, 4)),
                            Integer.parseInt(curr.getString("date").substring(5, 7)),
                            Integer.parseInt(curr.getString("date").substring(8, 10)),
                            Integer.parseInt(curr.getString("time").substring(0, 2)),
                            Integer.parseInt(curr.getString("time").substring(3, 5),
                            Integer.parseInt(curr.getString("time").substring(6, 8))));
                }
                else d= new Date(Integer.parseInt(curr.getString("date").substring(0,4)),
                        Integer.parseInt(curr.getString("date").substring(5,7)),
                        Integer.parseInt(curr.getString("date").substring(8,10)),
                        Integer.parseInt(curr.getString("time").substring(0,1)),
                        Integer.parseInt(curr.getString("time").substring(2,4)),
                        Integer.parseInt(curr.getString("time").substring(5,7)));

               */
                humDataPoints[i]=new DataPoint(i,curr.getInt("hum"));
                tempDataPoints[i]=new DataPoint(i,curr.getInt("temp"));
                //Log.d("My App", ""+curr.getInt("hum"));
            }
            LineGraphSeries<DataPoint> humSeries = new LineGraphSeries<>(humDataPoints);
            LineGraphSeries<DataPoint> tempSeries = new LineGraphSeries<>(tempDataPoints);
            tempSeries.setColor(Color.RED);
            graph.removeAllSeries();
            graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
            graph.addSeries(humSeries);
            graph.addSeries(tempSeries);

        // set date label formatter
        //graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this.getApplication()));
        //graph.getGridLabelRenderer().setNumHorizontalLabels(3);

       // graph.getViewport().setMinX(d1.getTime());
        //Log.d("My App", ""+d2.getTime());
       // graph.getViewport().setMaxX(d2.getTime());
        //graph.getViewport().setXAxisBoundsManual(true);
        //graph.getGridLabelRenderer().setHumanRounding(false);

    }

    public void changeIP(View view) {
        Intent i= new Intent(this,Main2Activity.class);
        startActivity(i);
    }
}
class PostTask extends AsyncTask<URL,Void,String> {
    String response="";
    @Override
    protected String doInBackground(URL... urls) {
        try {

            URL url=new URL("http://"+MainActivity.sip+"/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(MainActivity.query.getBytes());
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            while ((line = br.readLine()) != null) {
                response += line;
                Log.e("Response", response);



            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }return response;
    }


}