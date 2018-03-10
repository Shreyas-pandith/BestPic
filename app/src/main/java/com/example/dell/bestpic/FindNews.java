package com.example.dell.bestpic;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FindNews extends AppCompatActivity {
    String Token="your token";

    public class  Downloadtask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            Log.i("info","1");
            String result = "";

            URL url = null;
            try {
                url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                int data = inputStreamReader.read();
                Log.i("info","2");
                while (data != -1) {

                    char res = (char) data;
                    result = result + res;
                    data = inputStreamReader.read();


                }
                return result;
            } catch (MalformedURLException e) {
                Log.i("info","a");
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("info","b");
                e.printStackTrace();
            }

            Log.i("info","3");

            return "no";





        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("info",s);
            super.onPostExecute(s);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_news);
        Log.i("info","4");
         Downloadtask task =new Downloadtask();
        try {
            task.execute("http://webhose.io/filterWebContent?token="+Token+"&format=json&sort=crawled&q=%22virat%20kohli%22%20language%3Aenglish&size=5");


        }catch (Exception e)
        {  Log.i("iiii",   "gone");
            e.printStackTrace();
        }
        Log.i("info","5");
    }
}
