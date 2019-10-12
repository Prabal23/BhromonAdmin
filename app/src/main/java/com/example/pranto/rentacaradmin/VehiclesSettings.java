package com.example.pranto.rentacaradmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Pranto on 7/5/2018.
 */

public class VehiclesSettings extends AppCompatActivity{

    String id, name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setcar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");

        final EditText rent = (EditText)findViewById(R.id.rent);
        rent.setText(name);

        LinearLayout add = (LinearLayout)findViewById(R.id.info);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputStream is = null;
                String rents = rent.getText().toString();
                if(rents.contains(" ")){
                    rents = rents.replace(" ", "%20");
                }

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("id", id));
                nameValuePairs.add(new BasicNameValuePair("name", rents));

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://bhromondata.banijyadarpan.com/rentacar/caredit.php");
                    //httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                    String msg = "Vehicles Updated Successfully";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    finish();
                    Intent intent = new Intent(getBaseContext(), Vehicles.class);
                    startActivity(intent);
                }
                catch(ClientProtocolException e)
                {
                    Log.e("ClientProtocol", "Log_tag");
                    e.printStackTrace();
                }  catch (IOException e) {
                    Log.e("Log_tag", "IOException");
                    e.printStackTrace();
                }
            }
        });

        LinearLayout delete = (LinearLayout)findViewById(R.id.info1);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(VehiclesSettings.this);
                builder1.setIcon(R.drawable.bhromon_logo);
                builder1.setTitle("Delete");
                builder1.setMessage("Want to delete Item?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id1) {
                                InputStream is = null;

                                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                                nameValuePairs.add(new BasicNameValuePair("id", id));

                                try{
                                    HttpClient httpClient = new DefaultHttpClient();
                                    HttpPost httpPost = new HttpPost("http://bhromondata.banijyadarpan.com/rentacar/cardelete.php");
                                    //httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                                    HttpResponse response = httpClient.execute(httpPost);
                                    HttpEntity entity = response.getEntity();
                                    is = entity.getContent();
                                    String msg = "Vehicles Deleted Successfully";
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                    finish();
                                    Intent intent = new Intent(getBaseContext(), Vehicles.class);
                                    startActivity(intent);
                                }
                                catch(ClientProtocolException e)
                                {
                                    Log.e("ClientProtocol", "Log_tag");
                                    e.printStackTrace();
                                }  catch (IOException e) {
                                    Log.e("Log_tag", "IOException");
                                    e.printStackTrace();
                                }
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id1) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }
}
