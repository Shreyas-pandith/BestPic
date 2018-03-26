package com.example.dell.bestpic;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FindNews extends AppCompatActivity {
    String Token="8b7db346-a2c0-4d09-abeb-93707076ca7e";
    ListView listView;
    ArrayAdapter arrayAdapter;
    ArrayList <String> arrayList=new ArrayList<>();
    ArrayList <String> arrayList1=new ArrayList<>();


    public class  Downloadtask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            URL url = null;
            try {
                url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                int data = inputStreamReader.read();

                while (data != -1) {

                    char res = (char) data;
                    result = result + res;
                    data = inputStreamReader.read();


                }
                return result;
            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }



            return "no";





        }

        @Override
        protected void onPostExecute(String s) {

            try {
                Log.i("result",s);
                JSONObject jsonObj = new JSONObject(s);
               JSONArray posts= jsonObj.getJSONArray("posts");
               int i;
               for(i=0;i<posts.length();i++)
               {    Log.i("num",String.valueOf(i));

                   JSONObject post =posts.getJSONObject(i);
                  String thread= post.getString("thread");
                   JSONObject threads = new JSONObject(thread);
                   if(threads.has("main_image" ))
                   {


                       String image=threads.getString("main_image");
                       if(!image.equals(null))
                       {
                           arrayList.add(image);
                           arrayList1.add("Image "+String.valueOf(i+1));
                           Log.i("image",image);
                           arrayAdapter.notifyDataSetChanged();
                       }

                   }




               }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(s);
        }
    }



    ////////
    private void onResponseGetImages(JSONObject response)
            throws JSONException {
        int resultCount = Integer.parseInt(response.getString("result_count"));
        if(resultCount>0){
            JSONArray imagesArray = response.getJSONArray("images");
            JSONObject temp = imagesArray.getJSONObject(0);
            JSONArray displaySizesArray = temp.getJSONArray("display_sizes");
            JSONObject temp4 = displaySizesArray.getJSONObject(0);
            String imageUr = temp4.getString("uri");
            Toast.makeText(this,
                    ""+imageUr, Toast.LENGTH_SHORT)
                    .show();
        }else{
            Toast.makeText(this,
                    "No Images Found !", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    ///////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_news);
        Intent tag_intent= getIntent();
        String tag=tag_intent.getStringExtra("tag");

        String[] splittag = tag.split("\\s+");
        int count=0;
        String query="";
        for (String s :splittag)
        {
            Log.i("tagsplit",s);
            if (count  == 0)
            {
                 query=query+"%22"+s;
            }
            else {
                query=query+"%20"+s;
            }
         count++;
        }
        Log.i("info",query);

        listView=findViewById(R.id.list);


        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList1);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent (getApplicationContext(),Main2Activity.class);
                intent.putExtra("url",arrayList.get(i));
                startActivity(intent);
            }
        });



         Downloadtask task =new Downloadtask();
        try {
            task.execute("http://webhose.io/filterWebContent?token="+Token+"&format=json&sort=crawled&q="+query+"%22%20language%3Aenglish&size=7");


        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}

