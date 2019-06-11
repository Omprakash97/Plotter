package com.example.prakash.plotter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MainActivity extends AppCompatActivity {
    EditText coor;
    SQLiteDatabase db;
    GraphView graphView;
    LineGraphSeries series;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coor = (EditText)findViewById(R.id.cood);
        graphView = findViewById(R.id.graphView);

        series= new LineGraphSeries();
        graphView.addSeries(series);

        db = openOrCreateDatabase("plots",MODE_PRIVATE,null);

      //  plots();
    }

    public void update(View v){
        plots();
    }

    public void plots(){
        Cursor r = db.rawQuery("Select * from xy",null);
        Integer count = r.getCount();
        Integer i=0;

        if(count > 0){
          DataPoint[] dp = new DataPoint[count];
            if (r.moveToFirst()) {
                do {
                    dp[i] = new DataPoint(r.getInt(0),r.getInt(1));
                    i++;
                } while (r.moveToNext());
            }
            r.close();

        series.resetData(dp);
        }
    }

    public void push(View v){
        String data = coor.getText().toString();
        String[] d = data.split(",");
        Integer x,y;
        x=Integer.parseInt(d[0]);
        y=Integer.parseInt(d[1]);

        // CREATING A TABLE
        db.execSQL("CREATE TABLE IF NOT EXISTS xy(id INTEGER PRIMARY KEY AUTOINCREMENT, x INTEGER, y INTEGER);");

        // CHECKING IF THE ENTRY IS GREATER THAT 50 , AND CLEAR THE TABLE IF IT EXCEEDS

        Cursor r = db.rawQuery("Select * from xy",null);
        Integer count = r.getCount();

            if(count > 100){
                db.execSQL("DROP TABLE xy");
                db.execSQL("CREATE TABLE IF NOT EXISTS xy(id INTEGER PRIMARY KEY AUTOINCREMENT, x INTEGER, y INTEGER)");
            }

        // NOW COMES THE INSERT PART :

        db.execSQL("INSERT INTO xy(x,y) VALUES("+x+","+y+");");
            coor.setText("");
        Toast.makeText(this,"plot inserted",Toast.LENGTH_LONG).show();
    }

}
