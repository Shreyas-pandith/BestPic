package com.example.dell.bestpic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

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


public class Main3Activity extends Activity {
    String Token="8b7db346-a2c0-4d09-abeb-93707076ca7e";
    private GridView gridView;
     GridViewAdapter gridAdapter;


    ArrayList <String> arrayList=new ArrayList<>();
    ArrayList <String> arrayList1=new ArrayList<>();
    ArrayList<ImageItem> imageItems = new ArrayList<>();


    public class downloadimage extends AsyncTask<String,Void,Bitmap> {




        @Override
        protected Bitmap doInBackground(String...  urls) {
            //   Toast.makeText(MainActivity.this, "entered backgrond", Toast.LENGTH_SHORT).show();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream=httpURLConnection.getInputStream();
                Bitmap my = BitmapFactory.decodeStream(inputStream);
                Log.i("num","recv");
                return my;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                // Toast.makeText(Main3Activity.this, "failed at line35", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                //  Toast.makeText(Main3Activity.this, "failed at line38", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageItems.add(new ImageItem(bitmap, "Image#" ));
            gridView.invalidateViews();
            gridView.setAdapter(gridAdapter);
            Log.i("num","adpater chnaged");

        }
    }


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
                        if(!image.equals(""))
                        {
                            arrayList.add(image);
                            downloadimage task =new downloadimage();
                            task.execute(image);




                        }

                    }




                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(s);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
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


        gridView = (GridView) findViewById(R.id.gridView);
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getApplicationContext().getResources(), R.drawable.a, null);
        Bitmap myLogo = ((BitmapDrawable) vectorDrawable).getBitmap();



        gridAdapter = new GridViewAdapter(Main3Activity.this, R.layout.grid_item_layout, imageItems);
        gridView.setAdapter( gridAdapter);

        Downloadtask task =new Downloadtask();
        try {
            task.execute("http://webhose.io/filterWebContent?token="+Token+"&format=json&sort=crawled&q="+query+"%22%20language%3Aenglish&size=5");


        }catch (Exception e)
        {
            e.printStackTrace();
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);

                //Create intent
                Intent intent = new Intent(Main3Activity.this, DetailsActivity.class);

                intent.putExtra("image", item.getImage());

                //Start details activity
                startActivity(intent);
            }
        });
    }

    /**
     * Prepare some dummy data for gridview
     */
    private ArrayList<ImageItem> getData() {



        return imageItems;
    }
}