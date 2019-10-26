package com.example.harshal.bepro;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
//import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //private MapView mapView;


    //private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Button btn = (Button)findViewById(R.id.btn1);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);
        spinner.setOnItemSelectedListener(this);

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
//                intent.putExtra("data",String.valueOf(spinner.getSelectedItem()));
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        if (item.equals("bus3..........time= 3:00pm")){
            Toast.makeText(parent.getContext(),"Selected: " + item, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,MapsActivity.class);
            intent.putExtra("data",item);
            startActivity(intent);
        }
        else if(item.equals("Select Bus")) {
            Toast.makeText(parent.getContext(),"Select Bus", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(parent.getContext(),"Currently no data is available for " + item, Toast.LENGTH_LONG).show();
        }

//        if(item == "bus4"){
//            setContentView(R.layout.second);
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}