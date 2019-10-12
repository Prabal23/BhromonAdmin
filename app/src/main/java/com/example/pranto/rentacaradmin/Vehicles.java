package com.example.pranto.rentacaradmin;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Pranto on 7/5/2018.
 */

public class Vehicles extends AppCompatActivity{
    GridView gridView;
    GifImageView loader;
    private String JSON_STRING;
    String name;
    Dialog dialog;
    ImageView calling;
    TextView packnum, packnumbd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicles);

        packnum = (TextView)findViewById(R.id.packnum);
        packnumbd = (TextView)findViewById(R.id.packnumbd);
        gridView = (GridView)findViewById(R.id.gridView);
        loader = (GifImageView) findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);
        getCars();

        TextView add = (TextView)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddVehicles.class);
                startActivity(intent);
            }
        });
    }

    private void getCars(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loader.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loader.setVisibility(View.INVISIBLE);
                JSON_STRING = s;
                showCar();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest("http://bhromondata.banijyadarpan.com/rentacar/carlist.php");
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showCar() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("carlist");

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString("id");
                String name = jo.getString("name");
                byte[] b1 = name.getBytes("UTF-8");
                String carname = new String(b1, "UTF-8");

                HashMap<String, String> employees = new HashMap<>();
                employees.put("id", id);
                employees.put("name", name);
                list.add(employees);
            }
            int size = list.size();
            String s = String.valueOf(size);
            s = s.replace("0", "০").replace("1", "১").replace("2", "২").replace("3", "৩").replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭").replace("8", "৮").replace("9", "৯");
            packnum.setText(size+" Vehicles");
            packnumbd.setText(" ("+s+"টি যানবাহন)");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                Vehicles.this, list, R.layout.car_list_item,
                new String[]{"id", "name"},
                new int[]{R.id.id, R.id.txtName});

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> map = (HashMap) adapterView.getItemAtPosition(i);
                String id = map.get("id").toString();
                name = map.get("name").toString();
                Intent intent = new Intent(getBaseContext(), VehiclesSettings.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }
}
