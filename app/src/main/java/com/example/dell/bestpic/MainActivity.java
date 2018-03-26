package com.example.dell.bestpic;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ListView listView;
    ArrayAdapter <String> arrayAdapter;
    SQLiteDatabase data1;
    EditText text;
    int i = 1;
    ArrayList<String> names;

    public void save(View view) {
        EditText text=(EditText)findViewById(R.id.content);
        Log.i("info", String.valueOf(text.getText()));
        try {
            String sQL = "INSERT INTO favorite(name) VALUES(?)";
            SQLiteStatement sqLiteStatement = data1.compileStatement(sQL);
            Log.i("info", String.valueOf(text.getText()));
            sqLiteStatement.bindString(1, String.valueOf(text.getText()));
            sqLiteStatement.execute();
        } catch (Exception e) {
            Log.i("erroir", e.toString());

        }
        show();
    }
    public void show() {
        names.clear();
        Cursor c = data1.rawQuery("SELECT * FROM   favorite", null);
        if(c != null && c.moveToFirst()){
            int nameIndex = c.getColumnIndex("name");
                names.add(c.getString(nameIndex));
            while (c.moveToNext())
            {
                names.add(c.getString(nameIndex));
            }
            arrayAdapter.notifyDataSetChanged();
            c.close();
        }
        arrayAdapter.notifyDataSetChanged();
      /*  c.moveToFirst();
        int nameIndex = c.getColumnIndex("name");
        if(c!=null)
            names.add(c.getString(nameIndex));
        while (c.moveToNext())
        {
            names.add(c.getString(nameIndex));
        }
        arrayAdapter.notifyDataSetChanged();*/
    }

    public void Find_news( View view)
    {
        Intent intent =new Intent(getApplicationContext(),FindNews.class);

        startActivity(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("info","start");
        names=new ArrayList<String>();

        data1 = this.openOrCreateDatabase("Wallpaper", MODE_PRIVATE, null);
        //data.execSQL("DELETE FROM  favorite");
        data1.execSQL("CREATE TABLE IF NOT EXISTS favorite( name STRING) ");

        // listView=(ListView)findViewById(R.id.main);

        listView=(ListView)findViewById(R.id.listmain);
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        listView.setAdapter(arrayAdapter);
        show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent (getApplicationContext(),FindNews.class);
                intent.putExtra("tag",names.get(i));
                startActivity(intent);
            }
        });
    }

}


