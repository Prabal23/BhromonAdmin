package com.example.pranto.rentacaradmin;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
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

public class AddHotelRoom extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addhotelroom);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final EditText rent = (EditText)findViewById(R.id.rent);

        LinearLayout add = (LinearLayout)findViewById(R.id.info);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputStream is = null;
                String rents = rent.getText().toString();
                if(rents.contains(" ")){
                    rents = rents.replace(" ", "%20");
                }

                if(rents.equals("")){
                    Toast.makeText(AddHotelRoom.this, "Please fill up the rent section (অনুগ্রহকরে ভাড়া অঙ্গশটি পূরণ করুন)", Toast.LENGTH_LONG).show();
                }else{
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("name", rents));

                    try{
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost("http://bhromondata.banijyadarpan.com/rentacar/roominsert.php");
                        //httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                        HttpResponse response = httpClient.execute(httpPost);
                        HttpEntity entity = response.getEntity();
                        is = entity.getContent();
                        String msg = "Room Inserted Successfully";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent = new Intent(getBaseContext(), RoomType.class);
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
            }
        });
    }
}