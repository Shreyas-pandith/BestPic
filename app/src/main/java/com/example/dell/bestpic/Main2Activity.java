package com.example.dell.bestpic;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main2Activity extends AppCompatActivity {


    ImageView im;
    Bitmap image ;

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
                return my;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(Main2Activity.this, "failed at line35", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(Main2Activity.this, "failed at line38", Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }
    public void set(View view)
    {


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // get the height and width of screen
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        WallpaperManager wallpaperManager =WallpaperManager.getInstance(getApplicationContext());
        try {

            wallpaperManager.suggestDesiredDimensions(width, height);
            wallpaperManager.setBitmap(image);



            Toast.makeText(Main2Activity.this, "Wallpaper changed", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        im=(ImageView)findViewById(R.id.image);
        Intent intent=getIntent();

        downloadimage task = new downloadimage();

        try {
            image=  task.execute(intent.getStringExtra("url")).get();

            im.setImageBitmap(image);
            //     im.setImageResource(R.drawable.chota);

        } catch (Exception e) {
            e.printStackTrace();
            //    Toast.makeText(MainActivity.this, "failed at line52", Toast.LENGTH_SHORT).show();
        }




    }
}
